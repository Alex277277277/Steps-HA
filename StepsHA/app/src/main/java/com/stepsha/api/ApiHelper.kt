package com.stepsha.api

import android.util.Log
import com.stepsha.App
import com.stepsha.Utils
import com.stepsha.exception.*
import retrofit2.Response
import java.io.IOException

suspend fun <T : Any> performApiCall(call: suspend () -> Response<T>): Response<T> {
    try {
        val response = call.invoke()
        if (!response.isSuccessful) {
            Log.d(ApiService.TAG, "Api request failed, response code = ${response.code()} message =  ${response.message()}")
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
            Log.d(ApiService.TAG, "Api request failed with i/o exception $e")
            if (Utils.isInternetAvailable(App.appGraph.getAppContext())) {
                throw ServerUnreachableException
            } else {
                throw NetworkException
            }
        } else {
            Log.d(ApiService.TAG, "Api request failed exception $e")
            throw InternalServerException()
        }
    }
}