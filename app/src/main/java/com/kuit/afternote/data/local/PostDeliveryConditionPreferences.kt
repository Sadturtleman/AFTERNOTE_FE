package com.kuit.afternote.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.postDeliveryDataStore: DataStore<Preferences> by
    preferencesDataStore(name = "post_delivery_condition")

/**
 * 사후 전달 조건 설정을 로컬에 저장/조회합니다.
 *
 * @param deliveryMethod "AUTOMATIC_TRANSFER" | "RECEIVER_APPROVAL_TRANSFER"
 * @param triggerCondition "APP_INACTIVITY" | "SPECIFIC_DATE" | "RECEIVER_REQUEST"
 * @param triggerDateIso "yyyy-MM-dd" or null (when trigger is SPECIFIC_DATE)
 */
@Singleton
class PostDeliveryConditionPreferences
@Inject
constructor(
    @ApplicationContext private val context: Context
) {
    private val deliveryMethodKey = stringPreferencesKey("delivery_method")
    private val triggerConditionKey = stringPreferencesKey("trigger_condition")
    private val triggerDateIsoKey = stringPreferencesKey("trigger_date_iso")

    val deliveryMethodFlow: Flow<String?> =
        context.postDeliveryDataStore.data.map { it[deliveryMethodKey] }
    val triggerConditionFlow: Flow<String?> =
        context.postDeliveryDataStore.data.map { it[triggerConditionKey] }
    val triggerDateIsoFlow: Flow<String?> =
        context.postDeliveryDataStore.data.map { it[triggerDateIsoKey] }

    suspend fun getDeliveryMethod(): String? =
        context.postDeliveryDataStore.data.first()[deliveryMethodKey]

    suspend fun getTriggerCondition(): String? =
        context.postDeliveryDataStore.data.first()[triggerConditionKey]

    suspend fun getTriggerDateIso(): String? =
        context.postDeliveryDataStore.data.first()[triggerDateIsoKey]

    suspend fun save(
        deliveryMethod: String,
        triggerCondition: String,
        triggerDateIso: String?
    ) {
        context.postDeliveryDataStore.edit { prefs ->
            prefs[deliveryMethodKey] = deliveryMethod
            prefs[triggerConditionKey] = triggerCondition
            if (triggerDateIso != null) {
                prefs[triggerDateIsoKey] = triggerDateIso
            } else {
                prefs.remove(triggerDateIsoKey)
            }
        }
    }
}
