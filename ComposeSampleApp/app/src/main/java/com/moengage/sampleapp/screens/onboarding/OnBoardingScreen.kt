package com.moengage.sampleapp.screens.onboarding

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.moengage.sampleapp.libaries.moengage.MoEngageHelper
import com.moengage.sampleapp.state.ApplicationState
import com.moengage.sampleapp.ui.theme.Grey
import com.moengage.sampleapp.util.openPermissionPage
import com.moengage.sampleapp.viewmodel.OnBoardingViewModel

@Composable
fun OnboardScreen(
    activity: ComponentActivity,
    onBoardingViewModel: OnBoardingViewModel = hiltViewModel()
) {
    val onboardPages = getOnBoardingPages(LocalContext.current).filter {
        ApplicationState.osVersion >= it.minSdkVersion
    }
    val currentPageIndex = remember { mutableIntStateOf(0) }
    val currentPage = remember { derivedStateOf { onboardPages[currentPageIndex.intValue] } }

    LaunchedEffect(Unit) {
        MoEngageHelper.onScreenChange("OnboardScreen")
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.1f),
        )

        Image(
            modifier = Modifier.size(128.dp),
            painter = painterResource(currentPage.value.icon),
            contentDescription = currentPage.value.title
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f),
        )

        OnBoardDetails(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            currentPage = currentPage.value,
        )

        OnBoardNavButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            currentPage = currentPage.value,
            activity = activity
        ) {
            if (currentPageIndex.intValue == onboardPages.size - 1) {
                onBoardingViewModel.updateOnBoardingStatus(true)
            } else {
                currentPageIndex.intValue++
            }
        }
    }
}

@Composable
fun OnBoardDetails(
    modifier: Modifier = Modifier,
    currentPage: OnBoardingPageItem,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = currentPage.title,
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = Black,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = currentPage.description,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = Grey,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun OnBoardNavButton(
    modifier: Modifier = Modifier,
    currentPage: OnBoardingPageItem,
    activity: ComponentActivity,
    onNextClicked: () -> Unit
) {
    val permissionState: PermissionState = rememberPermissionState(currentPage.permission) {
        currentPage.postPermissionCallback(it)
        onNextClicked()
    }

    if (permissionState.status is PermissionStatus.Granted) {
        // Move to next page if permission is already granted
        currentPage.postPermissionCallback(true)
        onNextClicked()
    }

    Button(
        modifier = modifier.fillMaxWidth(0.8f),
        onClick = {
            if (currentPage.isSpecialPermission) {
                activity.openPermissionPage(currentPage.permissionAction)
                onNextClicked()
            } else {
                permissionState.launchPermissionRequest()
            }
        },
    ) {
        Text(text = "Allow")
    }
}