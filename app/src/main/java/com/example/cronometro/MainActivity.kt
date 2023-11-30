package com.example.cronometro


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent.DispatcherState
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    lateinit var txt1: TextView
    lateinit var imgbt1: ImageButton
    lateinit var imgbt2: ImageButton
    lateinit var imgbt3: ImageButton
    lateinit var imgbt4: ImageButton
    lateinit var btn1: Button
    lateinit var list1: ListView

    var corutina1: Job? = null
    val registros = mutableListOf<String>() //almacena los registros

    private var estadoContador: String = "00:00:00"
    private val REQUEST_CODE_SECOND_ACTIVITY = 101 //incia el segundo activity


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txt1 = findViewById(R.id.textView1)
        imgbt1 = findViewById(R.id.imageButton1)
        imgbt2 = findViewById(R.id.imageButton2)
        imgbt3 = findViewById(R.id.imageButton3)
        imgbt4 = findViewById(R.id.imageButton4)
        btn1 = findViewById(R.id.button1)

        val listView = findViewById<ListView>(R.id.listView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, registros)
        listView.adapter = adapter

        txt1.text = if (cont < 10) {
            "0$cont"
        } else {
            cont.toString()
        }


        imgbt1.setOnClickListener { //boton play
            corutina1?.cancel()
            hilo1()
        }

        imgbt2.setOnClickListener{ // boton pausa
            corutina1?.cancel()
            val minutos = cont / 6000
            val segundos = (cont % 6000) / 100
            val milisegundos = cont % 100

            val minutosStr = if (minutos < 10) {
                "0$minutos"
            } else {
                minutos.toString()
            }

            val segundosStr = if (segundos < 10) {
                "0$segundos"
            } else {
                segundos.toString()
            }

            val milisegundosStr = if (milisegundos < 10) {
                "0$milisegundos"
            } else {
                milisegundos.toString()
            }

            estadoContador = "$minutosStr:$segundosStr:$milisegundosStr"
        }

        imgbt3.setOnClickListener{ //boton reinicio
            corutina1?.cancel()
            cont = 0 // reinicia el contador a cero al hacer clic en imgbt3
            txt1.text = "00:00:00"
        }

        imgbt4.setOnClickListener{ //boton de registros
            val tiempoActual = txt1.text.toString()
            registros.add(tiempoActual)
            adapter.notifyDataSetChanged()
        }

        btn1.setOnClickListener{
            corutina1?.cancel()
            val registrosString = ArrayList(registros)

            val intent = Intent(this, MainActivity2::class.java)
            intent.putExtra("estado_contador", estadoContador)
            intent.putStringArrayListExtra("registros", registrosString)
            startActivityForResult(intent, REQUEST_CODE_SECOND_ACTIVITY)
        }

    }

    fun hilo1() {
        corutina1 = launch(Dispatchers.Main) {
            while (true) {
                cont++

                val minutos = cont / 6000
                val segundos = (cont % 6000) / 100
                val milisegundos = cont % 100

                val minutosStr = if (minutos < 10) {
                    "0$minutos"
                } else {
                    minutos.toString()
                }

                val segundosStr = if (segundos < 10) {
                    "0$segundos"
                } else {
                    segundos.toString()
                }

                val milisegundosStr = if (milisegundos < 10) {
                    "0$milisegundos"
                } else {
                    milisegundos.toString()
                }

                if (segundos == 60) {
                    cont = minutos * 6000
                }


                txt1.text = "$minutosStr:$segundosStr:$milisegundosStr"

                delay(10)

                // aqui se reinicia el cronometro a 0 cuando llega a 60 segundos
                if (cont == 360000) {
                    cont = 0
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SECOND_ACTIVITY && resultCode == Activity.RESULT_OK) {
            val selectedTime = data?.getStringExtra("selected_time") // agarra el tiempo seleccionado del segundo activity


            selectedTime?.let {
                txt1.text = it
                estadoContador = it
            }
        }
    }

    companion object{
        var cont: Int = 0
    }
}

