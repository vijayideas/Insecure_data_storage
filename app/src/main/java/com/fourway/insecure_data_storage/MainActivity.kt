package com.fourway.insecure_data_storage

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File
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
                if (hasStoragePermission()) {
                    saveSettings()
                    snakbar(view, "Settings saved", "Action")
                }else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        requestPermissions(STORAGE_PERMISSION, REQUEST_STORAGE_PERMISSION_CODE)

                }

            }catch (e:FileNotFoundException) {
                snakbar(view, "Something went wrong, settings not saved.", "Action")
            }catch (e: SecurityException) {
                snakbar(view, "Something went wrong, settings not saved.", "Action")
            }

        }
    }


    @Throws(FileNotFoundException::class, SecurityException::class)
    private fun saveSettings() {

        val name : String = editName.text.toString()
        val email : String = editEmail.text.toString()
        val username : String = editUsername.text.toString()

        /* check external storage mounted or not*/
        if (!isExternalStorageWritable()) {
            Toast.makeText(this@MainActivity, "The external storage is not mounted",
                    Toast.LENGTH_SHORT).show()
            return
        }


        val file: File = File(Environment.getExternalStorageDirectory(), name)
        val fileOutputStream: FileOutputStream = FileOutputStream(file)

        val pw = PrintWriter(fileOutputStream)

        pw.println("name: $name")
        pw.println("email: $email")
        pw.println("username: $username")

        pw.flush()
        pw.close()

    }

    /* show snalebar message */
    private fun snakbar(view: View, message: String, action: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction(action, null).show()
    }


    /* Checks if external storage is available for read and write */
    private fun isExternalStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }



    /* Checks if READ_EXTERNAL_STORAGE is PERMISSION_GRANTED return true else false */
    private fun hasStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkSelfPermission(STORAGE_PERMISSION[0]) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_STORAGE_PERMISSION_CODE && grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            saveSettings()
            Toast.makeText(this@MainActivity, "Settings saved",
                    Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this@MainActivity, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }


    companion object {
        private val STORAGE_PERMISSION = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        private val REQUEST_STORAGE_PERMISSION_CODE = 111
    }




}
