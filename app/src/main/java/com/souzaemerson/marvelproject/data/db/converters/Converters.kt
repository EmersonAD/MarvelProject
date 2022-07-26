package com.souzaemerson.marvelproject.data.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.souzaemerson.marvelproject.data.model.*
import java.lang.reflect.Type
import java.util.*

class Converters {
    @TypeConverter
    fun fromThumbnail(thumbnail: Thumbnail): String =
        Gson().toJson(thumbnail)

    @TypeConverter
    fun toThumbnail(string: String): Thumbnail =
        Gson().fromJson(string, Thumbnail::class.java)

    @TypeConverter
    fun fromListUrls(lists: List<Urls>): String =
        Gson().toJson(lists)

    @TypeConverter
    fun toListUrls(string: String?): List<Urls?>? {
        if (string == null) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<Urls?>?>() {}.type
        return Gson().fromJson<List<Urls?>>(string, listType)
    }

    @TypeConverter
    fun fromListResults(lists: List<Results>): String =
        Gson().toJson(lists)

    @TypeConverter
    fun toListResults(string: String?): List<Results?>? {
        if (string == null) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<Results?>?>() {}.type
        return Gson().fromJson<List<Results?>>(string, listType)
    }

    @TypeConverter
    fun fromComics(comics: Comics): String =
        Gson().toJson(comics)

    @TypeConverter
    fun toComics(string: String): Comics =
        Gson().fromJson(string, Comics::class.java)

    @TypeConverter
    fun fromSeries(series: Series): String =
        Gson().toJson(series)

    @TypeConverter
    fun toSeries(string: String): Series =
        Gson().fromJson(string, Series::class.java)

    @TypeConverter
    fun fromStories(stories: Stories): String =
        Gson().toJson(stories)

    @TypeConverter
    fun toStories(string: String): Stories =
        Gson().fromJson(string, Stories::class.java)

    @TypeConverter
    fun fromEvents(events: Events): String =
        Gson().toJson(events)

    @TypeConverter
    fun toEvents(string: String): Events =
        Gson().fromJson(string, Events::class.java)
}