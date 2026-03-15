package com.popop.lifesimulator.ui.screens.inventory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.popop.lifesimulator.data.models.inventory.InventoryItem
import com.popop.lifesimulator.data.models.inventory.ItemCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    items: List<InventoryItem>,
    wealth: Double,
    onBack: () -> Unit,
    onItemUse: (InventoryItem) -> Unit,
    onItemDrop: (InventoryItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf<ItemCategory?>(null) }
    var showWealthDetails by remember { mutableStateOf(false) }
    
    val filteredItems = selectedCategory?.let { category ->
        items.filter { it.category == category }
    } ?: items
    
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Inventory & Assets") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showWealthDetails = true }) {
                        Icon(
                            imageVector = Icons.Default.Inventory,
                            contentDescription = "Wealth Details"
                        )
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
            // Wealth Summary
            WealthSummaryCard(
                wealth = wealth,
                itemCount = items.size,
                modifier = Modifier.padding(16.dp)
            )
            
            // Category Filter
            CategoryFilterChips(
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            // Items Grid
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 100.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredItems, key = { it.id }) { item ->
                    InventoryItemCard(
                        item = item,
                        onUse = { onItemUse(item) },
                        onDrop = { onItemDrop(item) }
                    )
                }
                
                if (filteredItems.isEmpty()) {
                    item {
                        EmptyInventoryMessage(selectedCategory)
                    }
                }
            }
        }
    }
}

@Composable
private fun WealthSummaryCard(
    wealth: Double,
    itemCount: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Wealth",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "$${String.format("%.2f", wealth)}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            
            Divider(modifier = Modifier.padding(vertical = 4.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Items",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = itemCount.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun CategoryFilterChips(
    selectedCategory: ItemCategory?,
    onCategorySelected: (ItemCategory?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedCategory == null,
            onClick = { onCategorySelected(null) },
            label = { Text("All") }
        )
        
        ItemCategory.entries.take(6).forEach { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = { onCategorySelected(category) },
                label = { Text(category.displayName) }
            )
        }
    }
}

@Composable
private fun InventoryItemCard(
    item: InventoryItem,
    onUse: () -> Unit,
    onDrop: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { },
        colors = CardDefaults.cardColors(
            containerColor = getItemCardColor(item.condition)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Item Name
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
            
            // Quantity and Value
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (item.quantity > 1) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = "x${item.quantity}",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                
                Text(
                    text = "$${String.format("%.0f", item.currentValue)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            
            // Condition Indicator
            LinearProgressIndicator(
                progress = getItemConditionProgress(item.condition),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                color = getConditionColor(item.condition),
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            
            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onUse,
                    modifier = Modifier.weight(1f),
                    enabled = item.category != com.popop.lifesimulator.data.models.inventory.ItemCategory.REAL_ESTATE &&
                            item.category != com.popop.lifesimulator.data.models.inventory.ItemCategory.VEHICLES,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Use",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                
                OutlinedButton(
                    onClick = onDrop,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Drop",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyInventoryMessage(category: ItemCategory?) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Inventory,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
            Text(
                text = category?.let { "No items in ${it.displayName}" } ?: "Inventory is empty",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
private fun getItemCardColor(condition: com.popop.lifesimulator.data.models.inventory.ItemCondition): androidx.compose.ui.graphics.Color {
    return when (condition) {
        com.popop.lifesimulator.data.models.inventory.ItemCondition.PRISTINE,
        com.popop.lifesimulator.data.models.inventory.ItemCondition.EXCELLENT -> MaterialTheme.colorScheme.surface
        com.popop.lifesimulator.data.models.inventory.ItemCondition.GOOD -> MaterialTheme.colorScheme.surface
        com.popop.lifesimulator.data.models.inventory.ItemCondition.WORN -> MaterialTheme.colorScheme.surfaceVariant
        com.popop.lifesimulator.data.models.inventory.ItemCondition.DAMAGED,
        com.popop.lifesimulator.data.models.inventory.ItemCondition.BROKEN -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
        com.popop.lifesimulator.data.models.inventory.ItemCondition.DESTROYED -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
    }
}

@Composable
private fun getConditionColor(condition: com.popop.lifesimulator.data.models.inventory.ItemCondition): androidx.compose.ui.graphics.Color {
    return when (condition) {
        com.popop.lifesimulator.data.models.inventory.ItemCondition.PRISTINE,
        com.popop.lifesimulator.data.models.inventory.ItemCondition.EXCELLENT -> MaterialTheme.colorScheme.primary
        com.popop.lifesimulator.data.models.inventory.ItemCondition.GOOD -> MaterialTheme.colorScheme.tertiary
        com.popop.lifesimulator.data.models.inventory.ItemCondition.WORN -> MaterialTheme.colorScheme.secondary
        com.popop.lifesimulator.data.models.inventory.ItemCondition.DAMAGED -> MaterialTheme.colorScheme.error
        com.popop.lifesimulator.data.models.inventory.ItemCondition.BROKEN,
        com.popop.lifesimulator.data.models.inventory.ItemCondition.DESTROYED -> MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
    }
}

@Composable
private fun getItemConditionProgress(condition: com.popop.lifesimulator.data.models.inventory.ItemCondition): Float {
    return when (condition) {
        com.popop.lifesimulator.data.models.inventory.ItemCondition.PRISTINE -> 1.0f
        com.popop.lifesimulator.data.models.inventory.ItemCondition.EXCELLENT -> 0.9f
        com.popop.lifesimulator.data.models.inventory.ItemCondition.GOOD -> 0.7f
        com.popop.lifesimulator.data.models.inventory.ItemCondition.WORN -> 0.5f
        com.popop.lifesimulator.data.models.inventory.ItemCondition.DAMAGED -> 0.3f
        com.popop.lifesimulator.data.models.inventory.ItemCondition.BROKEN -> 0.1f
        com.popop.lifesimulator.data.models.inventory.ItemCondition.DESTROYED -> 0.0f
    }
}
