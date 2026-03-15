package com.popop.lifesimulator.ui.screens.locations

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.popop.lifesimulator.data.models.world.Location
import com.popop.lifesimulator.data.models.world.LocationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsScreen(
    locations: List<Location>,
    currentLocationId: Long?,
    onBack: () -> Unit,
    onTravel: (Location) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf<LocationType?>(null) }
    
    val filteredLocations = locations.filter { location ->
        val matchesSearch = location.name.contains(searchQuery, ignoreCase = true) ||
                           location.description.contains(searchQuery, ignoreCase = true)
        val matchesType = selectedType == null || location.type == selectedType
        matchesSearch && matchesType
    }
    
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Locations") },
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
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search locations") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Default.LocationOn, contentDescription = null)
                }
            )
            
            // Type Filter
            LocationTypeFilter(
                selectedType = selectedType,
                onTypeSelected = { selectedType = it },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            // Locations List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredLocations, key = { it.id }) { location ->
                    LocationCard(
                        location = location,
                        isCurrentLocation = location.id == currentLocationId,
                        onTravel = { onTravel(location) }
                    )
                }
                
                if (filteredLocations.isEmpty()) {
                    item {
                        EmptyLocationsMessage(searchQuery.isNotEmpty() || selectedType != null)
                    }
                }
            }
        }
    }
}

@Composable
private fun LocationTypeFilter(
    selectedType: LocationType?,
    onTypeSelected: (LocationType?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedType == null,
            onClick = { onTypeSelected(null) },
            label = { Text("All") },
            modifier = Modifier.weight(1f)
        )
        
        LocationType.entries.take(5).forEach { type ->
            FilterChip(
                selected = selectedType == type,
                onClick = { onTypeSelected(type) },
                label = { Text(type.displayName) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun LocationCard(
    location: Location,
    isCurrentLocation: Boolean,
    onTravel: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { if (!isCurrentLocation) onTravel() },
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrentLocation) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Location Icon
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
            
            // Location Info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = location.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    if (isCurrentLocation) {
                        AssistChip(
                            onClick = { },
                            label = { Text("Current") },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                
                Text(
                    text = location.type.displayName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                if (location.description.isNotBlank()) {
                    Text(
                        text = location.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        maxLines = 2
                    )
                }
                
                // Location Properties
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (location.securityLevel > 0) {
                        PropertyChip(
                            label = "Security",
                            value = location.securityLevel.toString()
                        )
                    }
                    if (location.quality != 50) {
                        PropertyChip(
                            label = "Quality",
                            value = location.quality.toString()
                        )
                    }
                    if (location.entryCost > 0) {
                        PropertyChip(
                            label = "Entry",
                            value = "$${location.entryCost}"
                        )
                    }
                }
            }
            
            // Travel Button
            if (!isCurrentLocation && location.isAccessible) {
                Button(
                    onClick = onTravel,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Travel")
                }
            } else if (!location.isAccessible) {
                AssistChip(
                    onClick = { },
                    label = { Text("Locked") },
                    enabled = false
                )
            }
        }
    }
}

@Composable
private fun PropertyChip(label: String, value: String) {
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
                text = value,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EmptyLocationsMessage(hasFilter: Boolean) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
            Text(
                text = if (hasFilter) "No locations match your filters" else "No locations available",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}
