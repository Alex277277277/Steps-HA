package com.stepsha.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

// SerializedName and ColumnInfo could be omitted here since it currently coincides with the JSON field names.
// This is just an additional layer of protection which prevents app breaking for cases when we refactor the names in data entity.
@Entity(tableName = "Comments")
data class Comment(

    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,

    @SerializedName("name")
    @ColumnInfo(name = "name")
    val name: String,

    @SerializedName("email")
    @ColumnInfo(name = "email")
    val email: String,

    @SerializedName("body")
    @ColumnInfo(name = "body")
    val body: String
)