package com.popop.lifesimulator.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.popop.lifesimulator.ui.viewmodels.NavigationViewModel
import com.popop.lifesimulator.ui.screens.*

/**
 * Main navigation graph for the application.
 * Defines all composable destinations and their arguments.
 * 
 * @param navHostController The navigation controller
 * @param startDestination The initial destination
 */
@Composable
fun AppNavigationGraph(
    navHostController: NavHostController,
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {
        // Splash screen
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToMainMenu = {
                    navHostController.navigate(Screen.MainMenu.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        // Main menu
        composable(Screen.MainMenu.route) {
            MainMenuScreen(
                onNewGame = {
                    navHostController.navigate(Screen.CharacterCreation.route)
                },
                onLoadGame = {
                    navHostController.navigate(Screen.LoadGame.route)
                },
                onSettings = {
                    navHostController.navigate(Screen.Settings.route)
                }
            )
        }
        
        // Character creation
        composable(Screen.CharacterCreation.route) {
            CharacterCreationScreen(
                onComplete = { playerId ->
                    navHostController.navigate(Screen.GameScreen.route) {
                        popUpTo(Screen.CharacterCreation.route) { inclusive = true }
                    }
                }
            )
        }
        
        // Load game
        composable(Screen.LoadGame.route) {
            LoadGameScreen(
                onLoadGame = { saveId ->
                    navHostController.navigate(Screen.GameScreen.route) {
                        popUpTo(Screen.LoadGame.route) { inclusive = true }
                    }
                },
                onBack = {
                    navHostController.popBackStack()
                }
            )
        }
        
        // Settings
        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = {
                    navHostController.popBackStack()
                }
            )
        }
        
        // Main game screen
        composable(Screen.GameScreen.route) {
            GameScreen(
                onNavigateToCharacterSheet = {
                    navHostController.navigate(Screen.CharacterSheet.route)
                },
                onNavigateToInventory = {
                    navHostController.navigate(Screen.Inventory.route)
                },
                onNavigateToRelationships = {
                    navHostController.navigate(Screen.Relationships.route)
                },
                onNavigateToWorldMap = {
                    navHostController.navigate(Screen.WorldMap.route)
                },
                onNavigateToJournal = {
                    navHostController.navigate(Screen.Journal.route)
                }
            )
        }
        
        // Character sheet
        composable(Screen.CharacterSheet.route) {
            CharacterSheetScreen(
                onBack = {
                    navHostController.popBackStack()
                }
            )
        }
        
        // Inventory
        composable(Screen.Inventory.route) {
            InventoryScreen(
                onBack = {
                    navHostController.popBackStack()
                }
            )
        }
        
        // Relationships
        composable(Screen.Relationships.route) {
            RelationshipsScreen(
                onNpcSelected = { npcId ->
                    navHostController.navigate(Screen.NpcInteraction.createRoute(npcId))
                },
                onBack = {
                    navHostController.popBackStack()
                }
            )
        }
        
        // World map
        composable(Screen.WorldMap.route) {
            WorldMapScreen(
                onLocationSelected = { locationId ->
                    navHostController.navigate(Screen.LocationScreen.createRoute(locationId))
                },
                onBack = {
                    navHostController.popBackStack()
                }
            )
        }
        
        // Journal
        composable(Screen.Journal.route) {
            JournalScreen(
                onBack = {
                    navHostController.popBackStack()
                }
            )
        }
        
        // NPC Interaction - with argument
        composable(
            route = Screen.NpcInteraction.route,
            arguments = listOf(
                navArgument("npcId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val npcId = backStackEntry.arguments?.getString("npcId") ?: return@composable
            NpcInteractionScreen(
                npcId = npcId,
                onBack = {
                    navHostController.popBackStack()
                }
            )
        }
        
        // Faction screen - with argument
        composable(
            route = Screen.FactionScreen.route,
            arguments = listOf(
                navArgument("factionId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val factionId = backStackEntry.arguments?.getString("factionId") ?: return@composable
            FactionScreen(
                factionId = factionId,
                onBack = {
                    navHostController.popBackStack()
                }
            )
        }
        
        // Location screen - with argument
        composable(
            route = Screen.LocationScreen.route,
            arguments = listOf(
                navArgument("locationId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val locationId = backStackEntry.arguments?.getString("locationId") ?: return@composable
            LocationScreen(
                locationId = locationId,
                onBack = {
                    navHostController.popBackStack()
                }
            )
        }
        
        // Event screen
        composable(Screen.EventScreen.route) {
            EventScreen(
                onContinue = {
                    navHostController.popBackStack()
                }
            )
        }
        
        // Dialog screen
        composable(Screen.DialogScreen.route) {
            DialogScreen(
                onDialogComplete = {
                    navHostController.popBackStack()
                }
            )
        }
        
        // Game over
        composable(Screen.GameOver.route) {
            GameOverScreen(
                onReturnToMainMenu = {
                    navHostController.navigate(Screen.MainMenu.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        
        // Victory screen
        composable(Screen.VictoryScreen.route) {
            VictoryScreen(
                onReturnToMainMenu = {
                    navHostController.navigate(Screen.MainMenu.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
