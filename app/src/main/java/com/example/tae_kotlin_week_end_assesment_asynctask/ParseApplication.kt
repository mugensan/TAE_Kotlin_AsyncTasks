package com.example.tae_kotlin_week_end_assesment_asynctask

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.lang.Exception

class ParseApplication {
    private val TAG = "parseApplication"
    val applications = ArrayList<FeedEntry>()

    fun parse(xmlData: String): Boolean {
        Log.d(TAG, "Parse called with $xmlData")
        var status = true
        var inEntry = false
        var textValue = ""

        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()
            xpp.setInput(xmlData.reader())
            var evenType = xpp.eventType
            var currentRecord = FeedEntry()

            while (evenType != XmlPullParser.END_DOCUMENT) {
                val tagName = xpp.name?.toLowerCase()
                when (evenType) {
                    XmlPullParser.START_TAG -> {
                        Log.d(TAG, "parse: starting tag for " + tagName)
                        if (tagName == "entry") {
                            inEntry = true
                        }

                    }
                    XmlPullParser.TEXT -> textValue = xpp.text

                    XmlPullParser.END_TAG -> {
                        Log.d(TAG, "Parse: Ending tag for " + tagName)
                        if (inEntry) when (tagName) {
                            "entry" -> {
                                applications.add(currentRecord)
                                inEntry = false
                                //creating a new object
                                currentRecord = FeedEntry()
                            }
                            //this is how you call the tags in the website's XML
                            "title" -> currentRecord.title = textValue
//                            "artist" -> currentRecord.artist = textValue
//                            "releasedate" -> currentRecord.releaseDate = textValue
                            "summary" -> currentRecord.summary = textValue
//                            "image" -> currentRecord.imageURL = textValue
                        }
                    }
                }
            }

            evenType = xpp.next()
        } catch (e: Exception) {
            e.printStackTrace()
            status = false
        }

        return status
    }
}


