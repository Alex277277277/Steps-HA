package com.stepsha.ui

import android.app.Application
import androidx.core.os.bundleOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.hadilq.liveevent.LiveEvent
import com.stepsha.R
import com.stepsha.entity.Bounds
import com.stepsha.ui.CommentsActivity.Companion.ARG_BOUNDS
import com.stepsha.ui.route.RouteInfo

class SetBoundsVM(val app: Application) : AndroidViewModel(app) {

    private val routerLD = LiveEvent<RouteInfo>()
    fun router() = routerLD as LiveData<RouteInfo>

    private val errorLD = LiveEvent<String>()
    fun error() = errorLD as LiveData<String>

    fun setBounds(startComment: String, endComment: String) {
        try {
            val bounds = Bounds(startComment.toLong(), endComment.toLong())
            validateBounds(bounds)
            routerLD.value = RouteInfo(R.id.commentsFragment, bundleOf(ARG_BOUNDS to bounds))
        } catch (e: Throwable) {
            errorLD.value = app.getString(R.string.err_bounds)
        }
    }

    private fun validateBounds(bounds: Bounds) {
        assert(bounds.startId > 0)
        assert(bounds.startId <= bounds.endId)
    }

}