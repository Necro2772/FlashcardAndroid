package com.example.flashcardandroid

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.coroutineContext

@Composable
fun FlashcardScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    viewModel: FlashcardViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { viewModel.flashcardList.size })
    LaunchedEffect(true) { viewModel.loadCards() }

    Scaffold (
        modifier = modifier,
        bottomBar = {
            FlashcardScreenFooter (
                modifier = Modifier.navigationBarsPadding(),
                navigateBack = navigateBack,
                flipAll = { viewModel.flipAll() },
                reshuffle = { viewModel.reshuffle() }
            )
        }
    ) {
        innerPadding ->
        FlashcardScreenBody(
            pagerState = pagerState,
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current)
                )
                .navigationBarsPadding()
                .safeContentPadding(),
            flashcardList = viewModel.flashcardList,
            onClick = { index -> viewModel.flip(index) },
            onRemove = { card ->
                viewModel.removeCardFromDeck(card)
                if (viewModel.flashcardList.isEmpty()) navigateBack()
            }
        )
    }
}

@Composable
fun FlashcardScreenFooter(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = {},
    flipAll: () -> Unit = {},
    reshuffle: () -> Unit = {}
) {
    Row (verticalAlignment = Alignment.Bottom, modifier = modifier) {
        OutlinedButton(
            onClick = { navigateBack() },
            modifier = Modifier.weight(0.5F).padding(horizontal = 2.dp)
        ) {
            Text("Return")
        }
        OutlinedButton(
            onClick = { flipAll() },
            modifier = Modifier.weight(0.5F).padding(horizontal = 2.dp)
        ) {
            Text("Flip all")
        }
        FilledTonalButton(
            onClick = { reshuffle() },
            modifier = Modifier.weight(0.5f).padding(horizontal = 2.dp)
        ) {
            Text("Reshuffle")
        }
    }
}

@Composable
fun FlashcardScreenBody(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    flashcardList: List<FlashcardDetails> = mutableListOf(),
    onClick: (Int) -> Unit = {},
    onRemove: (FlashcardDetails) -> Unit = {}
) {
    Column (modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        CurrentCard(
            flashcardList,
            pagerState,
            onClick = { index: Int -> onClick(index) },
            modifier = Modifier.wrapContentHeight(),
            onRemove = { card: FlashcardDetails -> onRemove(card) }
        )
    }
}

@Composable
fun CurrentCard(
    flashcardList: List<FlashcardDetails>,
    state: PagerState,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit = {},
    onRemove: (FlashcardDetails) -> Unit = {}
) {
    Column (modifier = modifier) {
        if (flashcardList.isNotEmpty()) {
            Text(
                "${state.currentPage + 1}/${state.pageCount}",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        HorizontalPager(state = state) { page ->
            Card(
                onClick = { onClick(page) },
                modifier = Modifier
                    .height(height = 350.dp)
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Box {
                    IconButton(
                        onClick = { onRemove(flashcardList[page]) },
                        modifier = Modifier.padding(horizontal = 4.dp).align(alignment = Alignment.TopEnd)
                    ) {
                        Icon(Icons.Outlined.Delete, "Discard")
                    }
                    Text(
                        flashcardList[page].frontText,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .wrapContentSize(align = Alignment.Center),
                        textAlign = TextAlign.Center,
                        fontSize = 30.sp,
                        lineHeight = 35.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FlashcardScreenPreview() {
    FlashcardScreenBody(flashcardList = listOf(FlashcardDetails(frontText = "Test\ntest", backText = "Details!!")), pagerState = rememberPagerState( pageCount = { 2 }))
}