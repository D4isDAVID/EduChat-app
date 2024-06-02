package io.github.d4isdavid.educhat.utils

import android.content.res.Resources
import io.github.d4isdavid.educhat.R
import java.time.Duration

fun Duration.toWeeks() = toDays() / 14
fun Duration.toMonths() = toDays() / 30
fun Duration.toYears() = toMonths() / 12

fun Duration.toRelativeString(resources: Resources): String {
    return if (toYears() > 0) {
        val count = toYears().toInt()
        resources.getQuantityString(R.plurals.years_ago, count, count)
    } else if (toMonths() > 0) {
        val count = toMonths().toInt()
        resources.getQuantityString(R.plurals.months_ago, count, count)
    } else if (toWeeks() > 0) {
        val count = toWeeks().toInt()
        resources.getQuantityString(R.plurals.weeks_ago, count, count)
    } else if (toDays() > 0) {
        val count = toDays().toInt()
        resources.getQuantityString(R.plurals.days_ago, count, count)
    } else if (toHours() > 0) {
        val count = toHours().toInt()
        resources.getQuantityString(R.plurals.hours_ago, count, count)
    } else if (toMinutes() > 0) {
        val count = toMinutes().toInt()
        resources.getQuantityString(R.plurals.minutes_ago, count, count)
    } else {
        resources.getString(R.string.just_now)
    }
}
