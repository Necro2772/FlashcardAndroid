package com.example.flashcardandroid

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcardandroid.navigation.NavigationDestination
import kotlinx.coroutines.launch

object FlashcardInputDestination: NavigationDestination {
    override val route = "flashcard_input"
    override val titleRes = R.string.flashcard_input_title
}

@Composable
fun FlashcardInputScreen(
    navigateToFlashcardView: (List<Int>) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FlashcardInputViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(true) { viewModel.loadAllCards() }
    Scaffold (modifier = modifier) {
        innerPadding ->
        FlashcardInputBody(modifier = Modifier
            .padding(
                start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                top = innerPadding.calculateTopPadding(),
                end = innerPadding.calculateEndPadding(LocalLayoutDirection.current)
            )
            .navigationBarsPadding()
            .fillMaxWidth()
            .safeContentPadding()
            .imePadding(),
            viewModel.flashcardUiState.flashcardDetails,
            viewModel::updateUiState,
            flashcards = viewModel.flashcardList.reversed(),
            isCardValid = viewModel.isCardValid,
            onSave = {
                coroutineScope.launch {
                    viewModel.saveCard()
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
            },
            onToggleEnabled = { card ->
                coroutineScope.launch {
                    viewModel.onToggleEnabled(card)
                }
            },
            navigateToFlashcardView = { navigateToFlashcardView(viewModel.getCardDeck()) }
        )
    }
}

@Composable
private fun FlashcardInputBody(
    modifier: Modifier = Modifier,
    flashcardDetails: FlashcardDetails = FlashcardDetails(),
    onValueChange: (FlashcardDetails) -> Unit = {},
    flashcards: List<FlashcardDetails> = mutableStateListOf(),
    isCardValid: Boolean = false,
    onSave: () -> Unit = {},
    onRemove: () -> Unit = {},
    onEdit: () -> Unit = {},
    onToggleEnabled: (FlashcardDetails) -> Unit = {},
    navigateToFlashcardView: () -> Unit = {}
) {
    val buttonModifier = Modifier.padding(horizontal = 2.dp)
    Column (modifier = modifier) {
        InputFlashcard(flashcardDetails, onValueChange =  onValueChange)
        Button(
            onClick = { navigateToFlashcardView() },
            buttonModifier.fillMaxWidth()
        ) {
            Text("Shuffle Flashcards!")
        }

        if (flashcardDetails.uid == 0) {
            Row (modifier = buttonModifier) {
                FilledTonalButton(
                    onClick = { onSave() },
                    buttonModifier.weight(0.5F),
                    enabled = isCardValid
                ) {
                    Text("Add Card")
                }
            }
        } else {
            Row (modifier = buttonModifier) {
                FilledTonalButton(
                    onClick = { onEdit() },
                    buttonModifier.weight(0.3F).padding(1.dp),
                    enabled = isCardValid
                ) {
                    Text("Confirm")
                }
                FilledTonalButton(
                    onClick = { onRemove() },
                    buttonModifier.weight(0.3F).padding(1.dp)
                ) {
                    Text("Delete")
                }
                OutlinedButton(
                    onClick = { onValueChange(FlashcardDetails()) },
                    buttonModifier.weight(0.3F).padding(1.dp)
                ) {
                    Text("Back")
                }
            }
        }
        FlashcardList(flashcards, onClick = { current -> onValueChange(current)}, onToggleEnabled = { card: FlashcardDetails -> onToggleEnabled(card) })
    }
}

@Composable
private fun InputFlashcard(
    flashcardDetails: FlashcardDetails,
    modifier: Modifier = Modifier,
    onValueChange: (FlashcardDetails) -> Unit = {}) {
    Column (modifier = modifier) {
        TextField(
            value = flashcardDetails.frontText,
            onValueChange = { onValueChange(flashcardDetails.copy(frontText = it)) },
            label = { Text("Front Side") },
            modifier =  Modifier.fillMaxWidth()
        )
        TextField(
            value = flashcardDetails.backText,
            onValueChange = { onValueChange(flashcardDetails.copy(backText = it)) },
            label = { Text("Reverse Side")},
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun FlashcardList(
    flashcards: List<FlashcardDetails>,
    modifier: Modifier = Modifier,
    onClick: (FlashcardDetails) -> Unit = {},
    onToggleEnabled: (FlashcardDetails) -> Unit = {}
) {
    Row (modifier = modifier) {
        LazyColumn {
            items(flashcards) {
                flashcard ->
                Column (modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Row (modifier = Modifier.clickable { onClick(flashcard) },
                        verticalAlignment = Alignment.CenterVertically) {
                        val color: Color
                        if (!flashcard.isEnabled()) {
                            color = TextFieldDefaults.colors().disabledLabelColor
                        } else {
                            color = TextFieldDefaults.colors().focusedTextColor
                        }

                        Text(flashcard.frontText, modifier = Modifier.weight(0.5F), color = color, textAlign = TextAlign.Center)
                        Text(flashcard.backText, modifier = Modifier.weight(0.5F), color = color, textAlign = TextAlign.Center)
                        IconToggleButton(
                            checked = flashcard.isEnabled(),
                            onCheckedChange = { onToggleEnabled(flashcard) },

                        ) {
                            if (flashcard.isEnabled()) {
                                Icon(
                                    painter = painterResource(R.drawable.toggle_on),
                                    contentDescription = "Toggled on",
                                    tint = TextFieldDefaults.colors().focusedTextColor
                                )
                            } else {
                                Icon(
                                    painter = painterResource(R.drawable.toggle_off),
                                    contentDescription = "Toggled off",
                                    tint = TextFieldDefaults.colors().disabledLabelColor
                                )
                            }
                        }
//                        Checkbox(
//                            modifier = Modifier.scale(0.9F),
//                            checked = flashcard.isEnabled(),
//                            onCheckedChange = { onToggleEnabled(flashcard) },
//                            colors = CheckboxDefaults.colors(checkedColor = CheckboxDefaults.colors().disabledCheckedBoxColor,
//                                uncheckedColor = CheckboxDefaults.colors().disabledCheckedBoxColor)
//                        )
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
    FlashcardInputBody(flashcards = listOf(
        FlashcardDetails(frontText = "fromt", backText = "back"),
        FlashcardDetails(frontText = "disabled", backText = "card", tags = listOf(disabledTag))
    ))
}