package GeekBrians.Slava_5655380.viewmodel

import GeekBrians.Slava_5655380.model.DebugRepository
import GeekBrians.Slava_5655380.model.MovieMetadata
import GeekBrians.Slava_5655380.model.RVItemState
import GeekBrians.Slava_5655380.model.Repository
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

// TODO: реализовать инициализацию ленты(первое заполнение ленты элементами),
//             предоставлющую готовую к взаимодействиям ленту и возможностью задать
//             начальную позицию ленты
// TODO: проконтролировать чтобы numberOfBufferingItems, feedBufferMaxSize были
//              в пределах допустимых значений(например numberOfBufferingItems должно быть > 0)
class MainViewModel(
    private var feedInitialPosition: Int = 0,
    private val repository: Repository = DebugRepository(),
    private val numberOfBufferingItems: Int = 2,
    private val feedBufferMaxSize: Int = 10 - numberOfBufferingItems
) : ViewModel() {
    var adapter: Adapter = Adapter(this)
    var feedBuffer: ArrayList<RVItemState> = arrayListOf()
    private val uiThreadHandler = Handler(Looper.getMainLooper())

    fun getItemCount(): Int {
        return feedBuffer.size
    }

    fun getItem(index: Int): RVItemState {
        return feedBuffer[index]
    }

    var rangeInsertStart: Int? = null
    var rangeInsertCount: Int? = null

    var bottomIsFetching: Boolean = false
    var topIsFetching: Boolean = false

    fun feed(feedBottom: Boolean) {
        if (feedBottom && bottomIsFetching || !feedBottom && topIsFetching) {
            return
        }
        if (feedBottom) {
            bottomIsFetching = true
        } else {
            topIsFetching = true
        }
        fetchData(feedBottom)
    }

    private fun cropFeedBuffer(cropBottom: Boolean) {
        if (cropBottom) {
            var itemsToCrop = feedBuffer.size - feedBufferMaxSize
            if (feedBuffer[0] is RVItemState.Loading) {
                itemsToCrop -= 1
                feedBuffer.subList(1, itemsToCrop + 1).clear()
                uiThreadHandler.post {
                    adapter.notifyItemRangeRemoved(
                        1,
                        itemsToCrop
                    )
                }
            } else {
                feedBuffer.subList(0, itemsToCrop).clear()
                uiThreadHandler.post {
                    adapter.notifyItemRangeRemoved(
                        0,
                        itemsToCrop
                    )
                }
            }
        } else {
            var itemsToCrop = feedBuffer.size - feedBufferMaxSize
            var cropStartPosition = feedBuffer.size - itemsToCrop
            if (feedBuffer[feedBuffer.size - 1] is RVItemState.Loading) {
                feedBuffer.subList(cropStartPosition, feedBuffer.size - 1).clear()
                uiThreadHandler.post {
                    adapter.notifyItemRangeRemoved(
                        cropStartPosition,
                        itemsToCrop - 1
                    )
                }
            } else {
                feedBuffer.subList(feedBuffer.size - itemsToCrop, feedBuffer.size).clear()
                uiThreadHandler.post {
                    adapter.notifyItemRangeRemoved(
                        cropStartPosition,
                        itemsToCrop
                    )
                }
            }
        }
    }

    private fun synchronizedFetchData(fetchBottom: Boolean) {
        fun toSuccessRVItemStateArray(input: Array<MovieMetadata>): Array<RVItemState> {
            return Array<RVItemState>(input.size) { i -> RVItemState.Success(input[i]) }
        }
        Thread.sleep(1000)
        if (fetchBottom) {
            rangeInsertStart = feedBuffer.size - 1
            val lastEndItemIndex =
                if (feedBuffer.size == 1) feedInitialPosition else (feedBuffer[feedBuffer.size - 2] as RVItemState.Success).movieMetadata.index + 1
            val fetchedData = repository.getRange(
                lastEndItemIndex,
                lastEndItemIndex + numberOfBufferingItems - 1
            )
            setSuccessState(fetchBottom)
            feedBuffer.addAll(toSuccessRVItemStateArray(fetchedData))
            rangeInsertCount = fetchedData.size + 1
        } else {
            val firstItemIndex =
                if (feedBuffer.size == 1) feedInitialPosition else (feedBuffer[1] as RVItemState.Success).movieMetadata.index
            val fetchedData = repository.getRange(
                firstItemIndex - numberOfBufferingItems,
                firstItemIndex - 1
            )
            setSuccessState(fetchBottom)
            feedBuffer.addAll(
                0,
                toSuccessRVItemStateArray(fetchedData).toCollection(ArrayList())
            )
            rangeInsertStart = 0
            rangeInsertCount = fetchedData.size
        }
        uiThreadHandler.post {
            adapter.notifyItemRangeInserted(
                rangeInsertStart!!,
                rangeInsertCount!!
            )
        }
        if (feedBuffer.size > feedBufferMaxSize) {
            cropFeedBuffer(fetchBottom)
        }
        if (fetchBottom) {
            bottomIsFetching = false
        } else {
            topIsFetching = false
        }
    }

    private val fetchingExecutorService: ExecutorService = Executors.newSingleThreadExecutor()
    private fun fetchData(fetchBottom: Boolean) {
        setLoadingState(fetchBottom)
        fetchingExecutorService.execute {
            synchronizedFetchData(fetchBottom)
        }
    }

    fun setLoadingState(fetchedByIndexIncrease: Boolean) {
        if (fetchedByIndexIncrease && (feedBuffer.size == 0 || feedBuffer[feedBuffer.size - 1] !is RVItemState.Loading)) {
            feedBuffer.add(RVItemState.Loading)
            uiThreadHandler.post {
                adapter.notifyItemInserted(feedBuffer.size - 1)
            }
        } else if (!fetchedByIndexIncrease && (feedBuffer.size == 0 || feedBuffer[0] !is RVItemState.Loading)) {
            feedBuffer.add(0, RVItemState.Loading)
            uiThreadHandler.post {
                adapter.notifyItemInserted(0)
            }
        }
    }

    fun setSuccessState(fetchedByIndexIncrease: Boolean) {
        if (fetchedByIndexIncrease && feedBuffer[feedBuffer.size - 1] !is RVItemState.Success) {
            feedBuffer.removeAt(feedBuffer.size - 1)
            uiThreadHandler.post {
                adapter.notifyItemRemoved(feedBuffer.size)
            }
        } else if (!fetchedByIndexIncrease && feedBuffer[0] !is RVItemState.Success) {
            feedBuffer.removeAt(0)
            uiThreadHandler.post {
                adapter.notifyItemRemoved(0)
            }
        }
    }
}
