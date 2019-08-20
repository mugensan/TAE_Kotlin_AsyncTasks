package com.example.tae_kotlin_week_end_assesment_asynctask

class FeedEntry{
    var title:String=""
    //    var artist:String=""
//    var releaseDate:String=""
    var summary:String=""
//    var imageURL:String=""


    override fun toString(): String {
        return """
            name = $title

            summary = $summary

        """.trimIndent()
    }
}