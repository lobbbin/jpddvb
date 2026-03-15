package com.popop.lifesimulator.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object MainMenu : Screen("main_menu")
    object CharacterCreation : Screen("character_creation")
    object Game : Screen("game")
    object CharacterSheet : Screen("character_sheet")
    object Relationships : Screen("relationships")
    object Inventory : Screen("inventory")
    object Locations : Screen("locations")
    object Factions : Screen("factions")
    object Events : Screen("events")
    object Settings : Screen("settings")
    object LoadGame : Screen("load_game")
}

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: String
) {
    object Status : BottomNavItem("status", "Status", "face")
    object Actions : BottomNavItem("actions", "Actions", "play_arrow")
    object Relationships : BottomNavItem("relationships_nav", "Relationships", "people")
    object Assets : BottomNavItem("assets", "Assets", "account_balance")
    object More : BottomNavItem("more", "More", "more_horiz")
}
