package com.stepsha.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.stepsha.entity.Comment

@Database(entities = [Comment::class], version = 1)
abstract class AppDb : RoomDatabase() {

    abstract fun commentsDao(): CommentsDao

}