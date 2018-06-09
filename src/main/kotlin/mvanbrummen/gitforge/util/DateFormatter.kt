package mvanbrummen.gitforge.util

import org.ocpsoft.prettytime.PrettyTime
import java.util.*

object DateFormatter {

    fun formatUnixTimestamp(timestamp: Int): String = PrettyTime().format(Date(timestamp * 1000L))

}