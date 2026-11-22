package com.example.learn_androidstudio.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.learn_androidstudio.R
import com.example.learn_androidstudio.domain.model.AppItem
import java.util.Locale

@Composable
fun AppDetailScreen(
    onBackClick: () -> Unit = {},
    onShowcaseItemClick: (AppItem) -> Unit = {},
    onSimilarAppClick: (AppItem) -> Unit = {},
    viewModel: AppDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is AppDetailUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                is AppDetailUiState.Error -> {
                    Text(
                        text = state.message.asString(),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is AppDetailUiState.Content -> {
                    AppDetailContent(
                        content = state,
                        onBackClick = onBackClick,
                        onInstallClick = viewModel::onInstallClick,
                        onSimilarDownloadClick = viewModel::onSimilarAppDownloadClick,
                        onShowcaseItemClick = onShowcaseItemClick,
                        onSimilarAppClick = onSimilarAppClick
                    )
                }
            }
        }
    }
}

@Composable
fun AppDetailContent(
    content: AppDetailUiState.Content,
    onBackClick: () -> Unit,
    onInstallClick: () -> Unit,
    onSimilarDownloadClick: (String) -> Unit,
    onShowcaseItemClick: (AppItem) -> Unit,
    onSimilarAppClick: (AppItem) -> Unit
) {
    val app = content.app
    val otherApps = content.allApps.filter { it.id != app.id }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp)
    ) {
        // 1. Верхняя панель
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.action_back),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = stringResource(R.string.action_share),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        // 2. Шапка приложения с тремя характеристиками (Возраст, Размер, Рейтинг)
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.Top
        ) {
            AsyncImage(
                model = app.iconUrl.ifEmpty { app.iconRes },
                contentDescription = stringResource(R.string.app_logo_desc),
                placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                error = painterResource(id = R.drawable.ic_launcher_foreground),
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(22.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = app.category,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = app.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = app.developer,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Возраст, Размер и Рейтинг
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = app.ageRating,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = stringResource(R.string.label_age),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = app.size,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = stringResource(R.string.label_size),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = if (app.rating != null) String.format(Locale.getDefault(), "%.1f", app.rating) else "—",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Text(
                            text = stringResource(R.string.label_rating),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 3. Кнопка «Установить» / «Отменить» / «Открыть»
        InstallButton(
            installState = content.installState,
            onClick = onInstallClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 4. Блок «Вам может понравиться» с работающими кнопками скачивания
        if (otherApps.isNotEmpty()) {
            SimilarAppsBlock(
                apps = otherApps.take(6),
                similarInstallStates = content.similarInstallStates,
                onAppClick = onSimilarAppClick,
                onDownloadClick = onSimilarDownloadClick
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        // 5. Блок «Скриншоты»
        Text(
            text = stringResource(R.string.section_screenshots),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(3) {
                Box(
                    modifier = Modifier
                        .width(260.dp)
                        .height(145.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                ) {
                    AsyncImage(
                        model = app.iconUrl.ifEmpty { app.iconRes },
                        contentDescription = null,
                        placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                        error = painterResource(id = R.drawable.ic_launcher_foreground),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 6. Описание
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = stringResource(R.string.section_description),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = app.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 7. Витрина
        if (otherApps.isNotEmpty()) {
            ShowcaseBlock(
                showcaseApps = otherApps.take(9),
                onItemClick = onShowcaseItemClick
            )
        }
    }
}

@Composable
fun InstallButton(
    installState: InstallState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (installState) {
        is InstallState.NotInstalled -> {
            Button(
                onClick = onClick,
                modifier = modifier.height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = stringResource(R.string.btn_install),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        is InstallState.Installing -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier.height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(
                    text = stringResource(R.string.btn_cancel_progress, installState.progress),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        is InstallState.Installed -> {
            Button(
                onClick = onClick,
                modifier = modifier.height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Text(
                    text = stringResource(R.string.btn_open),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SimilarAppsBlock(
    apps: List<AppItem>,
    similarInstallStates: Map<String, InstallState>,
    onAppClick: (AppItem) -> Unit,
    onDownloadClick: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.section_similar_apps),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(apps, key = { it.id }) { item ->
                SimilarAppCard(
                    app = item,
                    installState = similarInstallStates[item.id] ?: InstallState.NotInstalled,
                    onAppClick = { onAppClick(item) },
                    onDownloadClick = { onDownloadClick(item.id) }
                )
            }
        }
    }
}

@Composable
fun SimilarAppCard(
    app: AppItem,
    installState: InstallState,
    onAppClick: () -> Unit,
    onDownloadClick: () -> Unit
) {
    Column(
        modifier = Modifier.width(110.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Клик только по иконке и названию вызывает переход
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onAppClick() },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = app.iconUrl.ifEmpty { app.iconRes },
                contentDescription = app.name,
                placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                error = painterResource(id = R.drawable.ic_launcher_foreground),
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = app.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 15.sp
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Кнопка Скачать / Прогресс / Открыть
        when (installState) {
            is InstallState.NotInstalled -> {
                Button(
                    onClick = onDownloadClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    contentPadding = PaddingValues(vertical = 0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                ) {
                    Text(
                        text = stringResource(R.string.btn_download),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            is InstallState.Installing -> {
                OutlinedButton(
                    onClick = onDownloadClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    contentPadding = PaddingValues(vertical = 0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                ) {
                    Text(
                        text = "${installState.progress}%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            is InstallState.Installed -> {
                Button(
                    onClick = onDownloadClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    contentPadding = PaddingValues(vertical = 0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                ) {
                    Text(
                        text = stringResource(R.string.btn_open),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowcaseBlock(
    showcaseApps: List<AppItem>,
    onItemClick: (AppItem) -> Unit
) {
    val pages = showcaseApps.chunked(3)
    val pagerState = rememberPagerState(pageCount = { pages.size })

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.section_showcase),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 12.dp
        ) { pageIndex ->
            val pageApps = pages[pageIndex]

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                pageApps.forEach { app ->
                    AppItemCard(
                        app = app,
                        onClick = { onItemClick(app) }
                    )
                }
            }
        }
    }
}