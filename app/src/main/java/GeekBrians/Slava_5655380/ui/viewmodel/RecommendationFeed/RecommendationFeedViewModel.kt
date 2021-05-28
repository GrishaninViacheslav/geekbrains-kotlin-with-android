package GeekBrians.Slava_5655380.ui.viewmodel.RecommendationFeed

import GeekBrians.Slava_5655380.domain.model.DebugRepository
import GeekBrians.Slava_5655380.domain.MovieMetadata
import GeekBrians.Slava_5655380.domain.model.Repository
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

// TODO: проконтролировать чтобы numberOfBufferingItems, feedBufferMaxSize были
//              в пределах допустимых значений(например numberOfBufferingItems должно быть > 0,
//              от feedBufferMaxSize отнимается numberOfBufferingItems)
class RecommendationFeedViewModel(
    private val feedInitialPosition: Int = 0,
    private val repository: Repository = DebugRepository(),
    private val numberOfBufferingItems: Int = 2,
    private val feedBufferMaxSize: Int = 12 - numberOfBufferingItems,
    private val feedBuffer: ArrayList<RVItemState> = arrayListOf(),
    private val uiThreadHandler: Handler = Handler(Looper.getMainLooper()),
    private val feedState: MutableLiveData<AppState> = MutableLiveData()
) : ViewModel() {
    private val fetchingExecutorService: ExecutorService = Executors.newSingleThreadExecutor()
    private var bottomIsFetching: Boolean = false
    private var topIsFetching: Boolean = false

    @Synchronized
    private fun fetchData(fetchBottom: Boolean) {
        fun fetchItemsToFeedBuffer() {
            fun toSuccessRVItemStateArray(input: Array<MovieMetadata>): Array<RVItemState> {
                return Array<RVItemState>(input.size) { i -> RVItemState.Success(input[i]) }
            }

            fun adapterRangeInsertedNotify(prevFeedBufferSize: Int, fetchedDataSize: Int) {
                var rangeInsertStart: Int = prevFeedBufferSize
                var rangeInsertCount: Int = fetchedDataSize
                if (!fetchBottom) {
                    rangeInsertStart = 0
                    rangeInsertCount = fetchedDataSize
                }
                uiThreadHandler.post {
                    adapter.notifyItemRangeInserted(
                        rangeInsertStart,
                        rangeInsertCount
                    )
                }
            }

            Log.d("[MYLOG]", "fetchData fetchBottom: $fetchBottom")
            var fetchFromIndex: Int =
                if (feedBuffer.size == 1) feedInitialPosition else (feedBuffer[feedBuffer.size - 2] as RVItemState.Success).movieDataItem.index + 1
            var fetchToIndex: Int = fetchFromIndex + numberOfBufferingItems - 1
            if (!fetchBottom) {
                val firstItemIndex =
                    if (feedBuffer.size == 1) feedInitialPosition else (feedBuffer[1] as RVItemState.Success).movieDataItem.index
                fetchFromIndex = firstItemIndex - numberOfBufferingItems
                fetchToIndex = firstItemIndex - 1
            }
            try {
                val fetchedData = repository.getRange(fetchFromIndex, fetchToIndex)
                val successRVItemStateArray = toSuccessRVItemStateArray(fetchedData)
                removeLoadingItem(fetchBottom)
                val prevFeedBufferSize = feedBuffer.size
                if (fetchBottom) {
                    feedBuffer.addAll(successRVItemStateArray)
                } else {
                    feedBuffer.addAll(0, successRVItemStateArray.toCollection(ArrayList()))
                }
                adapterRangeInsertedNotify(prevFeedBufferSize, fetchedData.size)
                feedState.postValue(AppState.Success)
            } catch (e: Throwable) {
                feedState.postValue(AppState.Error(e))
                removeLoadingItem(fetchBottom)
            }
        }

        fetchItemsToFeedBuffer()
        if (feedBuffer.size > feedBufferMaxSize) {
            cropFeedBuffer(fetchBottom)
        }
        if (fetchBottom) {
            bottomIsFetching = false
        } else {
            topIsFetching = false
        }
    }

    private fun cropFeedBuffer(cropBottom: Boolean) {
        var cropFromIndex: Int
        var cropToIndex: Int
        if (cropBottom) {
            if (feedBuffer[0] is RVItemState.Loading) {
                cropFromIndex = 1
                cropToIndex = feedBuffer.size - feedBufferMaxSize
            } else {
                cropFromIndex = 0
                cropToIndex = feedBuffer.size - feedBufferMaxSize
            }
        } else {
            if (feedBuffer[feedBuffer.size - 1] is RVItemState.Loading) {
                cropFromIndex = feedBuffer.size - (feedBuffer.size - feedBufferMaxSize)
                cropToIndex = feedBuffer.size - 1
            } else {
                cropFromIndex = feedBuffer.size - (feedBuffer.size - feedBufferMaxSize)
                cropToIndex = feedBuffer.size
            }
        }

        feedBuffer.subList(cropFromIndex, cropToIndex)
            .forEach {
                uiThreadHandler.post {
                    (it as RVItemState.Success).movieDataItem.trailer.release()
                }
            }
        feedBuffer.subList(cropFromIndex, cropToIndex).clear()
        uiThreadHandler.post {
            adapter.notifyItemRangeRemoved(
                cropFromIndex,
                cropToIndex - cropFromIndex
            )
        }
    }

    private fun addLoadingItem(fetchedByIndexIncrease: Boolean) {
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

    private fun removeLoadingItem(fetchedByIndexIncrease: Boolean) {
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

    var adapter: Adapter = Adapter(this)

    fun feed(feedBottom: Boolean) {
        if (feedBottom && bottomIsFetching || !feedBottom && topIsFetching) {
            return
        }
        if (feedBuffer.size == 1 && feedBuffer[0] is RVItemState.Loading) {
            return
        }
        if (feedBottom) {
            bottomIsFetching = true
        } else {
            topIsFetching = true
        }
        feedState.value = AppState.Loading
        addLoadingItem(feedBottom)
        fetchingExecutorService.execute {
            fetchData(feedBottom)
        }
    }

    fun getItemCount(): Int {
        return feedBuffer.size
    }

    fun getItem(index: Int): RVItemState {
        return feedBuffer[index]
    }

    fun getFeedState() = feedState
}