package com.example.re_watch.data

data class CommentUIState(
    var comment: String = "",
    var userIdError: Boolean = false,
    var commentError: Boolean = false,
    val comments: MutableList<Comment> = mutableListOf()
)