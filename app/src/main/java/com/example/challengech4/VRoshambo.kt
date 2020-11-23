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
 *  File VRoshambo.kt berisikan semua file yang merepresentasikan VIEW pada MVC
 *  ada 3 View (activity) :
 *  1. Menu Utama --> class VRoshambo
 *  2. Vs Komputer --> class VComputer
 *  3. Vs Player --> class VPlayer --- UNDER CONSTRUCTION ---
 *  ===========================================================================
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

    /* variable untuk menyimpan data input dan dipertukarkan antara View-Controller-Model
    *  Format mapping :
    *  Key       Value
    *  ----------------------------------------------------
    *  time      timestamp
    *  player1   batu/gunting/kertas
    *  player2   batu/gunting/kertas
    *  result    String : 1/2/3 --> di generate oleh model
    *  type      vs Player/vs Komputer
    */
    private val data: MutableMap<String, String> = mutableMapOf<String, String>()

    private var controller: CRoshambo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vs_komputer)
        controller = CRoshambo(this)
        setListener()
    }

    override fun getGameData(): MutableMap<String, String>? {
        return this.data
    }

    /* Fungsi berikut untuk menampilkan hasil permainan dengan kode berikut :
       1 -> Pemain 1 Menang!
       2 -> Pemain 2 menang!
       3 -> Draw!
    * */
    override fun result(result: String?) {
        val tvResult = findViewById<TextView>(R.id.tvResult)
        val tvVS = findViewById<TextView>(R.id.tvVs)

        when (result) {
            "1" -> {
                tvResult.text = getString(R.string.result1)
                tvResult.setBackgroundColor(Color.parseColor("#99CC00"))
                tvResult.visibility = View.VISIBLE
                tvVS.visibility = View.INVISIBLE
            }
            "2" -> {
                tvResult.text = getString(R.string.result2)
                tvResult.setBackgroundColor(Color.parseColor("#99CC00"))
                tvResult.visibility = View.VISIBLE
                tvVS.visibility = View.INVISIBLE
            }
            "3" -> {
                tvResult.text = getString(R.string.result3)
                tvResult.setBackgroundColor(Color.parseColor("#FF3700B3"))
                tvResult.visibility = View.VISIBLE
                tvVS.visibility = View.INVISIBLE
            }
        }
    }

    //Asignment listener ke komponen layout
    private fun setListener() {
        val ivBack = findViewById<ImageView>(R.id.ivBack)
        val ivReset = findViewById<ImageView>(R.id.ivReset)
        val mapInputP1 = mapOf(
            R.id.ivStone to "batu",
            R.id.ivPaper to "kertas",
            R.id.ivScissor to "gunting"
        )

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
                    controller?.evaluate()

                    //Set status permainan
                    finish = true
                }
            }
        }

        ivBack.setOnClickListener { finish() }
        ivReset.setOnClickListener {
            data.clear()
            finish = false
            setContentView(R.layout.vs_komputer)
            setListener()
        }
    }

    //Mengembalikan pilihan random komputer
    private fun computerPick(): String {
        val comPick = arrayOf("batu", "kertas", "gunting").random()
        when (comPick) {
            "batu" -> findViewById<ImageView>(R.id.ivBgStoneCom).visibility = View.VISIBLE
            "kertas" -> findViewById<ImageView>(R.id.ivBgPaperCom).visibility = View.VISIBLE
            "gunting" -> findViewById<ImageView>(R.id.ivBgScissorCom).visibility = View.VISIBLE
        }
        return comPick
    }
}
