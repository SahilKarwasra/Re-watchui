package com.example.re_watch.data

sealed class UploadUIEvent{
    data class TitleChanged(val videoTitle: String): UploadUIEvent()
    data class DescriptionChanged(val videoDescription: String): UploadUIEvent()
    data class VideoUriChanged(val videoUriSelect: String): UploadUIEvent()
    data class videoTagsChanged(val videoTagsSelected: String): UploadUIEvent()
    data object UploadButtonClicked : UploadUIEvent()
}
