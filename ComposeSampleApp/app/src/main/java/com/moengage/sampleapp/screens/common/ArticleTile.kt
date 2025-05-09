package com.moengage.sampleapp.screens.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.moengage.sampleapp.model.Article
import com.moengage.sampleapp.ui.theme.Grey
import com.moengage.sampleapp.R
import com.moengage.sampleapp.libaries.moengage.MoEngageHelper
import com.moengage.sampleapp.model.toMap
import com.moengage.sampleapp.util.openDeeplink
import com.moengage.sampleapp.viewmodel.MainViewModel

@Composable
fun ArticleTile(
    modifier: Modifier = Modifier,
    article: Article,
    mainViewModel: MainViewModel
) {

    val isArticleBookmarked = rememberSaveable {
        mutableStateOf(article.isBookmarked)
    }
    val context = LocalContext.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(true, onClick = {
                context.openDeeplink(article.url)
            }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth(0.2f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(64.dp)
                    .clip(MaterialTheme.shapes.medium),
                model = ImageRequest.Builder(LocalContext.current).data(article.urlToImage).build(),
                contentDescription = "",
                contentScale = ContentScale.Crop,
            )

            article.source.name?.let {
                Text(
                    modifier = Modifier.size(64.dp),
                    fontSize = 10.sp,
                    text = it,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    color = Grey
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .weight(1f)
        ) {
            article.description?.let {
                Text(
                    text = it,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 4
                )
            }

            Row {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    textAlign = TextAlign.Start,
                    fontSize = 12.sp,
                    text = article.publishedAt,
                    maxLines = 1,
                    color = Grey
                )

                Button(
                    modifier = Modifier
                        .padding(0.dp),
                    colors = ButtonColors(
                        containerColor = Transparent,
                        contentColor = Grey,
                        disabledContainerColor = Transparent,
                        disabledContentColor = Transparent
                    ),
                    onClick = {
                        if (!article.isBookmarked) {
                            isArticleBookmarked.value = true
                            article.isBookmarked = true
                            mainViewModel.addToBookmark(listOf(article))
                            MoEngageHelper.trackEvent(
                                context,
                                "Bookmarked",
                                article.toMap()
                            )
                        }
                    }
                ) {
                    Image(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(
                            id =
                                if (isArticleBookmarked.value) {
                                    R.drawable.icon_bookmark
                                } else {
                                    R.drawable.icon_bookmarked
                                }
                        ),
                        contentDescription = ""
                    )

                }
            }
        }
    }
}