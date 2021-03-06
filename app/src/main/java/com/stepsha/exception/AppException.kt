package com.stepsha.exception

import com.stepsha.R

//App-provided exceptions
sealed class AppException(
    val errorMessage: Int
) : Throwable()

object NetworkException : AppException(R.string.err_network)
object ServerUnreachableException : AppException(R.string.err_server_unreachable)
data class InternalServerException(val httpCode: Int? = null) : AppException(R.string.err_internal_server)
data class ServerErrorException(val httpCode: Int? = null, val errorResponse: String?) : AppException(R.string.err_internal_server)
object InvalidBoundsException : AppException(R.string.err_bounds)