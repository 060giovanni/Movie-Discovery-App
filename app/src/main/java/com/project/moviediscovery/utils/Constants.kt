package com.project.moviediscovery.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog

object Constants {
    fun alertDialogMessage(context: Context, message: String, title: String? = null) {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)

        with(builder)
        {
            if (title != null) {
                setTitle(title)
            }
            setMessage(message)
            setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            show()
        }
    }
    
    const val BASE_POSTER_PATH = "https://image.tmdb.org/t/p/w342"
    const val BASE_BACKDROP_PATH = "https://image.tmdb.org/t/p/w780"
}
