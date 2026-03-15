package com.popop.lifesimulator.core.navigation

/**
 * Sealed class representing all navigation routes in the game.
 * Uses sealed class for type-safe navigation.
 */
sealed class Screen(val route: String) {
    
    // Main menu and setup screens
    object Splash : Screen("splash")
    object MainMenu : Screen("main_menu")
    object CharacterCreation : Screen("character_creation")
    object LoadGame : Screen("load_game")
    object Settings : Screen("settings")
    
    // Main game screens
    object GameScreen : Screen("game_screen")
    object CharacterSheet : Screen("character_sheet")
    object Inventory : Screen("inventory")
    object Relationships : Screen("relationships")
    object WorldMap : Screen("world_map")
    object Journal : Screen("journal")
    object QuestLog : Screen("quest_log")
    
    // Location-specific screens
    object Palace : Screen("palace")
    object Prison : Screen("prison")
    object Hospital : Screen("hospital")
    object Business : Screen("business")
    object Home : Screen("home")
    object School : Screen("school")
    object Government : Screen("government")
    object CriminalHideout : Screen("criminal_hideout")
    
    // Interaction screens
    object NpcInteraction : Screen("npc_interaction/{npcId}") {
        fun createRoute(npcId: String) = "npc_interaction/$npcId"
    }
    object FactionScreen : Screen("faction_screen/{factionId}") {
        fun createRoute(factionId: String) = "faction_screen/$factionId"
    }
    object LocationScreen : Screen("location_screen/{locationId}") {
        fun createRoute(locationId: String) = "location_screen/$locationId"
    }
    
    // Special screens
    object EventScreen : Screen("event_screen")
    object DialogScreen : Screen("dialog_screen")
    object GameOver : Screen("game_over")
    object VictoryScreen : Screen("victory_screen")
}

/**
 * Navigation actions that can be performed.
 */
sealed class NavigationAction {
    data class Navigate(val screen: Screen) : NavigationAction()
    data class NavigateAndClearBackStack(val screen: Screen) : NavigationAction()
    data class PopBack(val count: Int = 1) : NavigationAction()
    object PopToStart : NavigationAction()
}
