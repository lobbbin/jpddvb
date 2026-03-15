package com.popop.lifesimulator.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.popop.lifesimulator.ui.navigation.Screen
import com.popop.lifesimulator.ui.screens.mainmenu.MainMenuScreen
import com.popop.lifesimulator.ui.screens.charactercreation.CharacterCreationScreen
import com.popop.lifesimulator.ui.screens.game.GameScreen
import com.popop.lifesimulator.ui.screens.splash.SplashScreen

@Composable
fun LifeSimulatorApp() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToMainMenu = {
                    navController.navigate(Screen.MainMenu.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.MainMenu.route) {
            MainMenuScreen(
                onNewGame = {
                    navController.navigate(Screen.CharacterCreation.route)
                },
                onLoadGame = {
                    navController.navigate(Screen.LoadGame.route)
                },
                onSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        
        composable(Screen.CharacterCreation.route) {
            CharacterCreationScreen(
                onStartGame = { characterId ->
                    navController.navigate("${Screen.Game.route}/$characterId") {
                        popUpTo(Screen.MainMenu.route) { inclusive = true }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(
            route = "${Screen.Game.route}/{characterId}",
            arguments = listOf(
                androidx.navigation.navArgument("characterId") {
                    type = androidx.navigation.NavType.LongType
                }
            )
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getLong("characterId") ?: 0L
            GameScreen(
                characterId = characterId,
                onNavigateToCharacterSheet = {
                    navController.navigate(Screen.CharacterSheet.route)
                },
                onNavigateToRelationships = {
                    navController.navigate(Screen.Relationships.route)
                },
                onNavigateToInventory = {
                    navController.navigate(Screen.Inventory.route)
                },
                onNavigateToLocations = {
                    navController.navigate(Screen.Locations.route)
                },
                onNavigateToFactions = {
                    navController.navigate(Screen.Factions.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        
        composable(Screen.CharacterSheet.route) {
            // CharacterSheetScreen(
            //     onBack = { navController.popBackStack() }
            // )
        }
        
        composable(Screen.Relationships.route) {
            // RelationshipsScreen(
            //     onBack = { navController.popBackStack() }
            // )
        }
        
        composable(Screen.Inventory.route) {
            // InventoryScreen(
            //     onBack = { navController.popBackStack() }
            // )
        }
        
        composable(Screen.Locations.route) {
            // LocationsScreen(
            //     onBack = { navController.popBackStack() }
            // )
        }
        
        composable(Screen.Factions.route) {
            // FactionsScreen(
            //     onBack = { navController.popBackStack() }
            // )
        }
        
        composable(Screen.Settings.route) {
            // SettingsScreen(
            //     onBack = { navController.popBackStack() }
            // )
        }
        
        composable(Screen.LoadGame.route) {
            // LoadGameScreen(
            //     onLoadGame = { characterId ->
            //         navController.navigate("${Screen.Game.route}/$characterId") {
            //             popUpTo(Screen.MainMenu.route) { inclusive = true }
            //         }
            //     },
            //     onBack = { navController.popBackStack() }
            // )
        }
    }
}
