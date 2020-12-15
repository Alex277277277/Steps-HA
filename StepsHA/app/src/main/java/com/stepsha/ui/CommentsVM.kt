package com.stepsha.ui

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.stepsha.App
import com.stepsha.entity.Bounds
import com.stepsha.entity.Comment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

class CommentsVM(val app: Application) : AndroidViewModel(app) {

    private val repo = App.appGraph.getRepo()

    private var initialized = false
    private lateinit var bounds: Bounds

    private val loadingLD = MutableLiveData<Boolean>().also { it.value = false }
    fun loading() = loadingLD as LiveData<Boolean>

    private val initialLoadingStateLD = MutableLiveData<Boolean>().also { it.value = true }
    fun initialLoadingState() = initialLoadingStateLD as LiveData<Boolean>

    private val boundaryCallback = object: PagedList.BoundaryCallback<Comment>() {

        override fun onItemAtEndLoaded(itemAtEnd: Comment) {
            // Here we fetch the next chunk of items from the network (server)
            // This happens when we run out of data from cache (db)
            Log.d("Steps", "onItemAtEndLoaded ${itemAtEnd.id}")
            val bounds = calculateNextBounds(itemAtEnd.id) ?: return
            loadComments(bounds)
        }
    }

    val comments = repo.getComments().toLiveData(
        pageSize = 10,
        boundaryCallback = boundaryCallback
    )

    fun initialize(args: Bundle?) {
        if (initialized) return
        bounds = (args?.getSerializable(CommentsActivity.ARG_BOUNDS)) as Bounds? ?: throw IllegalStateException()
        initialized = true
    }

    fun loadInitialComments() {
        val lastId = bounds.startId - 1
        val bounds = calculateNextBounds(lastId) ?: return
        loadComments(bounds, true, MIN_LOADING_DELAY)
    }

    private fun calculateNextBounds(lastId: Long): Bounds? {
        Log.d("Steps", "calculateNextBounds $lastId")
        val upperBoundLimit = bounds.endId

        val lowerBound = lastId + 1
        if (lowerBound > upperBoundLimit) return null

        var upperBound = lastId + CHUNK_SIZE
        if (upperBound > upperBoundLimit) {
            upperBound = upperBoundLimit
        }

        Log.d("Steps", "calculateNextBounds done ${Bounds(lastId + 1, upperBound)}")
        return Bounds(lastId + 1, upperBound)
    }

    private fun loadComments(bounds: Bounds, replace: Boolean = false, minDelay: Long = 0L) {
        Log.d("Steps", "Replace comment - 1 = $replace")
        viewModelScope.launch {
            loadingLD.value = true
            try {
                val startTime = System.currentTimeMillis()
                repo.loadComments(bounds, replace)
                val delta = System.currentTimeMillis() - startTime - minDelay
                if (delta > 0) {
                    delay(delta)
                }
                initialLoadingStateLD.value = false
            } catch (e: Throwable) {
                Log.d("Steps", "Loading initial comments - error $e")
                // TODO handle errors
            }
            loadingLD.value = false
        }
    }

    companion object {
        const val CHUNK_SIZE = 10L
        const val MIN_LOADING_DELAY = 3 * 1000L   // minimum loading delay of 5 seconds
    }

}