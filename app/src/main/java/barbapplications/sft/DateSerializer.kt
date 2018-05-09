package barbapplications.sft

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.util.*

/**
 * Created by kalimole on 11/2/2017.
 */
class DateSerializer : JsonSerializer<Date> {
    override fun serialize(date: Date?, typeOfDate: Type?,
                           context: JsonSerializationContext?): JsonElement = JsonPrimitive(
            DateConverter.fromDate(date))
}