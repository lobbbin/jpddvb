package com.popop.lifesimulator.ui.screens.factions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.popop.lifesimulator.data.models.world.Faction
import com.popop.lifesimulator.data.models.world.FactionCategory
import com.popop.lifesimulator.data.models.world.OpinionTier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FactionsScreen(
    factions: List<Faction>,
    onBack: () -> Unit,
    onFactionClick: (Faction) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf<FactionCategory?>(null) }
    
    val filteredFactions = selectedCategory?.let { category ->
        factions.filter { it.category == category }
    } ?: factions
    
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Factions") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Category Filter
            FactionCategoryFilter(
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it },
                modifier = Modifier.padding(16.dp)
            )
            
            // Factions List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredFactions, key = { it.id }) { faction ->
                    FactionCard(
                        faction = faction,
                        onClick = { onFactionClick(faction) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FactionCategoryFilter(
    selectedCategory: FactionCategory?,
    onCategorySelected: (FactionCategory?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedCategory == null,
            onClick = { onCategorySelected(null) },
            label = { Text("All") },
            modifier = Modifier.weight(1f)
        )
        
        FactionCategory.entries.forEach { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = { onCategorySelected(category) },
                label = { Text(category.displayName) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun FactionCard(
    faction: Faction,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = getOpinionColor(faction.opinionOfPlayer)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Faction Icon
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                
                // Faction Info
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = faction.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Text(
                        text = faction.category.displayName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Opinion Badge
                OpinionBadge(faction.opinionOfPlayer)
            }
            
            // Description
            if (faction.description.isNotBlank()) {
                Text(
                    text = faction.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FactionStat("Power", faction.powerLevel.toString())
                FactionStat("Wealth", formatWealth(faction.wealth))
                FactionStat("Members", faction.memberCount.toString())
                FactionStat("Territory", "${faction.territoryControl}%")
            }
            
            // Player Status
            Divider(modifier = Modifier.padding(vertical = 4.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Your Rank: ${faction.playerRank}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
                
                if (faction.isPlayerMember) {
                    AssistChip(
                        onClick = { },
                        label = { Text("Member") },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun OpinionBadge(opinion: Int) {
    val tier = getOpinionTier(opinion)
    val color = getOpinionColor(opinion)
    
    Surface(
        color = color,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = tier.displayName,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun FactionStat(label: String, value: String) {
    Column {
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

@Composable
private fun getOpinionColor(opinion: Int): androidx.compose.ui.graphics.Color {
    return when {
        opinion >= 80 -> MaterialTheme.colorScheme.primaryContainer
        opinion >= 50 -> MaterialTheme.colorScheme.tertiaryContainer
        opinion >= 20 -> MaterialTheme.colorScheme.secondaryContainer
        opinion >= -20 -> MaterialTheme.colorScheme.surface
        opinion >= -50 -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
        else -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
    }
}

@Composable
private fun getOpinionTier(opinion: Int): OpinionTier {
    return when {
        opinion >= 80 -> OpinionTier.REVERED
        opinion >= 50 -> OpinionTier.TRUSTED
        opinion >= 20 -> OpinionTier.LIKED
        opinion >= -20 -> OpinionTier.NEUTRAL
        opinion >= -50 -> OpinionTier.DISLIKED
        opinion >= -80 -> OpinionTier.DISTRUSTED
        else -> OpinionTier.HATED
    }
}

private fun formatWealth(wealth: Double): String {
    return when {
        wealth >= 1000000 -> "$${wealth.toInt() / 1000000}M"
        wealth >= 1000 -> "$${wealth.toInt() / 1000}K"
        else -> "$${wealth.toInt()}"
    }
}
