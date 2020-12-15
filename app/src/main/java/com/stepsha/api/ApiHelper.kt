package com.stepsha.api

import com.stepsha.App
import com.stepsha.Utils
import com.stepsha.exception.*
import retrofit2.Response
import java.io.IOException

// Helper class for calling network requests and handling some general errors
suspend fun <T : Any> performApiCall(call: suspend () -> Response<T>): Response<T> {
    try {
        val response = call.invoke()
        if (!response.isSuccessful) {
            if (response.code().toString().startsWith("5")) {
                throw InternalServerException(response.code())
            } else {
                throw ServerErrorException(response.code(), response.errorBody()?.string())
            }
        }
        return response
    } catch (e: Exception) {
        if (e is AppException) {
            throw e
        }
        if (e is IOException) {
            if (Utils.isInternetAvailable(App.appGraph.getAppContext())) {
                throw ServerUnreachableException
            } else {
                throw NetworkException
            }
        } else {
            throw InternalServerException()
        }
    }
}