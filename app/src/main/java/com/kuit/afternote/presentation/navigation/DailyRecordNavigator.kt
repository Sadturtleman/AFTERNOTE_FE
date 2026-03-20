package com.kuit.afternote.presentation.navigation

import com.kuit.afternote.feature.dailyrecord.presentation.navigiation.RecordRoute

interface DailyRecordNavigator {
    fun gotoDailyRecord(route: RecordRoute)
}
