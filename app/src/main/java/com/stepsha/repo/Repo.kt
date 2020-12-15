package com.stepsha.repo

import com.stepsha.api.ApiService
import com.stepsha.api.performApiCall
import com.stepsha.db.CommentsDao
import com.stepsha.entity.Bounds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repo @Inject constructor(
    private val apiService: ApiService,
    private val dao: CommentsDao
) {

    suspend fun loadComments(bounds: Bounds, replace: Boolean = false, minDelay: Long = 0L) = withContext(Dispatchers.IO) {
        val startTime = System.currentTimeMillis()
        val comments = performApiCall {
            apiService.getComments(bounds.startId.toString(), bounds.endId.toString())
        }.body()!!

        // wait for the minimum amount of time even if server responds faster
        val delta = minDelay - (System.currentTimeMillis() - startTime)
        if (delta > 0) {
            delay(delta)
        }

        if (replace) {
            dao.reloadComments(comments)
        } else {
            dao.insertComments(comments)
        }
    }

    fun getComments() = dao.getComments()

    suspend fun clearComments() = withContext(Dispatchers.IO) {
        dao.clearComments()
    }

}