package com.popop.lifesimulator.ui.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.popop.lifesimulator.ui.navigation.BottomNavItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    characterId: Long,
    onNavigateToCharacterSheet: () -> Unit,
    onNavigateToRelationships: () -> Unit,
    onNavigateToInventory: () -> Unit,
    onNavigateToLocations: () -> Unit,
    onNavigateToFactions: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            GameTopBar(
                onSettingsClick = onNavigateToSettings
            )
        },
        bottomBar = {
            GameBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                0 -> StatusTab()
                1 -> ActionsTab()
                2 -> RelationshipsTab(onNavigateToRelationships)
                3 -> AssetsTab(onNavigateToInventory)
                4 -> MoreTab(
                    onNavigateToLocations = onNavigateToLocations,
                    onNavigateToFactions = onNavigateToFactions,
                    onNavigateToCharacterSheet = onNavigateToCharacterSheet
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameTopBar(
    onSettingsClick: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = "Ultimate Life Simulator",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Day 1 • Monday • 8:00 AM",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        },
        actions = {
            IconButton(onClick = onSettingsClick) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Composable
private fun GameBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar {
        val items = listOf(
            BottomNavItem.Status,
            BottomNavItem.Actions,
            BottomNavItem.Relationships,
            BottomNavItem.Assets,
            BottomNavItem.More
        )
        
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = when (item.icon) {
                            "face" -> Icons.Default.Face
                            "play_arrow" -> Icons.Default.PlayArrow
                            "people" -> Icons.Default.People
                            "account_balance" -> Icons.Default.AccountBalance
                            else -> Icons.Default.MoreHoriz
                        },
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                selected = selectedTab == index,
                onClick = { onTabSelected(index) }
            )
        }
    }
}

@Composable
private fun StatusTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Character Info Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "John Doe",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatBadge(label = "Age", value = "18")
                    StatBadge(label = "Gender", value = "M")
                    StatBadge(label = "Path", value = "Career")
                }
            }
        }
        
        // Primary Stats Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Primary Stats",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                StatBar(label = "Health", value = 85, maxValue = 100, color = MaterialTheme.colorScheme.error)
                StatBar(label = "Energy", value = 70, maxValue = 100, color = MaterialTheme.colorScheme.tertiary)
                StatBar(label = "Stress", value = 25, maxValue = 100, color = MaterialTheme.colorScheme.primary)
            }
        }
        
        // Secondary Stats Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Attributes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                AttributeRow(label = "Charisma", value = 45)
                AttributeRow(label = "Intellect", value = 60)
                AttributeRow(label = "Cunning", value = 40)
                AttributeRow(label = "Violence", value = 30)
                AttributeRow(label = "Stealth", value = 35)
                AttributeRow(label = "Perception", value = 55)
                AttributeRow(label = "Willpower", value = 50)
            }
        }
        
        // Wealth Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Wealth",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "$20,000.00",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
        
        // Current Location Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Column {
                    Text(
                        text = "Current Location",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "Home - Apartment",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionsTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Actions",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        
        // Quick Actions
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Quick Actions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                ActionButton(
                    icon = Icons.Default.Rest,
                    title = "Rest",
                    subtitle = "Restore energy (-8 hours)",
                    onClick = { }
                )
                
                ActionButton(
                    icon = Icons.Default.FitnessCenter,
                    title = "Exercise",
                    subtitle = "Improve fitness (-20 energy, -5 stress)",
                    onClick = { }
                )
                
                ActionButton(
                    icon = Icons.Default.MenuBook,
                    title = "Study",
                    subtitle = "Gain skill experience (-30 energy)",
                    onClick = { }
                )
                
                ActionButton(
                    icon = Icons.Default.Work,
                    title = "Work",
                    subtitle = "Earn money (-40 energy, +10 stress)",
                    onClick = { }
                )
            }
        }
        
        // Location Actions
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Travel",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                ActionButton(
                    icon = Icons.Default.Store,
                    title = "Go to Store",
                    subtitle = "Buy items and supplies",
                    onClick = { }
                )
                
                ActionButton(
                    icon = Icons.Default.LocalHospital,
                    title = "Visit Hospital",
                    subtitle = "Treat injuries and illnesses",
                    onClick = { }
                )
                
                ActionButton(
                    icon = Icons.Default.Gavel,
                    title = "Go to Work",
                    subtitle = "Perform your job duties",
                    onClick = { }
                )
            }
        }
    }
}

@Composable
private fun RelationshipsTab(onNavigateToRelationships: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.People,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
            Text(
                text = "Relationships",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
            Button(onClick = onNavigateToRelationships) {
                Text("View All Relationships")
            }
        }
    }
}

@Composable
private fun AssetsTab(onNavigateToInventory: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AccountBalance,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
            Text(
                text = "Assets & Inventory",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
            Button(onClick = onNavigateToInventory) {
                Text("View Inventory")
            }
        }
    }
}

@Composable
private fun MoreTab(
    onNavigateToLocations: () -> Unit,
    onNavigateToFactions: () -> Unit,
    onNavigateToCharacterSheet: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "More Options",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        
        MoreOptionCard(
            icon = Icons.Default.Face,
            title = "Character Sheet",
            subtitle = "View detailed stats and skills",
            onClick = onNavigateToCharacterSheet
        )
        
        MoreOptionCard(
            icon = Icons.Default.LocationOn,
            title = "Locations",
            subtitle = "Browse and travel to locations",
            onClick = onNavigateToLocations
        )
        
        MoreOptionCard(
            icon = Icons.Default.People,
            title = "Factions",
            subtitle = "View faction relationships",
            onClick = onNavigateToFactions
        )
        
        MoreOptionCard(
            icon = Icons.Default.Event,
            title = "Event Log",
            subtitle = "View your action history",
            onClick = { }
        )
        
        MoreOptionCard(
            icon = Icons.Default.Save,
            title = "Save Game",
            subtitle = "Save your progress",
            onClick = { }
        )
    }
}

@Composable
private fun StatBar(
    label: String,
    value: Int,
    maxValue: Int,
    color: androidx.compose.ui.graphics.Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, style = MaterialTheme.typography.bodyMedium)
            Text("$value/$maxValue", style = MaterialTheme.typography.bodyMedium)
        }
        LinearProgressIndicator(
            progress = value.toFloat() / maxValue,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .padding(top = 4.dp),
            color = color,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
private fun AttributeRow(label: String, value: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun StatBadge(label: String, value: String) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.small
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun MoreOptionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}
