package dev.loudbook.pastebook.data

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Suppress("unused")
@Document("paste")
class Paste(@Id var id: String?, val title: String, val content: String, val created: Long, val reportBook: Boolean = false, val unlisted: Boolean = false, val wrap: Boolean = false, val creatorIP: String)