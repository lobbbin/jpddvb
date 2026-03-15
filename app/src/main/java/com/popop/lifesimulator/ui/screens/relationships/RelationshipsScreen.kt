package com.popop.lifesimulator.ui.screens.relationships

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.popop.lifesimulator.data.models.relationship.Relationship
import com.popop.lifesimulator.data.models.relationship.RelationshipCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RelationshipsScreen(
    relationships: List<Relationship>,
    onBack: () -> Unit,
    onRelationshipClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf<RelationshipCategory?>(null) }
    
    val filteredRelationships = selectedCategory?.let { category ->
        relationships.filter { it.relationshipType.category == category }
    } ?: relationships
    
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Relationships") },
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
            CategoryFilterRow(
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )
            
            // Relationships List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredRelationships, key = { it.id }) { relationship ->
                    RelationshipCard(
                        relationship = relationship,
                        onClick = { onRelationshipClick(relationship.npcId) }
                    )
                }
                
                if (filteredRelationships.isEmpty()) {
                    item {
                        EmptyRelationshipsMessage(selectedCategory)
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryFilterRow(
    selectedCategory: RelationshipCategory?,
    onCategorySelected: (RelationshipCategory?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedCategory == null,
            onClick = { onCategorySelected(null) },
            label = { Text("All") },
            modifier = Modifier.weight(1f)
        )
        
        RelationshipCategory.entries.take(4).forEach { category ->
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
private fun RelationshipCard(
    relationship: Relationship,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = getRelationshipColor(relationship.relationshipScore)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Relationship Icon
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
            
            // Relationship Info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = relationship.relationshipType.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                Text(
                    text = relationship.getRelationshipStatus().displayName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                // Metrics Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricChip("Trust", relationship.trust)
                    MetricChip("Respect", relationship.respect)
                    MetricChip("Loyalty", relationship.loyalty)
                }
            }
            
            // Score Indicator
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = if (relationship.relationshipScore > 0) Color(0xFFE53E3E) else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "${relationship.relationshipScore}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun MetricChip(label: String, value: Int) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "$value",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EmptyRelationshipsMessage(category: RelationshipCategory?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.People,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
            Text(
                text = category?.let { "No ${it.displayName} relationships yet" }
                    ?: "No relationships yet",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
            Text(
                text = "Build relationships through social interactions",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
private fun getRelationshipColor(score: Int): Color {
    return when {
        score >= 80 -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        score >= 50 -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
        score >= 20 -> MaterialTheme.colorScheme.surface
        score >= -20 -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
        else -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
    }
}
