package com.example.ui

import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BookContent
import com.example.data.BookSection
import com.example.data.UserPreferencesManager
import com.example.ui.components.AppNavigationDest
import com.example.ui.components.AudioPlayerBar
import com.example.ui.components.Book3DCoverView
import com.example.ui.components.BookReader3DFlip
import com.example.ui.components.BookmarksView
import com.example.ui.components.InvasionMatrixView
import com.example.ui.components.NoteDialog
import com.example.ui.components.QuizView
import com.example.ui.components.RenewersTableView
import com.example.ui.components.SearchDialog
import com.example.ui.components.SettingsDialog
import com.example.ui.components.TableOfContentsDrawer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookAppMainScreen(
    userPrefs: UserPreferencesManager,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }

    // User preferences states
    val themeMode by userPrefs.themeMode.collectAsState()
    val fontSize by userPrefs.fontSize.collectAsState()
    val is3DMode by userPrefs.is3DViewMode.collectAsState()
    val bookmarks by userPrefs.bookmarks.collectAsState()
    val lastReadSectionId by userPrefs.lastReadSectionId.collectAsState()

    // Flattened list of all sections for linear reader
    val allSections: List<BookSection> = remember {
        BookContent.chapters.flatMap { it.sections }
    }

    var currentSectionIndex by remember {
        val initialIdx = allSections.indexOfFirst { it.id == lastReadSectionId }
        mutableIntStateOf(if (initialIdx >= 0) initialIdx else 0)
    }

    var currentDest by remember { mutableStateOf(AppNavigationDest.COVER_3D) }
    var showSearchDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var activeNoteSectionId by remember { mutableStateOf<String?>(null) }

    // TTS Audio Reader State
    var ttsEngine by remember { mutableStateOf<TextToSpeech?>(null) }
    var isTtsPlaying by remember { mutableStateOf(false) }
    var currentSpeechSpeed by remember { mutableFloatStateOf(1.0f) }
    var currentSpeakingTitle by remember { mutableStateOf("") }
    var showAudioPlayerBar by remember { mutableStateOf(false) }

    // Initialize TTS
    DisposableEffect(Unit) {
        var tts: TextToSpeech? = null
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale("ar")
            }
        }
        ttsEngine = tts

        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    fun speakText(text: String, title: String) {
        currentSpeakingTitle = title
        showAudioPlayerBar = true
        isTtsPlaying = true
        ttsEngine?.setSpeechRate(currentSpeechSpeed)
        ttsEngine?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "BOOK_SPEECH")
    }

    fun stopSpeaking() {
        ttsEngine?.stop()
        isTtsPlaying = false
    }

    fun shareAppOrSection(textToShare: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, textToShare)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "مشاركة المقطع")
        context.startActivity(shareIntent)
    }

    MyApplicationTheme(themeMode = themeMode) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    TableOfContentsDrawer(
                        bookInfo = BookContent.bookInfo,
                        chapters = BookContent.chapters,
                        allSections = allSections,
                        currentSectionIndex = currentSectionIndex,
                        currentDest = currentDest,
                        bookmarksCount = bookmarks.size,
                        onNavigate = { dest ->
                            currentDest = dest
                            scope.launch { drawerState.close() }
                        },
                        onSelectSectionIndex = { idx ->
                            currentSectionIndex = idx
                            userPrefs.saveLastReadSection(allSections[idx].id)
                            currentDest = AppNavigationDest.READER_3D
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            }
        ) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = when (currentDest) {
                                    AppNavigationDest.COVER_3D -> "أثر الفكر الغربي"
                                    AppNavigationDest.READER_3D -> allSections.getOrNull(currentSectionIndex)?.title ?: "قراءة الكتاب"
                                    AppNavigationDest.RENEWERS_TABLE -> "موسوعة المجددين"
                                    AppNavigationDest.INVASION_MATRIX -> "مصفوفة الغزو الفكري"
                                    AppNavigationDest.QUIZ -> "اختبر معلوماتك"
                                    AppNavigationDest.BOOKMARKS -> "الإشارات المرجعية"
                                    AppNavigationDest.REFERENCES -> "المراجع والاتصال"
                                },
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                ),
                                maxLines = 1
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = { scope.launch { drawerState.open() } },
                                modifier = Modifier.testTag("btn_open_drawer")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "القائمة الرئيسية",
                                    tint = GoldLight
                                )
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = { showSearchDialog = true },
                                modifier = Modifier.testTag("btn_open_search")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "بحث",
                                    tint = Color.White
                                )
                            }

                            IconButton(
                                onClick = { showSettingsDialog = true },
                                modifier = Modifier.testTag("btn_open_settings")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "الإعدادات",
                                    tint = Color.White
                                )
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = EmeraldPrimary
                        )
                    )
                },
                bottomBar = {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp
                    ) {
                        NavigationBarItem(
                            selected = currentDest == AppNavigationDest.COVER_3D,
                            onClick = { currentDest = AppNavigationDest.COVER_3D },
                            icon = { Icon(Icons.Default.MenuBook, contentDescription = "الغلاف") },
                            label = { Text("الغلاف", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = EmeraldPrimary,
                                selectedTextColor = EmeraldPrimary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )

                        NavigationBarItem(
                            selected = currentDest == AppNavigationDest.READER_3D,
                            onClick = { currentDest = AppNavigationDest.READER_3D },
                            icon = { Icon(Icons.Default.MenuBook, contentDescription = "الكتاب") },
                            label = { Text("الكتاب", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = EmeraldPrimary,
                                selectedTextColor = EmeraldPrimary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )

                        NavigationBarItem(
                            selected = currentDest == AppNavigationDest.RENEWERS_TABLE,
                            onClick = { currentDest = AppNavigationDest.RENEWERS_TABLE },
                            icon = { Icon(Icons.Default.MenuBook, contentDescription = "المجددون") },
                            label = { Text("المجددون", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = EmeraldPrimary,
                                selectedTextColor = EmeraldPrimary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )

                        NavigationBarItem(
                            selected = currentDest == AppNavigationDest.INVASION_MATRIX,
                            onClick = { currentDest = AppNavigationDest.INVASION_MATRIX },
                            icon = { Icon(Icons.Default.Bookmark, contentDescription = "المصفوفة") },
                            label = { Text("المصفوفة", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = EmeraldPrimary,
                                selectedTextColor = EmeraldPrimary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )

                        NavigationBarItem(
                            selected = currentDest == AppNavigationDest.BOOKMARKS,
                            onClick = { currentDest = AppNavigationDest.BOOKMARKS },
                            icon = { Icon(Icons.Default.Bookmark, contentDescription = "المحفوظات") },
                            label = { Text("المحفوظات", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = EmeraldPrimary,
                                selectedTextColor = EmeraldPrimary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                },
                snackbarHost = { SnackbarHost(snackbarHostState) },
                modifier = modifier.fillMaxSize()
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (currentDest) {
                        AppNavigationDest.COVER_3D -> {
                            Book3DCoverView(
                                bookInfo = BookContent.bookInfo,
                                onStartReading = { currentDest = AppNavigationDest.READER_3D },
                                onOpenRenewers = { currentDest = AppNavigationDest.RENEWERS_TABLE },
                                onOpenMatrix = { currentDest = AppNavigationDest.INVASION_MATRIX }
                            )
                        }

                        AppNavigationDest.READER_3D -> {
                            val currentSec = allSections[currentSectionIndex]
                            val isBookmarked = bookmarks.contains(currentSec.id)

                            BookReader3DFlip(
                                sections = allSections,
                                currentIndex = currentSectionIndex,
                                onIndexChanged = { newIdx ->
                                    currentSectionIndex = newIdx
                                    userPrefs.saveLastReadSection(allSections[newIdx].id)
                                    if (isTtsPlaying) {
                                        val sec = allSections[newIdx]
                                        val text = sec.title + " . " + sec.paragraphs.joinToString(" ")
                                        speakText(text, sec.title)
                                    }
                                },
                                fontSizeSp = fontSize,
                                onFontSizeChanged = { userPrefs.setFontSize(it) },
                                isBookmarked = isBookmarked,
                                onToggleBookmark = {
                                    userPrefs.toggleBookmark(currentSec.id)
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            if (!isBookmarked) "تمت إضافة الصفحة للإشارات المرجعية" else "تمت إزالة الإشارة المرجعية"
                                        )
                                    }
                                },
                                onSpeakSection = { text ->
                                    speakText(text, currentSec.title)
                                },
                                onShareSection = { text ->
                                    shareAppOrSection(text)
                                },
                                onOpenNoteDialog = { secId ->
                                    activeNoteSectionId = secId
                                }
                            )
                        }

                        AppNavigationDest.RENEWERS_TABLE -> {
                            RenewersTableView(renewers = BookContent.renewersList)
                        }

                        AppNavigationDest.INVASION_MATRIX -> {
                            InvasionMatrixView(matrixEntries = BookContent.invasionMatrix)
                        }

                        AppNavigationDest.QUIZ -> {
                            QuizView(questions = BookContent.quizQuestions)
                        }

                        AppNavigationDest.BOOKMARKS -> {
                            BookmarksView(
                                bookmarkedSectionIds = bookmarks,
                                allSections = allSections,
                                getNote = { secId -> userPrefs.getNote(secId) },
                                onSelectSectionIndex = { idx ->
                                    currentSectionIndex = idx
                                    currentDest = AppNavigationDest.READER_3D
                                },
                                onRemoveBookmark = { secId ->
                                    userPrefs.toggleBookmark(secId)
                                }
                            )
                        }

                        AppNavigationDest.REFERENCES -> {
                            // Display references
                            RenewersTableView(renewers = BookContent.renewersList)
                        }
                    }

                    // Floating Audio Player Control
                    AnimatedVisibility(
                        visible = showAudioPlayerBar,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    ) {
                        AudioPlayerBar(
                            isPlaying = isTtsPlaying,
                            currentSpeakingTitle = currentSpeakingTitle,
                            currentSpeed = currentSpeechSpeed,
                            onTogglePlay = {
                                if (isTtsPlaying) {
                                    stopSpeaking()
                                } else {
                                    val sec = allSections[currentSectionIndex]
                                    val text = sec.title + " . " + sec.paragraphs.joinToString(" ")
                                    speakText(text, sec.title)
                                }
                            },
                            onChangeSpeed = {
                                currentSpeechSpeed = when (currentSpeechSpeed) {
                                    0.75f -> 1.0f
                                    1.0f -> 1.25f
                                    1.25f -> 1.5f
                                    else -> 0.75f
                                }
                                if (isTtsPlaying) {
                                    val sec = allSections[currentSectionIndex]
                                    val text = sec.title + " . " + sec.paragraphs.joinToString(" ")
                                    speakText(text, sec.title)
                                }
                            },
                            onClosePlayer = {
                                stopSpeaking()
                                showAudioPlayerBar = false
                            }
                        )
                    }
                }
            }
        }

        // Search Dialog
        if (showSearchDialog) {
            SearchDialog(
                chapters = BookContent.chapters,
                allSections = allSections,
                onDismiss = { showSearchDialog = false },
                onSelectSectionIndex = { idx ->
                    currentSectionIndex = idx
                    userPrefs.saveLastReadSection(allSections[idx].id)
                    currentDest = AppNavigationDest.READER_3D
                }
            )
        }

        // Settings Dialog
        if (showSettingsDialog) {
            SettingsDialog(
                themeMode = themeMode,
                onThemeModeChanged = { userPrefs.setThemeMode(it) },
                fontSize = fontSize,
                onFontSizeChanged = { userPrefs.setFontSize(it) },
                is3DMode = is3DMode,
                on3DModeChanged = { userPrefs.set3DViewMode(it) },
                bookInfo = BookContent.bookInfo,
                onDismiss = { showSettingsDialog = false }
            )
        }

        // Notes Dialog
        activeNoteSectionId?.let { secId ->
            val section = allSections.find { it.id == secId }
            NoteDialog(
                sectionId = secId,
                sectionTitle = section?.title ?: "",
                initialNote = userPrefs.getNote(secId),
                onSaveNote = { note ->
                    userPrefs.saveNote(secId, note)
                    scope.launch {
                        snackbarHostState.showSnackbar("تم حفظ ملاحظتك بنجاح")
                    }
                },
                onDismiss = { activeNoteSectionId = null }
            )
        }
    }
}
