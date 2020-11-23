package com.example.challengech4

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDateTime

/*  ===========================================================================
    File VRoshambo.kt berisikan semua file yang merepresentasikan VIEW pada MVC
    ada 3 View (activity) :
    1. Menu Utama --> class VRoshambo
    2. Vs Komputer --> class VComputer
    3. Vs Player --> class VPlayer --- UNDER CONSTRUCTION ---
    ===========================================================================
 */

class VRoshambo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_utama)
        val btVsCom = findViewById<Button>(R.id.btVsCom)
        val btExit = findViewById<Button>(R.id.btExit)

        btVsCom.setOnClickListener(View.OnClickListener {
            launchVsCom()
        })
        btExit.setOnClickListener(View.OnClickListener { finish() })
    }

    private fun launchVsCom() {
        val intent = Intent(this, VComputer::class.java)
        startActivity(intent)
    }

}

class VComputer : AppCompatActivity(), CallbackInt {
    //Variabel untuk kontrol flow
    private var finish: Boolean = false

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
    private val data: MutableMap<String, String> = mutableMapOf<String, String>()

    //Variable untuk menghandle komponen layout
    private var tvResult: TextView? = null
    private var tvVS: TextView? = null

    private var ivStone: ImageView? = null
    private var ivPaper: ImageView? = null
    private var ivScissor: ImageView? = null
    private var ivBgStoneCom: ImageView? = null
    private var ivBgPaperCom: ImageView? = null
    private var ivBgScissorCom: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vs_komputer)

        val controller = CRoshambo(this)

        val ivBack = findViewById<ImageView>(R.id.ivBack)
        val ivReset = findViewById<ImageView>(R.id.ivReset)
        tvVS = findViewById(R.id.tvVs)
        tvResult = findViewById(R.id.tvResult)
        ivStone = findViewById(R.id.ivStone)
        ivPaper = findViewById(R.id.ivPaper)
        ivScissor = findViewById(R.id.ivScissor)
        ivBgStoneCom = findViewById(R.id.ivBgStoneCom)
        ivBgPaperCom = findViewById(R.id.ivBgPaperCom)
        ivBgScissorCom = findViewById(R.id.ivBgScissorCom)

        //Mapping ImageView dengan value sebagai masukan player 1
        val mapInputP1 = mapOf(
            R.id.ivStone to "batu",
            R.id.ivPaper to "kertas",
            R.id.ivScissor to "gunting"
        )

        // Set OnClickListener untuk semua ImageView atau Button yang dapat di klik
        ivBack.setOnClickListener { finish() }
        ivReset.setOnClickListener { reset() }
        mapInputP1.forEach { (key, value) ->
            findViewById<ImageView>(key).setOnClickListener {
                if (!finish) {
                    //timestamp
                    data["time"] = LocalDateTime.now().toString()

                    //Pilihan Player 1
                    it.setBackgroundResource(R.drawable.bg_picked)
                    it.isClickable = false
                    data["player1"] = value

                    //Pilihan Player 2 / Com
                    data["player2"] = computerPick()

                    //Tipe Permainan
                    data["type"] = "Player Vs Computer"

                    //Check Pemenang
                    controller.evaluate()

                    //Set status permainan
                    finish = true
                }
            }
        }
    }

    /* Fungsi berikut untuk menampilkan hasil permainan dengan kode berikut :
       1 -> Pemain 1 Menang!
       2 -> Pemain 2 menang!
       3 -> Draw!
    * */
    override fun result(result: String?) {
        when (result) {
            "1" -> {
                tvResult?.text = getString(R.string.result1)
                tvResult?.setBackgroundColor(Color.parseColor("#99CC00"))
                tvResult?.visibility = View.VISIBLE
                tvVS?.visibility = View.INVISIBLE
            }
            "2" -> {
                tvResult?.text = getString(R.string.result2)
                tvResult?.setBackgroundColor(Color.parseColor("#99CC00"))
                tvResult?.visibility = View.VISIBLE
                tvVS?.visibility = View.INVISIBLE
            }
            "3" -> {
                tvResult?.text = getString(R.string.result3)
                tvResult?.setBackgroundColor(Color.parseColor("#FF3700B3"))
                tvResult?.visibility = View.VISIBLE
                tvVS?.visibility = View.INVISIBLE
            }
        }
    }

    override fun getGameData(): MutableMap<String, String>? {
        return this.data
    }

    //Mengembalikan pilihan random komputer

    private fun computerPick(): String {

        val option = arrayOf("batu", "kertas", "gunting")
        val comPick = option.random()

        when (comPick) {
            "batu" -> ivBgStoneCom?.visibility = View.VISIBLE
            "kertas" -> ivBgPaperCom?.visibility = View.VISIBLE
            "gunting" -> ivBgScissorCom?.visibility = View.VISIBLE
        }

        return comPick
    }

    //Mengembalikan semua tampilan, data dan state ke kondisi awal dimulainya activity
    private fun reset() {
        tvVS?.visibility = View.VISIBLE
        tvResult?.visibility = View.INVISIBLE
        ivStone?.setBackgroundResource(R.drawable.bg_transparent)
        ivStone?.isClickable = true
        ivPaper?.setBackgroundResource(R.drawable.bg_transparent)
        ivPaper?.isClickable = true
        ivScissor?.setBackgroundResource(R.drawable.bg_transparent)
        ivScissor?.isClickable = true
        ivBgStoneCom?.visibility = View.INVISIBLE
        ivBgPaperCom?.visibility = View.INVISIBLE
        ivBgScissorCom?.visibility = View.INVISIBLE
        data.clear()
        finish = false
    }
}