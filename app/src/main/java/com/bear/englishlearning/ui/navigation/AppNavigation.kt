package com.bear.englishlearning.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bear.englishlearning.ui.MainViewModel
import com.bear.englishlearning.ui.screens.conversation.ConversationScreen
import com.bear.englishlearning.ui.screens.dailytask.DailyTaskScreen
import com.bear.englishlearning.ui.screens.listening.ListeningQuizScreen
import com.bear.englishlearning.ui.screens.realtimeconversation.RealTimeConversationScreen
import com.bear.englishlearning.ui.screens.memo.MemoListScreen
import com.bear.englishlearning.ui.screens.memo.MemoScreen
import com.bear.englishlearning.ui.screens.onboarding.OnboardingScreen
import com.bear.englishlearning.ui.screens.review.ReviewScreen
import com.bear.englishlearning.ui.screens.settings.SettingsScreen
import com.bear.englishlearning.ui.screens.translation.TranslationScreen
import com.bear.englishlearning.ui.screens.vocabulary.VocabularyScreen

sealed class Screen(val route: String, val label: String) {
    data object Onboarding : Screen("onboarding", "引導")
    data object DailyTask : Screen("daily_task", "每日任務")
    data object Conversation : Screen("conversation", "模擬對話")
    data object ListeningQuiz : Screen("listening_quiz", "聽力測驗")
    data object Vocabulary : Screen("vocabulary", "單字表")
    data object MemoList : Screen("memo_list", "備忘錄")
    data object MemoCreate : Screen("memo_create", "新增備忘")
    data object Review : Screen("review", "複習")
    data object RealTimeConversation : Screen("realtime_conversation", "即時對話")
    data object Translation : Screen("translation", "翻譯")
    data object Settings : Screen("settings", "設定")
}

private data class BottomNavItem(
    val screen: Screen,
    val icon: @Composable () -> Unit,
    val label: String
)

private val bottomNavItems = listOf(
    BottomNavItem(Screen.DailyTask, { Icon(Icons.Default.CalendarToday, contentDescription = "每日任務") }, "每日任務"),
    BottomNavItem(Screen.Conversation, { Icon(Icons.Default.Forum, contentDescription = "模擬對話") }, "模擬對話"),
    BottomNavItem(Screen.ListeningQuiz, { Icon(Icons.Default.Hearing, contentDescription = "聽力測驗") }, "聽力測驗"),
    BottomNavItem(Screen.Vocabulary, { Icon(Icons.Default.Spellcheck, contentDescription = "單字表") }, "單字表"),
    BottomNavItem(Screen.MemoList, { Icon(Icons.Default.StickyNote2, contentDescription = "備忘錄") }, "備忘錄"),
    BottomNavItem(Screen.Translation, { Icon(Icons.Default.Translate, contentDescription = "翻譯") }, "翻譯"),
)

@Composable
fun AppNavigation(mainViewModel: MainViewModel = hiltViewModel()) {
    val onboardingCompleted by mainViewModel.onboardingCompleted.collectAsStateWithLifecycle()

    when (onboardingCompleted) {
        null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        false -> {
            OnboardingScreen(onComplete = { mainViewModel.completeOnboarding() })
        }
        true -> {
            MainAppContent()
        }
    }
}

@Composable
private fun MainAppContent() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Screen.DailyTask.route,
        Screen.Conversation.route,
        Screen.ListeningQuiz.route,
        Screen.Vocabulary.route,
        Screen.MemoList.route,
        Screen.Translation.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = item.icon,
                            label = { Text(item.label) },
                            selected = navBackStackEntry?.destination?.hierarchy?.any {
                                it.route == item.screen.route
                            } == true,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.DailyTask.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.DailyTask.route) {
                DailyTaskScreen(
                    onNavigateToListening = {
                        navController.navigate(Screen.ListeningQuiz.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToVocabulary = {
                        navController.navigate(Screen.Vocabulary.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToConversation = {
                        navController.navigate(Screen.Conversation.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToSettings = {
                        navController.navigate(Screen.Settings.route)
                    }
                )
            }
            composable(Screen.ListeningQuiz.route) {
                ListeningQuizScreen()
            }
            composable(Screen.Conversation.route) {
                ConversationScreen(
                    onNavigateToRealTime = {
                        navController.navigate(Screen.RealTimeConversation.route)
                    }
                )
            }
            composable(Screen.RealTimeConversation.route) {
                RealTimeConversationScreen(
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Vocabulary.route) {
                VocabularyScreen()
            }
            composable(Screen.MemoList.route) {
                MemoListScreen(
                    onCreateMemo = { navController.navigate(Screen.MemoCreate.route) },
                    onReview = { navController.navigate(Screen.Review.route) }
                )
            }
            composable(Screen.MemoCreate.route) {
                MemoScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.Review.route) {
                ReviewScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.Settings.route) {
                SettingsScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.Translation.route) {
                TranslationScreen()
            }
        }
    }
}
