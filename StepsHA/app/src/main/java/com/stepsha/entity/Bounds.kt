package com.stepsha.entity

import java.io.Serializable

// Class that holds lower and upper bounds for loading comments
// Alternatively, we could use Pair<Long, Long> instead of this data class, but data class approach give us more clarity
data class Bounds(val startId: Long, val endId: Long) : Serializable