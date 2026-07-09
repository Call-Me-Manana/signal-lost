package com.signal.lost.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.signal.lost.presentation.theme.SignalLostTheme

@Composable
fun MainScreen(
    onOpenCaseArchive: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF071013))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "SIGNAL LOST",
            color = Color(0xFFE6F7FF),
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 34.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "MEMORY RECOVERY INTERFACE ONLINE",
            color = Color(0xFF8BE9FD),
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Case archive contains corrupted station records, damaged sensor logs, and unresolved crew events.",
            color = Color(0xFFA7B6BE),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onOpenCaseArchive
        ) {
            Text(text = "Open Case Archive")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    SignalLostTheme {
        MainScreen(onOpenCaseArchive = {})
    }
}
