/**
 * DONT FORGET TO ADD THE INTERNET PERMISSION
 **/

package com.example.tae_kotlin_week_end_assesment_asynctask

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.security.cert.CertPath

class MainActivity : AppCompatActivity() {
    private val TAG ="mainactivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //
        val downloadingData = DownloadingData()
        //declaring the target URL
        downloadingData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml")
        Log.d(TAG, "onCreate method")
    }

    companion object {
        //the inner class holds a reference to the main activity
        //we need to create a companion object
        private class DownloadingData : AsyncTask<String, Void, String>(){
            private val TAG = "DownloadingData"




            //runs in the main UI thread
            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                Log.d (TAG, "onPostExecute: $result")
            }

            //p0 is a String parameter, to use a URL p0 has to be changed
            //to url
            override fun doInBackground(vararg url: String?): String {
                Log.d(TAG,"doInBackground activated")
                val myRssFeed = downlaodXML(url[0])
                if(myRssFeed.isEmpty()){
                    //using Log.e because when app is uploaded, we want to keep the message error
                    Log.e(TAG, "Error Dowloading")
                }
                return myRssFeed
            }
            private fun downlaodXML(urlPath: String?): String{
                val xmlResult = StringBuilder()

                //we need a try and catch to check if there any errors in the whole
                //download process and internet connectivity
                try {
                    var url = URL(urlPath)
                    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                    val response = connection.responseCode
                    Log.d(TAG, "downloadXML: $response")

//            val inputStream = connection.inputStream
//            val inputStreamReader = InputStreamReader(inputStream)

                    //replacing the 2 above lines with one
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))

                    //in order to read the data until the last bit of it we need an inputBuffer that will read characters
                    //reading a limit of 500 characters at the time
                    val inputBuffer = CharArray(500)
                    var readingChars = 0

                    //creating a while loop to check that there is still data available and then stop
                    while (readingChars >= 0) {
                        readingChars = reader.read(inputBuffer)
                        if (readingChars > 0) {
                            xmlResult.append(String(inputBuffer, 0, readingChars))
                        }
                    }
                    //we need a method to close the reader
                    reader.close()
                    return xmlResult.toString()

//                }catch (e: MalformedURLException){
//                    Log.e(TAG, "invalid URL ${e.message}")
//                }catch (e: IOException){
//                    Log.e(TAG,"IO exception: ${e.message}")
//                }
                //We can put all the catch error in even better pratice
                }catch (e:Exception){
                    //creating 1 variable for all the exeptions
                    val errorMessage: String = when (e){
                        is MalformedURLException -> "downloadXML: Invalid URL ${e.message}"
                        is IOException -> "downloadXML: IO Exception reading data:${e.message}"
                        is SecurityException -> {
                            e.printStackTrace()
                            "downloadXML: Security Exception. Needs permission?${e.message}"
                        }
                        else -> "Unknown error"

                    }
                }


                return ""
        }
    }


    }

}
