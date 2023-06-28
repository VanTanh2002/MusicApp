package com.poupa.vinylmusicplayer.deezer

data class DeezerResponse(
        val `data`: List<Data>,
        val next: String,
        val total: Int
)