package com.example.cronometro

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView

data class Registro(val tiempo: String, val descripcion: String)
class MainActivity2 : AppCompatActivity() {

    private lateinit var textView1: TextView
    lateinit var btn: Button
    private lateinit var registros: List<Registro>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        btn = findViewById(R.id.button)
        textView1 = findViewById(R.id.textView1)

        btn.setOnClickListener {
            // aqui agarra el tiempo seleccionado de la lista
            val selectedTime = textView1.text.toString()

            // aqui envia el tiempo seleccionado de vuelta al primer activity y finaliza en este activity2
            val intent = Intent()
            intent.putExtra("selected_time", selectedTime)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }


        val estadoContador = intent.getStringExtra("estado_contador")
        val textView1 = findViewById<TextView>(R.id.textView1)
        textView1.text = estadoContador


        val registros = intent.getStringArrayListExtra("registros")
        val listView = findViewById<ListView>(R.id.listView)

        if (registros != null) {
            val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, registros.toList())
            listView.adapter = adapter

        } else {

        }

        listView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            textView1.text = selectedItem
        }
    }
}