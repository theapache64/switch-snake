package com.theapache64.switchsnake.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomRadioButton(selected: Boolean, onClick: () -> Unit, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(end = 20.dp)
            .clickable {
                onClick()
            }
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )

        Text(text = text)
    }
}