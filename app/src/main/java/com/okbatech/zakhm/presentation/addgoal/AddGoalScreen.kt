package com.okbatech.zakhm.presentation.addgoal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.okbatech.zakhm.domain.model.FrequencyType
import com.okbatech.zakhm.domain.model.GoalType

private val GOAL_COLORS = listOf("#00E676", "#00C853", "#26C6DA", "#40C4FF", "#FFAB40", "#64FFDA", "#FF4081", "#FF6D00")
private val EMOJIS = listOf("🎯", "💰", "💪", "🏃", "📚", "🧘", "🔥", "⭐", "🏆", "🎮", "🎨", "🌱", "❤️", "✈️", "🎸")

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddGoalScreen(
    viewModel: AddGoalViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                AddGoalEffect.GoalCreated, AddGoalEffect.NavigateBack -> onBack()
            }
        }
    }

    val primary = MaterialTheme.colorScheme.primary
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    val surfaceContainerHigh = MaterialTheme.colorScheme.surfaceContainerHigh
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    val tertiary = MaterialTheme.colorScheme.tertiary

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Goal", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SectionLabel("Goal Type")
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                GoalType.entries.forEach { type ->
                    val selected = state.type == type
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selected) primary else surfaceVariant)
                            .border(1.dp, if (selected) primary else surfaceContainerHigh, RoundedCornerShape(12.dp))
                            .clickable { viewModel.onEvent(AddGoalEvent.TypeSelected(type)) }
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text(
                            "${type.emoji} ${type.label}",
                            style = MaterialTheme.typography.labelLarge,
                            color = if (selected) onPrimary else onSurfaceVariant
                        )
                    }
                }
            }

            SectionLabel("Title")
            OutlinedTextField(
                value = state.title,
                onValueChange = { viewModel.onEvent(AddGoalEvent.TitleChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("e.g. Save for a new laptop", color = onSurfaceVariant) },
                isError = state.titleError != null,
                supportingText = state.titleError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                colors = outlinedColors(primary, surfaceContainerHigh, surfaceVariant),
                shape = RoundedCornerShape(12.dp)
            )

            SectionLabel("Description (optional)")
            OutlinedTextField(
                value = state.description,
                onValueChange = { viewModel.onEvent(AddGoalEvent.DescriptionChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("What's your motivation?", color = onSurfaceVariant) },
                minLines = 2,
                colors = outlinedColors(primary, surfaceContainerHigh, surfaceVariant),
                shape = RoundedCornerShape(12.dp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    SectionLabel("Target")
                    OutlinedTextField(
                        value = state.targetValue,
                        onValueChange = { viewModel.onEvent(AddGoalEvent.TargetValueChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = state.targetError != null,
                        supportingText = state.targetError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                        colors = outlinedColors(primary, surfaceContainerHigh, surfaceVariant),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    SectionLabel("Unit")
                    OutlinedTextField(
                        value = state.unit,
                        onValueChange = { viewModel.onEvent(AddGoalEvent.UnitChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("times, $, km…", color = onSurfaceVariant) },
                        colors = outlinedColors(primary, surfaceContainerHigh, surfaceVariant),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            SectionLabel("Frequency")
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FrequencyType.entries.forEach { freq ->
                    val selected = state.frequencyType == freq
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(if (selected) primary else surfaceVariant)
                            .border(1.dp, if (selected) primary else surfaceContainerHigh, RoundedCornerShape(50))
                            .clickable { viewModel.onEvent(AddGoalEvent.FrequencySelected(freq)) }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            freq.label,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (selected) onPrimary else onSurfaceVariant
                        )
                    }
                }
            }

            SectionLabel("Emoji")
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                EMOJIS.forEach { emoji ->
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(if (state.emoji == emoji) primary.copy(alpha = 0.2f) else surfaceVariant)
                            .border(
                                1.dp,
                                if (state.emoji == emoji) primary else surfaceContainerHigh,
                                CircleShape
                            )
                            .clickable { viewModel.onEvent(AddGoalEvent.EmojiSelected(emoji)) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(emoji, style = MaterialTheme.typography.titleMedium)
                    }
                }
            }

            SectionLabel("Color")
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                GOAL_COLORS.forEach { hex ->
                    val color = runCatching { Color(android.graphics.Color.parseColor(hex)) }.getOrElse { primary }
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                3.dp,
                                if (state.colorHex == hex) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                                CircleShape
                            )
                            .clickable { viewModel.onEvent(AddGoalEvent.ColorSelected(hex)) }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(surfaceVariant)
                    .padding(12.dp)
            ) {
                Column {
                    Text("🏆 Completion Reward", style = MaterialTheme.typography.labelLarge, color = tertiary)
                    Text(
                        "+${xpRewardForType(state.type)} XP when you complete this goal",
                        style = MaterialTheme.typography.bodySmall,
                        color = onSurfaceVariant,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            Button(
                onClick = { viewModel.onEvent(AddGoalEvent.Submit) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !state.isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = primary),
                shape = RoundedCornerShape(14.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = onPrimary, strokeWidth = 2.dp)
                } else {
                    Text("Create Goal 🚀", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun outlinedColors(
    primary: Color,
    surfaceContainerHigh: Color,
    surfaceVariant: Color
) = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = primary,
    unfocusedBorderColor = surfaceContainerHigh,
    focusedTextColor = MaterialTheme.colorScheme.onSurface,
    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
    cursorColor = primary,
    unfocusedContainerColor = surfaceVariant,
    focusedContainerColor = surfaceVariant
)

private fun xpRewardForType(type: GoalType) = when (type) {
    GoalType.FINANCIAL -> 300
    GoalType.FITNESS -> 200
    GoalType.HABIT -> 150
    GoalType.LEARNING -> 200
    GoalType.MINDFULNESS -> 150
    GoalType.CUSTOM -> 100
}
