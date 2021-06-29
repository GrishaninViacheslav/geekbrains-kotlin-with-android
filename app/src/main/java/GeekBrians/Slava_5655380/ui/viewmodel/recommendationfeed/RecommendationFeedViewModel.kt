package GeekBrians.Slava_5655380.ui.viewmodel.recommendationfeed

import GeekBrians.Slava_5655380.domain.MovieMetadata
import GeekBrians.Slava_5655380.domain.model.Repository
import GeekBrains.Slava_5655380.domain.model.repositoryimpl.RepositoryImpl
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

// TODO: проконтролировать чтобы numberOfBufferingItems, feedBufferMaxSize были
//              в пределах допустимых значений(например numberOfBufferingItems должно быть > 0,
//              от feedBufferMaxSize отнимается numberOfBufferingItems)
// TODO: проконтролировать чтобы значения feedBuffer нельзя было изменить
//              за пределами RecommendationFeedViewModel
class RecommendationFeedViewModel(
    val feedBuffer: ArrayList<RVItemState> = arrayListOf(),
    private val feedInitialPosition: Int = 0,
    private val repository: Repository = RepositoryImpl(),
    private val numberOfBufferingItems: Int = 2,
    private val feedBufferMaxSize: Int = 12 - numberOfBufferingItems,
    private val uiThreadHandler: Handler = Handler(Looper.getMainLooper()),
    private val feedState: MutableLiveData<AppState> = MutableLiveData()
) : ViewModel() {
    private val fetchingExecutorService: ExecutorService = Executors.newSingleThreadExecutor()
    private var bottomIsFetching: Boolean = false
    private var topIsFetching: Boolean = false

    private val TAG = "[RFVM]"

    @Synchronized
    private fun fetchData(fetchBottom: Boolean) {
        fun fetchItemsToFeedBuffer() {
            fun toSuccessRVItemStateArray(input: List<MovieMetadata>): Array<RVItemState> {
                return Array<RVItemState>(input.size) { i -> RVItemState.Success(input[i]) }
            }
            cdl.await()
            var fetchFromIndex: Int =
                if (feedBuffer.size == 1) feedInitialPosition else (feedBuffer[feedBuffer.size - 2] as RVItemState.Success).movieDataItem.metadata.index + 1
            var fetchToIndex: Int = fetchFromIndex + numberOfBufferingItems - 1
            if (!fetchBottom) {
                val firstItemIndex =
                    if (feedBuffer.size == 1) feedInitialPosition else (feedBuffer[1] as RVItemState.Success).movieDataItem.metadata.index
                fetchFromIndex = firstItemIndex - numberOfBufferingItems
                fetchToIndex = firstItemIndex - 1
            }
            try {
                l("fetchFromIndex: $fetchFromIndex, fetchToIndex: $fetchToIndex")
                val fetchedData = repository.getRange(fetchFromIndex, fetchToIndex)
                val successRVItemStateArray = toSuccessRVItemStateArray(fetchedData)
                logRVItemStateArray(successRVItemStateArray, "successRVItemStateArray")
                val cdl2 = CountDownLatch(1)
                uiThreadHandler.post {
                    if (fetchBottom && feedBuffer[feedBuffer.size - 1] !is RVItemState.Success) {
                        feedBuffer.removeAt(feedBuffer.size - 1)
                        adapter.notifyItemRemoved(feedBuffer.size)

                    } else if (!fetchBottom && feedBuffer[0] !is RVItemState.Success) {
                        feedBuffer.removeAt(0)
                        adapter.notifyItemRemoved(0)
                    }

                    if (fetchBottom) {
                        feedBuffer.addAll(successRVItemStateArray)
                    } else {
                        feedBuffer.addAll(0, successRVItemStateArray.toCollection(ArrayList()))
                    }
                    l("positionStart: ${if (fetchBottom) feedBuffer.size - fetchedData.size else 0}, itemCount: ${fetchedData.size}")
                    adapter.notifyItemRangeInserted(
                        if (fetchBottom) feedBuffer.size - fetchedData.size else 0,
                        fetchedData.size
                    )
                    logRVItemStateArray(feedBuffer, "feedBuffer")
                    cdl2.countDown()
                }
                cdl2.await()
                feedState.postValue(AppState.Success)
            } catch (e: Throwable) {
                feedState.postValue(AppState.Error(e))
                removeLoadingItem(fetchBottom)
            }
        }

        l("fetchData fetchBottom: $fetchBottom")
        fetchItemsToFeedBuffer()
        if (feedBuffer.size > feedBufferMaxSize) {
            // TODO: возможно стоит подождать завершения cropFeedBuffer
            cropFeedBuffer(fetchBottom)
        }
        if (fetchBottom) {
            bottomIsFetching = false
        } else {
            topIsFetching = false
        }
    }

    private fun cropFeedBuffer(cropBottom: Boolean) {
        l("cropFeedBuffer cropBottom: $cropBottom")
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

        uiThreadHandler.post {
            l("cropFromIndex: $cropFromIndex, cropToIndex: $cropToIndex")
            feedBuffer.subList(cropFromIndex, cropToIndex)
                .forEach {
                    // TODO: разобраться почему здесь может быть RVItemState.Loading
                    if (it is RVItemState.Success) {
                        (it as RVItemState.Success).movieDataItem.trailer.release()
                    }
                }
            feedBuffer.subList(cropFromIndex, cropToIndex).clear()
            l("positionStart: $cropFromIndex, itemCount: ${cropToIndex - cropFromIndex}")
            adapter.notifyItemRangeRemoved(
                cropFromIndex,
                cropToIndex - cropFromIndex
            )
            logRVItemStateArray(feedBuffer, "croppedFeedBuffer")
        }
    }

    private var cdl: CountDownLatch = CountDownLatch(1)
    private fun addLoadingItem(fetchedByIndexIncrease: Boolean) {
        if (fetchedByIndexIncrease && (feedBuffer.size == 0 || feedBuffer[feedBuffer.size - 1] !is RVItemState.Loading)) {
            cdl = CountDownLatch(1)
            uiThreadHandler.post {
                feedBuffer.add(RVItemState.Loading)
                adapter.notifyItemInserted(feedBuffer.size - 1)
                cdl.countDown()
            }
        } else if (!fetchedByIndexIncrease && (feedBuffer.size == 0 || feedBuffer[0] !is RVItemState.Loading)) {
            cdl = CountDownLatch(1)
            uiThreadHandler.post {
                feedBuffer.add(0, RVItemState.Loading)
                adapter.notifyItemInserted(0)
                cdl.countDown()
            }
        }
    }

    private fun removeLoadingItem(fetchedByIndexIncrease: Boolean) {
        if (fetchedByIndexIncrease && feedBuffer[feedBuffer.size - 1] !is RVItemState.Success) {
            uiThreadHandler.post {
                feedBuffer.removeAt(feedBuffer.size - 1)
                adapter.notifyItemRemoved(feedBuffer.size)
            }
        } else if (!fetchedByIndexIncrease && feedBuffer[0] !is RVItemState.Success) {
            uiThreadHandler.post {
                feedBuffer.removeAt(0)
                adapter.notifyItemRemoved(0)
            }
        }
    }


    // TODO: как перегрузить эти функции чтобы измежать дублирования?
    private fun logRVItemStateArray(arr: ArrayList<RVItemState>, name: String) {
        var msg = "$name[ "
        for (el in arr) {
            msg += when (el) {
                is RVItemState.Success -> "${el.movieDataItem.metadata.index} "
                RVItemState.Loading -> " L "
            }
        }
        msg += "]"
        l(msg)
    }

    private fun logRVItemStateArray(arr: Array<RVItemState>, name: String) {
        var msg = "$name[ "
        for (el in arr) {
            msg += when (el) {
                is RVItemState.Success -> "${el.movieDataItem.metadata.index} "
                RVItemState.Loading -> " L "
            }
        }
        msg += "]"
        l(msg)
    }

    private fun l(msg: String) {
        Log.d(TAG, msg)
    }


    var adapter: Adapter = Adapter(feedBuffer)

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

    fun getFeedState() = feedState
}