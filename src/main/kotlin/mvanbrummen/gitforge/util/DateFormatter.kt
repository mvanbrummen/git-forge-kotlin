package mvanbrummen.gitforge.util

import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {

    fun formatUnixTimestampPretty(timestamp: Int): String = PrettyTime().format(Date(timestamp * 1000L))

    fun formatUnixTimestamp(timestamp: Int): String {
        val fmt = SimpleDateFormat("EEEE, MMMM d, yyyy")
        val date = Date(timestamp * 1000L)

        return fmt.format(date)
    }

}