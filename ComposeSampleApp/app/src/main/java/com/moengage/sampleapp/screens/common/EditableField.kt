package com.moengage.sampleapp.screens.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moengage.sampleapp.ui.theme.Grey

@Composable
fun EditableField(
    label: String,
    initialValue: String?,
    isEditable: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    onSave: (String) -> Unit,
) {
    val text = rememberSaveable { mutableStateOf("") }
    val isEditing = rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = Grey,
                fontSize = 16.sp
            )
            if (isEditable) {
                IconButton(
                    onClick = {
                        isEditing.value = !isEditing.value
                        if (!isEditing.value) {
                            onSave(text.value)
                        }
                    },
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = if (isEditing.value) {
                            Icons.Filled.Check
                        } else {
                            Icons.Filled.Edit
                        },
                        contentDescription = label
                    )
                }
            }
        }

        if (isEditing.value) {
            OutlinedTextField(
                value = text.value,
                onValueChange = { text.value = it },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                singleLine = true
            )
        } else {
            Text(
                text = initialValue ?: "",
                color = Black,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
    }
}