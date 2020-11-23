package com.example.challengech4

import android.util.Log

class CRoshambo(private val view: CallbackInt) {
    private var model: MRoshambo? = null

    init {
        model = MRoshambo()
    }

    fun evaluate() {
        this.model?.set(view.getGameData())
        this.model?.evaluate()
        val data = this.model?.get()
        this.view.result(data?.getValue("result"))

        data?.forEach { t, u ->
            Log.d(t, u)
        }
    }
}