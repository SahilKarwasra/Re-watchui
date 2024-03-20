package com.example.re_watch.data

data class UploadUiState(
    var videoUri: String = "",
    var title: String = "",
    var description: String = "",
    var uriError: Boolean = false,
    var titleError: Boolean = false,
    var descriptionError: Boolean = false
)