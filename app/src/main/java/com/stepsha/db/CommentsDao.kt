package com.stepsha.db

import androidx.paging.DataSource
import androidx.room.*
import com.stepsha.entity.Comment

@Dao
interface CommentsDao {

    @Query("SELECT * FROM Comments ORDER BY id ASC")
    fun getComments(): DataSource.Factory<Int, Comment>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertComments(comments: List<Comment>)

    @Query("DELETE FROM Comments")
    fun clearComments()

    @Transaction
    fun reloadComments(comments: List<Comment>) {
        clearComments()
        insertComments(comments)
    }

}