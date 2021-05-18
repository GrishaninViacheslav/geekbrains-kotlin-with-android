package GeekBrians.Slava_5655380.viewmodel

import GeekBrians.Slava_5655380.model.DebugRepository
import GeekBrians.Slava_5655380.model.MovieMetadata
import GeekBrians.Slava_5655380.model.RVItemState
import GeekBrians.Slava_5655380.model.Repository
import GeekBrians.Slava_5655380.utils.notifyingthread.NotifyingThread
import GeekBrians.Slava_5655380.utils.notifyingthread.ThreadCompleteListener
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import java.lang.Exception
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class MainViewModel(
    private val repository: Repository = DebugRepository(),
    private val numberOfBufferingItems: Int = 2, // TODO: значение должно быть больше 1
    // TODO: исправить баг из за которого первое заполнение экрана
    //       лентой работает только при numberOfBufferingItems = 2
    private val feedBufferMaxSize: Int = 5
) : ViewModel(), ThreadCompleteListener {
    var adapter: Adapter = Adapter(this)

    // как инициализировать пустой LinkedList?
    var feedBuffer: ArrayList<RVItemState> = arrayListOf()

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
        // TODO: реализовать feedBottom == false

        if (feedBottom && bottomIsFetching || !feedBottom && topIsFetching) {
            return
        }
        if (feedBottom) {
            bottomIsFetching = true
        } else {
            topIsFetching = true
        }


        // TODO: сделать так, чтобы нельзя было вызвать fetchData,
        // если не завершился поток запущенный ранее вызовом fetchData с таким же значением fetchByIndexIncrease.
        // То есть сделать так чтобы нельзя было повторно запустить fetchData для загрузки ленты снизу,
        // если лента снизу уже и так грузится
        fetchData(feedBottom)
    }

    private fun cropFeedBuffer(cropBottom: Boolean) {
        if (cropBottom) {
            val itemsToCrop = feedBuffer.size - feedBufferMaxSize
            feedBuffer.subList(0, itemsToCrop).clear()
            ThreadUtil.runOnUiThread {
                adapter.notifyItemRangeRemoved(
                    0,
                    itemsToCrop
                )
            }
        } else {
            val itemsToCrop = feedBuffer.size - feedBufferMaxSize
            val cropStartPosition = feedBuffer.size - itemsToCrop
            feedBuffer.subList(feedBuffer.size - itemsToCrop, feedBuffer.size).clear()
            ThreadUtil.runOnUiThread {
                adapter.notifyItemRangeRemoved(
                    cropStartPosition,
                    itemsToCrop
                )
            }
        }
    }

    @Synchronized
    private fun synchronizedFetchData(fetchBottom: Boolean){
        fun toSuccessRVItemStateArray(input: Array<MovieMetadata>): Array<RVItemState> {
            return Array<RVItemState>(input.size) { i -> RVItemState.Success(input[i]) }
        }
        Thread.sleep(1000)
        if (fetchBottom) {
            rangeInsertStart = feedBuffer.size - 1
            val lastEndItemIndex =
                if (feedBuffer.size == 1) 0 else (feedBuffer[feedBuffer.size - 2] as RVItemState.Success).movieMetadata.index + 1
            val fetchedData = repository.getRange(
                lastEndItemIndex,
                lastEndItemIndex + numberOfBufferingItems - 1
            )
            setSuccessState(fetchBottom)
            feedBuffer.addAll(toSuccessRVItemStateArray(fetchedData))
            rangeInsertCount = fetchedData.size + 1
        } else {
            val firstItemIndex =
                if (feedBuffer.size == 1) 0 else (feedBuffer[1] as RVItemState.Success).movieMetadata.index
            val fetchedData = repository.getRange(
                firstItemIndex - numberOfBufferingItems,
                firstItemIndex - 1
            )
            setSuccessState(fetchBottom)
            Log.d("[MYLOG]", "fetchedData[")
            for(el in fetchedData){
                Log.d("[MYLOG]", "${el.index}")
            }
            Log.d("[MYLOG]", "]")
            feedBuffer.addAll(
                0,
                toSuccessRVItemStateArray(fetchedData).toCollection(ArrayList())
            )
            rangeInsertStart = 0
            rangeInsertCount = fetchedData.size
        }

        Log.d("[MYLOG]", "feedBuffer[")
        for(el in feedBuffer){
            when(el){
                is RVItemState.Success ->  Log.d("[MYLOG]", "${el.movieMetadata.index}")
                is RVItemState.Loading -> Log.d("[MYLOG]", "Loading")
            }
        }
        Log.d("[MYLOG]", "]")

        ThreadUtil.runOnUiThread {
            adapter.notifyItemRangeInserted(
                rangeInsertStart!!,
                rangeInsertCount!!
            )
        }
        if (fetchBottom) {
            bottomIsFetching = false
        } else {
            topIsFetching = false
        }
    }

    private val fetchingExecutorService: ExecutorService = Executors.newSingleThreadExecutor()
    private val FETCHING_THREAD_CLASS_CODE = "FETCHING_THREAD"
    private val FETCH_BOTTOM_VALUE_KEY = "FETCH_BOTTOM_VALUE"
    private fun fetchData(fetchBottom: Boolean) {
        // TODO: реализовать возможность задать начальную позицию ленты,
        //       сейчас норамально работает только начальная позиция равная 0
        setLoadingState(fetchBottom)

        // TODO: не запускать поток до тех пор пока не завершиться запущенный ранее
        try {
            val bundle: Bundle = Bundle()
            bundle.putBoolean(FETCH_BOTTOM_VALUE_KEY, fetchBottom)
            val thread = object : NotifyingThread(FETCHING_THREAD_CLASS_CODE, bundle) {
                override fun doRun() {
                    synchronizedFetchData(fetchBottom)
                }
            }
            thread.addListener(this)
            thread.start()
        } catch (e: Exception) {
            setErrorState(e)
        }
    }

    fun setLoadingState(fetchedByIndexIncrease: Boolean) {
        if (fetchedByIndexIncrease && (feedBuffer.size == 0 || feedBuffer[feedBuffer.size - 1] !is RVItemState.Loading)) {
            feedBuffer.add(RVItemState.Loading)
            ThreadUtil.runOnUiThread {
                adapter.notifyItemInserted(feedBuffer.size - 1)
            }
        } else if (!fetchedByIndexIncrease && (feedBuffer.size == 0 || feedBuffer[0] !is RVItemState.Loading)) {
            feedBuffer.add(0, RVItemState.Loading)
            ThreadUtil.runOnUiThread {
                adapter.notifyItemInserted(0)
            }
        }
    }

    fun setSuccessState(fetchedByIndexIncrease: Boolean) {
        if (fetchedByIndexIncrease && feedBuffer[feedBuffer.size - 1] !is RVItemState.Success) {
            feedBuffer.removeAt(feedBuffer.size - 1)
            ThreadUtil.runOnUiThread {
                adapter.notifyItemRemoved(feedBuffer.size) // TODO: разобраться почему нужно передать eedBuffer.size, а не feedBuffer.size - 1
            }
            // TODO: notify вынести в runOnUiThread
        } else if (!fetchedByIndexIncrease && feedBuffer[0] !is RVItemState.Success) {
            feedBuffer.removeAt(0)
            ThreadUtil.runOnUiThread {
                adapter.notifyItemRemoved(0)
            }
        }
    }

    fun setErrorState(e: Exception) {
    }

    override fun notifyOfThreadComplete(thread: NotifyingThread?) {
        // TODO: реализовать crop, так чтобы если выполнение fetch() приведёт к тому
        //       что размер буффера станет больше feedBufferMAxSize, то новый fetch(),
        //       завершает активный fetch(), производится crop для освобождения места
        //       для новых item и выполняется новый feed()

//        // Если не прибавлять число к rangeInsertCount,
//        // то почему-то последний item RecyclerView будет дублировать item который был последнем при первом fetchData
//        if (thread == null || thread.CLASS_CODE != FETCHING_THREAD_CLASS_CODE) {
//            return
//        }
//
//        // TODO: завершить все потоки запущенные fetch()
//        Log.d("[MYLOG]", "feedBuffer[")
//        for(el in feedBuffer){
//            Log.d("[MYLOG]", "${(el as RVItemState.Success).movieMetadata.index}")
//        }
//        Log.d("[MYLOG]", "]")
//
//        if (feedBuffer.size > feedBufferMaxSize) {
//            cropFeedBuffer(thread.bundle.getBoolean(FETCH_BOTTOM_VALUE_KEY))
//        }
    }

}

object ThreadUtil {
    private val handler = Handler(Looper.getMainLooper())

    fun runOnUiThread(action: () -> Unit) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            handler.post(action)
        } else {
            action.invoke()
        }
    }
}