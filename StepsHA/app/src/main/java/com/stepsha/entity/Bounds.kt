package com.stepsha.entity

import java.io.Serializable

// Alternatively, we could use Pair<Long, Long> instead of this data class, but data class approach give us more clarity
data class Bounds(val startId: Long, val endId: Long) : Serializable