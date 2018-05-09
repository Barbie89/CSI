package au.com.hrbandroid.tools

import barbapplications.sft.DateSerializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

/**
 * Created by kalimole on 11/2/2017.
 */
class JsonConverter {
    private val gson: Gson? = GsonBuilder().setPrettyPrinting().serializeNulls().enableComplexMapKeySerialization().registerTypeAdapter(
            Date::class.java, DateSerializer()).registerTypeAdapter(Date::class.java,
            DateDeserializer()).create()

    fun <T> toJson(obj: T): String? = gson?.toJson(obj)
    fun <T> toJson(obj: T, objectType: Type?): String? = gson?.toJson(obj, objectType)
    fun <T> fromJson(jsonString: String, type: Type): T? = gson?.fromJson(jsonString, type)
    fun <T> fromJson(jsonString: String, typeToken: TypeToken<*>): T? = gson?.fromJson(jsonString,
            typeToken.type)
}