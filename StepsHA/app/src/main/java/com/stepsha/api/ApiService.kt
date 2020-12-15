package com.stepsha.api

import com.stepsha.entity.Comment
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Note:
    //  JSONPlaceholder doc https://jsonplaceholder.typicode.com/guide/ doesn't mention possibility to sort the result set and filter it by range of values.
    //  On the other hand, this service is based on JSON-server, which DOES support ordering and filtering by range.
    //  The meaning of query parameters:
    //      id_gte - all entries with the "id" field greater than or equal startCommentId
    //      id_lte - all entries with the "id" lower than or equal endCommentId
    //      _sort - sort by sortField field (which is the "id" by default)
    //      _order - order (ascending by default)
    //  See more details here https://github.com/typicode/json-server
    @GET("comments")
    suspend fun getComments(
        @Query("id_gte") startCommentId: String,
        @Query("id_lte") endCommentId: String,
        @Query("_sort") sortField: String = "id",
        @Query("_order") order: String = "asc"
    ): Response<List<Comment>>

    companion object {
        const val TAG = "Steps-ApiService"
    }

}