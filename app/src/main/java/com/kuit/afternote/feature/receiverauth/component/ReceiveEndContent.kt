package com.kuit.afternote.feature.receiverauth.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.feature.receiverauth.domain.entity.DeliveryVerificationStatus
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun ReceiveEndContent(
    deliveryVerificationStatus: DeliveryVerificationStatus? = null
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.receiver_verify_complete_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Sansneo
        )

        Spacer(modifier = Modifier.height(14.dp))

        val statusMessage = when (deliveryVerificationStatus?.status) {
            "APPROVED" -> stringResource(R.string.receiver_verify_status_approved)
            "REJECTED" -> {
                val base = stringResource(R.string.receiver_verify_status_rejected)
                if (!deliveryVerificationStatus.adminNote.isNullOrBlank()) {
                    "$base\n${deliveryVerificationStatus.adminNote}"
                } else base
            }
            else -> stringResource(R.string.receiver_verify_status_pending)
        }
        Text(
            text = statusMessage,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = Sansneo
        )
    }
}
