package com.moengage.sampleapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Article(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String,
    var isBookmarked: Boolean = false
)

fun Article.toMap(): Map<String, Any?> {
    return mapOf(
        "source" to source.name,
        "author" to author,
        "title" to title,
        "url" to url
    )
}