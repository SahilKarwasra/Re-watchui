package com.example.re_watch.data


sealed class CommentUIEvent {

    data class UserId(val userId: String ="user"): CommentUIEvent()
    data class CommentChanged(val comment: String): CommentUIEvent()

    data class videoIdChanged(val videoId: String): CommentUIEvent()

    data object CommentButtonClicked : CommentUIEvent()

}