package com.popop.lifesimulator.ui.screens.charactercreation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.popop.lifesimulator.data.models.character.Gender

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterCreationScreen(
    onStartGame: (Long) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf(Gender.MALE) }
    var selectedAge by remember { mutableStateOf(18) }
    var selectedLifePath by remember { mutableStateOf<LifePathOption?>(null) }
    
    val canCreate = firstName.isNotBlank() && lastName.isNotBlank() && selectedLifePath != null
    
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Create Character") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Name Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Name",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    OutlinedTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = { Text("First Name") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                        singleLine = true
                    )
                    
                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = { Text("Last Name") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                        singleLine = true
                    )
                }
            }
            
            // Gender Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Gender",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Gender.entries.forEach { gender ->
                            FilterChip(
                                selected = selectedGender == gender,
                                onClick = { selectedGender = gender },
                                label = { Text(gender.displayName) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
            
            // Age Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Age: $selectedAge",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Slider(
                        value = selectedAge.toFloat(),
                        onValueChange = { selectedAge = it.toInt() },
                        valueRange = 16f..60f,
                        steps = 44,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("16", style = MaterialTheme.typography.bodySmall)
                        Text("30", style = MaterialTheme.typography.bodySmall)
                        Text("45", style = MaterialTheme.typography.bodySmall)
                        Text("60", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            
            // Life Path Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Choose Your Life Path",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Text(
                        text = "Your life path determines your starting point and available opportunities",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    
                    LifePathOption.entries.forEach { path ->
                        LifePathCard(
                            option = path,
                            selected = selectedLifePath == path,
                            onClick = { selectedLifePath = path }
                        )
                    }
                }
            }
            
            // Starting Wealth Display
            if (selectedLifePath != null) {
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
                            text = "Starting Wealth",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "$${selectedLifePath!!.startingWealth}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
            
            // Create Button
            Button(
                onClick = { /* Create character and start game */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = canCreate
            ) {
                Text(
                    text = "Start Game",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun LifePathCard(
    option: LifePathOption,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (selected) 4.dp else 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = option.icon,
                    style = MaterialTheme.typography.headlineSmall
                )
                Column {
                    Text(
                        text = option.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = if (selected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    )
                    Text(
                        text = option.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

enum class LifePathOption(
    val displayName: String,
    val description: String,
    val startingWealth: Double,
    val icon: String
) {
    ROYALTY(
        "Royalty",
        "Born into nobility, you start with power and privilege",
        100000.0,
        "👑"
    ),
    POLITICS(
        "Politics",
        "Begin your journey in public service",
        50000.0,
        "🏛️"
    ),
    CRIME(
        "Crime",
        "Start on the streets and build your empire",
        5000.0,
        "🔪"
    ),
    BUSINESS(
        "Business",
        "Entrepreneur with a dream and capital",
        75000.0,
        "💼"
    ),
    CAREER(
        "Career",
        "Professional starting your career path",
        20000.0,
        "📚"
    )
}
