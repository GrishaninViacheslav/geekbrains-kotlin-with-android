package GeekBrians.Slava_5655380.viewmodel

import GeekBrians.Slava_5655380.model.DebugRepository
import GeekBrians.Slava_5655380.model.MovieMetadata
import GeekBrians.Slava_5655380.model.RVItemState
import GeekBrians.Slava_5655380.model.Repository
import GeekBrians.Slava_5655380.utils.notifyingthread.NotifyingThread
import GeekBrians.Slava_5655380.utils.notifyingthread.ThreadCompleteListener
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import java.lang.Exception
import kotlin.collections.ArrayList

class MainViewModel(
    private val repository: Repository = DebugRepository(),
    private val numberOfBufferingItems: Int = 2 // TODO: значение должно быть больше 1
) : ViewModel(), ThreadCompleteListener {
    var adapter: Adapter = Adapter(this)

    // как инициализировать пустой LinkedList?
    var feedBuffer: ArrayList<RVItemState> = arrayListOf()

    init {
        //fetchData(true)
    }

    fun getItemCount(): Int {
        return feedBuffer.size
    }

    fun getItem(index: Int): RVItemState {
        return feedBuffer[index]
    }

    var rangeInsertStart: Int? = null
    var rangeInsertCount: Int? = null

    var isFetching: Boolean = false

    fun fetchData(fetchByIndexIncrease: Boolean) {
        if(isFetching){
            return
        }
        isFetching = true

        // TODO: сделать так, чтобы нельзя было вызвать fetchData,
        // если не завершился поток запущенный ранее вызовом fetchData с таким же значением fetchByIndexIncrease.
        // То есть сделать так чтобы нельзя было повторно запустить fetchData для загрузки ленты снизу,
        // если лента снизу уже и так грузится

        setLoadingState(fetchByIndexIncrease)
        try {
            val thread = object : NotifyingThread() {
                override fun doRun() {
                    fun toSuccessRVItemStateArray(input: Array<MovieMetadata>): Array<RVItemState> {
                        return Array<RVItemState>(input.size) { i -> RVItemState.Success(input[i]) }
                    }

                    Thread.sleep(1000)
                    if (fetchByIndexIncrease) {
                        rangeInsertStart = feedBuffer.size - 1


//                        val lastEndItemIndex =
//                            if (feedBuffer.size == 0) 0 else feedBuffer[feedBuffer.size - 1].index + 1

                        val lastEndItemIndex =
                            if (feedBuffer.size == 1) 0 else (feedBuffer[feedBuffer.size - 2] as RVItemState.Success).movieMetadata.index + 1


                        val fetchedData = repository.getRange(
                            lastEndItemIndex,
                            lastEndItemIndex + numberOfBufferingItems - 1
                        )
                        setSuccessState(fetchByIndexIncrease)
                        feedBuffer.addAll(toSuccessRVItemStateArray(fetchedData))
                        rangeInsertCount = fetchedData.size
                    } else {
                        val firstItemIndex =
                            if (feedBuffer.size == 1) 0 else (feedBuffer[0] as RVItemState.Success).movieMetadata.index
                        val fetchedData = repository.getRange(
                            firstItemIndex - numberOfBufferingItems,
                            firstItemIndex
                        )
                        setSuccessState(fetchByIndexIncrease)
                        feedBuffer.addAll(
                            0,
                            toSuccessRVItemStateArray(fetchedData).toCollection(ArrayList())
                        )
                    }
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
            adapter.notifyItemInserted(feedBuffer.size - 1)
        } else if (!fetchedByIndexIncrease && feedBuffer[0] !is RVItemState.Loading) {
            feedBuffer.add(0, RVItemState.Loading)
            adapter.notifyItemInserted(0)
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
            adapter.notifyItemRemoved(0)
        }
    }

    fun setErrorState(e: Exception) {
    }

    override fun notifyOfThreadComplete(thread: Thread?) {
        // Если не прибавлять число к rangeInsertCount,
        // то почему-то последний item RecyclerView будет дублировать item который был последнем при первом fetchData

        ThreadUtil.runOnUiThread {
            adapter.notifyItemRangeInserted(
                rangeInsertStart!!,
                rangeInsertCount!! + 1
            )
        }

        isFetching = false
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