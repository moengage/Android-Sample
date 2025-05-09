package com.moengage.sampleapp.screens.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.moengage.sampleapp.R
import com.moengage.sampleapp.data.repository.GoogleSigning
import com.moengage.sampleapp.screens.common.EditableField
import com.moengage.sampleapp.screens.common.ToggleField
import com.moengage.sampleapp.util.openDeeplink
import com.moengage.sampleapp.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    val userId = viewModel.getUserId().collectAsState(null)
    val firstName = viewModel.getFirstName().collectAsState(null)
    val lastName = viewModel.getLastName().collectAsState(null)
    val email = viewModel.getEmail().collectAsState(null)

    val isUserIdEditable = remember {
        derivedStateOf { userId.value.isNullOrBlank() }
    }

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            shape = CircleShape,
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = rememberImagePainter(R.drawable.icon_profile),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
        )

        EditableField(
            label = "User Id",
            initialValue = userId.value,
            isEditable = isUserIdEditable.value
        ) {
            viewModel.updateUserId(it)
        }

        EditableField(
            label = "First Name",
            initialValue = firstName.value
        ) {
            viewModel.saveFirstName(it)
        }

        EditableField(
            label = "Last Name",
            initialValue = lastName.value
        ) {
            viewModel.saveLastName(it)
        }

        EditableField(
            label = "Email",
            initialValue = email.value
        ) {
            viewModel.saveEmail(it)
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
        )

        ToggleField(
            label = "GAID Tracking",
            initialCheckStatus = viewModel.getGaidStatus().collectAsState(false).value
        ) {
            viewModel.saveGaidStatus(it)
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    true,
                    onClick = {
                       coroutineScope.launch {
                           GoogleSigning(context).logout {
                                viewModel.onLogout()
                           }
                       }
                    }
                ),
            text = "Logout",
            color = Black,
            fontSize = 16.sp
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    true,
                    onClick = {
                        context.openDeeplink("https://www.moengage.com/")
                    }
                ),
            text = "Terms and Condition",
            color = Black,
            fontSize = 16.sp
        )
    }
}