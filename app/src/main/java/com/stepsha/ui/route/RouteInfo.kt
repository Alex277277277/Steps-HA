package com.stepsha.ui.route

import android.os.Bundle
import androidx.navigation.NavOptions

data class RouteInfo(
    val routeId: Int,
    val params: Bundle = Bundle(),
    val navOptions: NavOptions = NavOptions.Builder().build()
)