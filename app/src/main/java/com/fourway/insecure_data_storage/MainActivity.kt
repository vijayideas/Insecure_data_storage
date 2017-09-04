package com.fourway.insecure_data_storage

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.PrintWriter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->

            try {
                saveSettings()
                snakbar(view, "Settings saved", "Action")

            }catch (e:FileNotFoundException) {
                snakbar(view, "Something went wrong, settings not saved.", "Action")
            }catch (e: SecurityException) {
                snakbar(view, "Something went wrong, settings not saved.", "Action")
            }

        }
    }

    private fun snakbar(view: View, message: String, action: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction(action, null).show()
    }

    @Throws(FileNotFoundException::class, SecurityException::class)
    private fun saveSettings() {

        val name : String = editName.text.toString()
        val email : String = editEmail.text.toString()
        val username : String = editUsername.text.toString()

        val fileOutputStream : FileOutputStream = openFileOutput(name, Context.MODE_WORLD_READABLE)

        val pw = PrintWriter(fileOutputStream)

        pw.println("name: $name")
        pw.println("email: $email")
        pw.println("username: $username")

        pw.flush()
        pw.close()

    }


}
