package com.example.flashcardandroid

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun FlashcardScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    viewModel: FlashcardViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { viewModel.flashcardList.size })
    LaunchedEffect(true) { viewModel.loadCards() }

    Scaffold {
        innerPadding ->
        FlashcardScreenBody(
            pagerState = pagerState,
            modifier = modifier.padding(
                start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                top = innerPadding.calculateTopPadding(),
                end = innerPadding.calculateEndPadding(LocalLayoutDirection.current)
            ).navigationBarsPadding().fillMaxWidth().safeContentPadding(),
            flashcardList = viewModel.flashcardList,
            onClick = { index -> viewModel.flip(index) },
            navigateBack = navigateBack
        )
    }
}

@Composable
fun FlashcardScreenBody(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    flashcardList: List<FlashcardDetails> = mutableListOf(),
    onClick: (Int) -> Unit = {},
    navigateBack: () -> Unit = {}
) {
    Column {
        FilledTonalButton(onClick = { navigateBack() }, modifier = modifier) { Text("Return") }
        CurrentCard(flashcardList, pagerState, onClick = { index: Int -> onClick(index) }, modifier = modifier)
    }
}

@Composable
fun CurrentCard(
    flashcardList: List<FlashcardDetails>,
    state: PagerState,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit = {}
) {
    HorizontalPager(state = state) { page ->
        Card(
            onClick = { onClick(page) },
            modifier = modifier.size(width = 100.dp, height = 300.dp).padding(20.dp)
        ) {
            Text(
                flashcardList[page].frontText,
                modifier = Modifier.fillMaxSize().padding(16.dp).wrapContentSize(align = Alignment.Center),
                textAlign = TextAlign.Center,
                fontSize = 30.sp
            )
        }

    }
}

@Preview
@Composable
fun FlashcardScreenPreview() {
    //FlashcardScreenBody(flashcardList = listOf(FlashcardDetails(frontText = "Test", backText = "Details!!")))
}