package com.example.flashcardandroid

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@Composable
fun FlashcardScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    viewModel: FlashcardViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()


    Scaffold {
        innerPadding ->
        FlashcardScreenBody(modifier = modifier.padding(
            start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
            top = innerPadding.calculateTopPadding(),
            end = innerPadding.calculateEndPadding(LocalLayoutDirection.current)
        ).navigationBarsPadding().fillMaxWidth().safeContentPadding(),
            flashcardList = viewModel.flashcardList,
            navigateBack = navigateBack,
            onClick = {
                coroutineScope.launch {
                    viewModel.loadCards()
                } }
        )
    }
    Text(viewModel.args.toString())
}

@Composable
fun FlashcardScreenBody(
    modifier: Modifier,
    flashcardList: List<FlashcardDetails> = mutableListOf(),
    navigateBack: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    Column {
        FilledTonalButton(onClick = { navigateBack() }, modifier = modifier) { Text("Return") }
        Text(flashcardList.toString())
        CurrentCard(flashcardList[0])
        Button(onClick = onClick) { Text("Reload") }
    }
}

@Composable
fun CurrentCard(flashcardDetails: FlashcardDetails) {
    Text(flashcardDetails.frontText)
}