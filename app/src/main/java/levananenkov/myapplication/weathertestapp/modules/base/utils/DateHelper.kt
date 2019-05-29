package levananenkov.myapplication.weathertestapp.modules.base.utils

import java.text.SimpleDateFormat
import java.util.*

open class DateHelper {

    var DB_FORMAT = "yyyy-MM-dd HH:mm:ss"
    var FILE_NAME_FORMAT = "yyyyMMdd"

    open fun dateToFileNameStr(date: Date? = null, formatStr: String? = FILE_NAME_FORMAT): String {
        return dateToDbStr(date, formatStr)
    }

    open fun dateToDbStr(date: Date? = null, formatStr: String? = DB_FORMAT): String {
        var tmpDate = date

        if (tmpDate == null) {
            tmpDate = Date()
        }

        val sm = SimpleDateFormat(formatStr)

        return sm.format(tmpDate)
    }

    open fun dbStrToDate(dbDate: String, formatStr: String? = DB_FORMAT): Date {

        val sm = SimpleDateFormat(formatStr)

        return sm.parse(dbDate)
    }

    open fun dbDatetimeToHumanDate(datetime: String): String {
        var dateList = datetime.split(" ")
        var dbDate = dateList[0]
        var dbDateList = dbDate.split("-")

        return dbDateList.reversed().joinToString(".") + " " + dateList[1]
    }

    open fun dbDatetimeToHumanTime(datetime: String): String {
        var dateList = datetime.split(" ")
        var dbTime = dateList[1]
        var dbTimeList = dbTime.split(":")

        return dbTimeList[0] + ":" + dbTimeList[1]
    }

    open fun dbDatetimeToHumanDateTime(datetime: String): String {
        var humanTime = dbDatetimeToHumanTime(datetime)

        if (isTodayDate(datetime)) {
            return humanTime
        }

        var humanDate = dbDatetimeToHumanDate(datetime)

        return humanTime + " " + humanDate
    }

    open fun isTodayDate(datetime: String): Boolean {
        var dateTimeDate = dbStrToDate(datetime)

        if (dbDatetimeToHumanDate(dateToDbStr(dateTimeDate)) == dbDatetimeToHumanDate(dateToDbStr())) {
            return true
        }

        return false
    }

}
