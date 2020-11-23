package com.example.challengech4

import java.security.Timestamp
import java.sql.Time
import java.util.function.BiConsumer

/*  ============================================================================
    File MRoshambo.kt berisikan satu kelas yang merepresentasikan MODEL pada MVC
    ============================================================================
 */

class MRoshambo() {
    /*variable untuk menyimpan data input dan dipertukarkan antara View-Controller-Model
      Format mapping :
      Key       Value
      ----------------------------------------------------
      time      timestamp
      player1   batu/gunting/kertas
      player2   batu/gunting/kertas
      result    String : 1/2/3 --> di generate oleh model
      type      vs Player/vs Komputer

    */
    private var data: MutableMap<String, String>? = null

    fun set(data: MutableMap<String, String>?) {
        this.data = data
    }

    fun get(): MutableMap<String, String>? {
        return this.data
    }

    fun evaluate(){
        //player 1 win
        if (((data?.getValue("player1") == "batu") && (data?.getValue("player2") == "gunting")) ||
            ((data?.getValue("player1") == "kertas") && (data?.getValue("player2") == "batu")) ||
            ((data?.getValue("player1") == "gunting") && (data?.getValue("player2") == "kertas"))
        ) data?.set("result", "1")

        //player 2 win
        else if (((data?.getValue("player2") == "batu") && (data?.getValue("player1") == "gunting")) ||
            ((data?.getValue("player2") == "kertas") && (data?.getValue("player1") == "batu")) ||
            ((data?.getValue("player2") == "gunting") && (data?.getValue("player1") == "kertas"))
        ) data?.set("result", "2")

        //draw
        else data?.set("result", "3")
    }
}
