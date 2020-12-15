package com.stepsha.repo

import android.util.Log
import com.stepsha.api.ApiService
import com.stepsha.api.performApiCall
import com.stepsha.db.CommentsDao
import com.stepsha.entity.Bounds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repo @Inject constructor(
    private val apiService: ApiService,
    private val dao: CommentsDao
) {

    suspend fun loadComments(bounds: Bounds, replace: Boolean = false) = withContext(Dispatchers.IO) {
        Log.d("Steps", "Replace comment = $replace")
        val comments = performApiCall {
            apiService.getComments(bounds.startId.toString(), bounds.endId.toString())
        }.body()!!

        if (replace) {
            dao.reloadComments(comments)
        } else {
            dao.insertComments(comments)
        }
    }

    fun getComments() = dao.getComments()

}