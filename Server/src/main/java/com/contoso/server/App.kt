package com.contoso.server

import android.app.Application

class App : Application() {

    init {
        app = this
    }

    companion object {

        lateinit var app: App
    }
}
