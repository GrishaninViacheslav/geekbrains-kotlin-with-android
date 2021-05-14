package GeekBrians.Slava_5655380.viewmodel

import GeekBrians.Slava_5655380.model.DebugRepository
import GeekBrians.Slava_5655380.model.MovieMetadata
import GeekBrians.Slava_5655380.model.Repository
import GeekBrians.Slava_5655380.utils.notifyingthread.NotifyingThread
import GeekBrians.Slava_5655380.utils.notifyingthread.ThreadCompleteListener
import android.util.Log
import androidx.lifecycle.ViewModel
import java.lang.Exception
import kotlin.collections.ArrayList

class MainViewModel(
    private val repository: Repository = DebugRepository(),
    private val numberOfBufferingItems: Int = 2 // TODO: значение должно быть больше 1
) : ViewModel(), ThreadCompleteListener {
    var adapter: Adapter = Adapter(this)

    // как инициализировать пустой LinkedList?
    var feedBuffer: ArrayList<MovieMetadata> = arrayListOf()

    init {
        //fetchData(true)
    }

    fun getItemCount(): Int {
        return feedBuffer.size
    }

    fun getItem(index: Int): MovieMetadata {
        return feedBuffer[index]
    }

    var rangeInsertStart: Int? = null
    var rangeInsertCount: Int? = null

    fun fetchData(fetchByIndexIncrease: Boolean) {
        setLoadingState(fetchByIndexIncrease)
        try {
            val thread = object : NotifyingThread(){
                override fun doRun() {
                    Thread.sleep(1000)
                    if (fetchByIndexIncrease) {
                        rangeInsertStart = feedBuffer.size - 1
                        val lastEndItemIndex =
                            if (feedBuffer.size == 0) 0 else feedBuffer[feedBuffer.size - 1].index + 1
                        val fethedData = repository.getRange(
                            lastEndItemIndex,
                            lastEndItemIndex + numberOfBufferingItems - 1
                        )
                        feedBuffer.addAll(fethedData)
                        rangeInsertCount = fethedData.size
                    } else {
                        val firstItemIndex =
                            if (feedBuffer.size == 0) 0 else feedBuffer[0].index
                        val fethedData = repository.getRange(
                            firstItemIndex - numberOfBufferingItems,
                            firstItemIndex
                        )
                        feedBuffer.addAll(0, fethedData.toCollection(ArrayList()))
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
        Log.d("[FetchState]", "Loading")
        // добавление loading item
    }

    fun setSuccessState() {
        Log.d("[FetchState]", "Success")
        // удаление loading item
    }

    fun setErrorState(e: Exception) {
        Log.d("[FetchState]", "Error")
    }

    override fun notifyOfThreadComplete(thread: Thread?) {
        // TODO: исправить баг приводящий к ошибке: java.lang.IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling androidx.recyclerview.widget.RecyclerView
        adapter.notifyItemRangeInserted(rangeInsertStart!!, rangeInsertCount!!)
        setSuccessState()
    }

}