package com.example.flashcardandroid

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch


@Composable
fun FlashcardInputScreen(viewModel: FlashcardInputViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold {
        innerPadding ->
        FlashcardInputBody(modifier = Modifier.padding(
            start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
            top = innerPadding.calculateTopPadding(),
            end = innerPadding.calculateEndPadding(LocalLayoutDirection.current)
        ).fillMaxWidth(),
            viewModel.flashcardUiState.flashcardDetails,
            viewModel::updateUiState,
            flashcards = viewModel.flashcardList,
            onSave = {
                coroutineScope.launch {
                    viewModel.saveCard()
                }
            },
            onLoad = {
                coroutineScope.launch {
                    viewModel.loadAllCards()
                }
            },
            onEdit = {
                coroutineScope.launch {
                    viewModel.updateCard()
                }
            },
            onRemove = {
                coroutineScope.launch {
                    viewModel.deleteCard()
                }
            }
        )
    }
}

@Composable
private fun FlashcardInputBody(
    modifier: Modifier = Modifier,
    flashcardDetails: FlashcardDetails = FlashcardDetails(),
    onValueChange: (FlashcardDetails) -> Unit = {},
    flashcards: List<FlashcardDetails> = mutableStateListOf(),
    onSave: () -> Unit = {},
    onLoad: () -> Unit = {},
    onRemove: () -> Unit = {},
    onEdit: () -> Unit = {}
) {
    Column (modifier = modifier) {
        InputFlashcard(flashcardDetails, onValueChange)

        if (flashcardDetails.uid == 0) {
            Row {
                FilledTonalButton( onClick = { onSave() }) { Text("Add") }
                FilledTonalButton( onClick = { onLoad() }) { Text("Reload") }
            }
        } else {
            Row {
                FilledTonalButton( onClick = { onRemove() }) { Text("Remove") }
                FilledTonalButton( onClick = { onEdit() }) { Text("Edit") }
                FilledTonalButton( onClick = { onValueChange(FlashcardDetails()) }) { Text("Clear") }
            }
        }
        FlashcardList(flashcards, onClick = { current -> onValueChange(current)})
    }
}

@Composable
private fun InputFlashcard(flashcardDetails: FlashcardDetails, onValueChange: (FlashcardDetails) -> Unit = {}) {
    Column {
        TextField(
            value = flashcardDetails.frontText,
            onValueChange = { onValueChange(flashcardDetails.copy(frontText = it)) },
            label = { Text("Front Side") }
        )
        TextField(
            value = flashcardDetails.backText,
            onValueChange = { onValueChange(flashcardDetails.copy(backText = it)) },
            label = { Text("Reverse Side") }
        )
    }
}

@Composable
fun FlashcardList(flashcards: List<FlashcardDetails>, onClick: (FlashcardDetails) -> Unit = {}) {
    Row {
        LazyColumn {
            items(flashcards) {
                flashcard ->
                Column (modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Row (modifier = Modifier.clickable { onClick(flashcard) },
                        verticalAlignment = Alignment.CenterVertically) {
                        //Text(flashcard.uid.toString(), modifier = Modifier.weight(0.1F))
                        Text(flashcard.frontText, modifier = Modifier.weight(0.5F), textAlign = TextAlign.Center)
                        Text(flashcard.backText, modifier = Modifier.weight(0.5F), textAlign = TextAlign.Center)
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TestPreview() {
    FlashcardInputBody()
}