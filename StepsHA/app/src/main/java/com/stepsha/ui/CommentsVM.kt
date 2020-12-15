package com.stepsha.ui

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.hadilq.liveevent.LiveEvent
import com.stepsha.App
import com.stepsha.entity.Bounds
import com.stepsha.entity.Comment
import com.stepsha.exception.AppException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

class CommentsVM(val app: Application) : AndroidViewModel(app) {

    enum class LoadingState {
        INITIALIZED,
        INITIAL_LOADING,
        LOADING,
        IDLE
    }

    private val repo = App.appGraph.getRepo()

    private var initialized = false
    private lateinit var bounds: Bounds
    private var loadingJob: Job? = null

    private val loadingStateLD = MutableLiveData<LoadingState>().also { it.value = LoadingState.INITIALIZED }
    fun loadingState() = loadingStateLD as LiveData<LoadingState>

    private val errorLD = LiveEvent<String>()
    fun error() = errorLD as LiveData<String>

    private val boundaryCallback = object: PagedList.BoundaryCallback<Comment>() {

        override fun onItemAtEndLoaded(itemAtEnd: Comment) {
            // Here we fetch the next chunk of items from the network (server)
            // This happens when we run out of data from cache (db)
            val bounds = calculateNextBounds(itemAtEnd.id) ?: return
            loadComments(bounds)
        }
    }

    // LiveData for observing the cache (db) changes
    val comments = repo.getComments().toLiveData(
        pageSize = 10,
        boundaryCallback = boundaryCallback
    )

    fun initialize(args: Bundle?) {
        if (initialized) return
        bounds = (args?.getSerializable(CommentsActivity.ARG_BOUNDS)) as Bounds? ?: throw IllegalStateException()
        initialized = true
    }

    // Loads initial chunk of comments
    fun loadInitialComments() {
        val bounds = calculateNextBounds(bounds.startId - 1) ?: return
        loadComments(bounds, true, MIN_LOADING_DELAY)
    }

    // Calculates the next chunk of comments to be loaded
    private fun calculateNextBounds(lastId: Long): Bounds? {
        val lowerBound = lastId + 1
        if (lowerBound > bounds.endId) return null

        var upperBound = lastId + PAGE_SIZE
        if (upperBound > bounds.endId) {
            upperBound = bounds.endId
        }

        return Bounds(lowerBound, upperBound)
    }

    // Loads a chunk of comments from the network (server) into the cache (db)
    private fun loadComments(bounds: Bounds, replace: Boolean = false, minDelay: Long = 0L) {
        loadingJob = viewModelScope.launch {
            loadingStateLD.value = if (loadingStateLD.value == LoadingState.INITIALIZED) LoadingState.INITIAL_LOADING else LoadingState.LOADING
            try {
                repo.loadComments(bounds, replace, minDelay)
                loadingStateLD.value = LoadingState.IDLE
            } catch (e: Throwable) {
                loadingStateLD.value = if (loadingStateLD.value == LoadingState.INITIAL_LOADING) LoadingState.INITIALIZED else LoadingState.IDLE
                if (e is AppException) {
                    errorLD.value = app.getString(e.errorMessage)
                }
            }
            loadingJob = null
        }
    }

    // Cancels initial loading if it's currently in progress
    fun cancelLoading() {
        if (loadingStateLD.value == LoadingState.INITIAL_LOADING) {
            loadingJob?.cancel()
        }
    }

    companion object {
        const val PAGE_SIZE = 10L
        const val MIN_LOADING_DELAY = 3 * 1000L   // minimum loading delay of 3 seconds
    }

}