package com.example.anothertryofmusicanim.repo

import android.content.Context
import com.example.anothertryofmusicanim.entities.Game
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.parseList
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.io.InputStream

class Repository {
    companion object{
        @ImplicitReflectionSerializer
        fun get(c: Context): List<Game>{
            var fileContent = ""
            try{
                val inputStream: InputStream = c.assets.open("games.json")
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                fileContent = String(buffer)
            } catch (ex:IOException){
                Timber.e(ex)
            }
            return Json.parseList(fileContent)
        }
    }
}