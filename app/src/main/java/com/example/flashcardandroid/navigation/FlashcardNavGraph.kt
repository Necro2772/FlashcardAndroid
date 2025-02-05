package com.example.flashcardandroid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.flashcardandroid.FlashcardInputDestination
import com.example.flashcardandroid.FlashcardInputScreen
import com.example.flashcardandroid.FlashcardScreen
import kotlinx.serialization.Serializable

@Serializable
data class Flashcards(val flashcardList: List<Int>)

@Composable
fun FlashcardNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = FlashcardInputDestination.route,
        modifier = modifier
    ) {
        composable(route = FlashcardInputDestination.route) {
            FlashcardInputScreen(
                navigateToFlashcardView = {
                        cards -> navController.navigate(Flashcards(flashcardList = cards))
                }
            )
        }
        composable<Flashcards> { FlashcardScreen( navigateBack = { navController.popBackStack() }) }
    }
}
