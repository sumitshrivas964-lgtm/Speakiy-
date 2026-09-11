package com.example.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.ConversationScreen
import com.example.ui.screens.NativeMatchScreen
import com.example.ui.screens.SavedRulesScreen
import com.example.ui.screens.VerifyAccountScreen

@Composable
fun MainScreen(
    viewModel: SpeakiyViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                NavigationBarItem(
                    selected = uiState.currentScreen == SpeakiyScreen.CONVERSATION,
                    onClick = { viewModel.setScreen(SpeakiyScreen.CONVERSATION) },
                    icon = { Icon(Icons.Default.ChatBubble, contentDescription = "Live Talk") },
                    label = { Text("Talk", fontSize = 12.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("nav_tab_talk")
                )

                NavigationBarItem(
                    selected = uiState.currentScreen == SpeakiyScreen.NATIVE_MATCH,
                    onClick = { viewModel.setScreen(SpeakiyScreen.NATIVE_MATCH) },
                    icon = { Icon(Icons.Default.People, contentDescription = "Native Match") },
                    label = { Text("Match", fontSize = 12.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("nav_tab_match")
                )

                NavigationBarItem(
                    selected = uiState.currentScreen == SpeakiyScreen.VERIFY_ACCOUNT,
                    onClick = { viewModel.setScreen(SpeakiyScreen.VERIFY_ACCOUNT) },
                    icon = { Icon(Icons.Default.VerifiedUser, contentDescription = "Verify Account") },
                    label = { Text("Verify", fontSize = 12.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("nav_tab_verify")
                )

                NavigationBarItem(
                    selected = uiState.currentScreen == SpeakiyScreen.SAVED_RULES,
                    onClick = { viewModel.setScreen(SpeakiyScreen.SAVED_RULES) },
                    icon = { Icon(Icons.Default.Bookmark, contentDescription = "Grammar Rules") },
                    label = { Text("Rules", fontSize = 12.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("nav_tab_rules")
                )
            }
        }
    ) { innerPadding ->
        when (uiState.currentScreen) {
            SpeakiyScreen.CONVERSATION -> {
                ConversationScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            SpeakiyScreen.NATIVE_MATCH -> {
                NativeMatchScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            SpeakiyScreen.VERIFY_ACCOUNT, SpeakiyScreen.PROFILE -> {
                VerifyAccountScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            SpeakiyScreen.SAVED_RULES -> {
                SavedRulesScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
