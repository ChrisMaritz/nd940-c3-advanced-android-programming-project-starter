package com.udacity

import androidx.lifecycle.ViewModel

class ViewModel(): ViewModel() {

    var downloadLink : String = ""

    fun downloadLinkGlide(){
        downloadLink = "https://github.com/bumptech/glide"
    }

    fun downloadLinkUdacity(){
        downloadLink = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"
    }

    fun downloadLinkRetrofit(){
        downloadLink = "https://github.com/square/retrofit"
    }
}