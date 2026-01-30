package com.kuit.afternote.core.navigation

import com.kuit.afternote.feature.dailyrecord.presentation.navigiation.RecordRoute

interface DailyRecordNavigator {
    fun gotoDailyRecord(route: RecordRoute)
}
