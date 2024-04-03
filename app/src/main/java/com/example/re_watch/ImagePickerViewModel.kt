package com.example.re_watch

import android.content.Context
import androidx.lifecycle.ViewModel

class ImagePickerViewModel : ViewModel() {
    private val TAG = ImagePickerViewModel::class.simpleName

    private lateinit var context: Context


    fun imagePickerContext(context: Context) {
        this.context = context
    }

}
