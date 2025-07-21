package com.projects.reverbnews.ui.uiStateFunctions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.projects.reverbnews.module.error.mapErrorCodeToDetails

@Composable
fun ErrorScreen(
    retryAction: () -> Unit,
    errorCode: Int?,
    modifier: Modifier = Modifier
) {
    val errorDetails = mapErrorCodeToDetails(errorCode ?: -1)

    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("lottie/${errorDetails.lottieAsset}")
    )
    Scaffold {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(50.dp))
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = errorDetails.message,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = retryAction) {
                Text("Retry")
            }
        }
    }
}