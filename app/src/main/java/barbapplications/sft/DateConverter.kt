package barbapplications.sft

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by kalimole on 11/2/2017.
 */
class DateConverter {

    companion object {
        private val pattern: String = "yyyy-MM-dd'T'HH:mm:ss"

        fun toDate(date: String?): Date {
            val timeZone = "EST"
            val dateFormat = SimpleDateFormat(pattern, Locale.US)
            dateFormat.timeZone = TimeZone.getTimeZone("EST")
            var cal: Calendar = GregorianCalendar.getInstance()
//            cal.time = dateFormat.parse(date)
            return dateFormat.parse(date)
        }

        fun fromDate(date: Date?): String = SimpleDateFormat(pattern, Locale.US).format(date)
    }
}