package com.stepsha.ui

import android.app.Application
import androidx.core.os.bundleOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.stepsha.App
import com.stepsha.R
import com.stepsha.entity.Bounds
import com.stepsha.exception.AppException
import com.stepsha.exception.InvalidBoundsException
import com.stepsha.ui.CommentsActivity.Companion.ARG_BOUNDS
import com.stepsha.ui.route.RouteInfo
import kotlinx.coroutines.launch

class SetBoundsVM(val app: Application) : AndroidViewModel(app) {

    private val repo = App.appGraph.getRepo()

    private val routerLD = LiveEvent<RouteInfo>()
    fun router() = routerLD as LiveData<RouteInfo>

    private val errorLD = LiveEvent<String>()
    fun error() = errorLD as LiveData<String>

    // Accepts (or declines) the bounds entered by user
    fun setBounds(startComment: String, endComment: String) {
        try {
            val bounds = Bounds(startComment.toLong(), endComment.toLong())
            validateBounds(bounds)
            viewModelScope.launch { repo.clearComments() }
            routerLD.value = RouteInfo(R.id.commentsFragment, bundleOf(ARG_BOUNDS to bounds))
        } catch (e: Throwable) {
            if (e is AppException) {
                errorLD.value = app.getString(e.errorMessage)
            }
        }
    }

    // Validates bounds entered by user
    private fun validateBounds(bounds: Bounds) {
        if (bounds.startId <= 0L || bounds.startId > bounds.endId) {
            throw InvalidBoundsException
        }
    }

}