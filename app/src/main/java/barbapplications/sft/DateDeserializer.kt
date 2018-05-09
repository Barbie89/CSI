package au.com.hrbandroid.tools

import barbapplications.sft.DateConverter
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.util.Date

/**
 * Created by kalimole on 11/2/2017.
 */
class DateDeserializer : JsonDeserializer<Date> {
    override fun deserialize(dateString: JsonElement?, type: Type?,
                             context: JsonDeserializationContext?): Date = DateConverter.toDate(
            dateString?.asString)
}