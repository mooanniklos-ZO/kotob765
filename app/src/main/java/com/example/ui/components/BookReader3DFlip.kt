package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BookSection
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary

@Composable
fun BookReader3DFlip(
    sections: List<BookSection>,
    currentIndex: Int,
    onIndexChanged: (Int) -> Unit,
    fontSizeSp: Int,
    onFontSizeChanged: (Int) -> Unit,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    onSpeakSection: (String) -> Unit,
    onShareSection: (String) -> Unit,
    onOpenNoteDialog: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var flipAngle by remember { mutableFloatStateOf(0f) }
    var showFontSlider by remember { mutableStateOf(false) }

    val currentSection = sections.getOrElse(currentIndex) { sections[0] }
    val totalSections = sections.size

    val animatedAngle by animateFloatAsState(
        targetValue = flipAngle,
        animationSpec = tween(durationMillis = 350),
        label = "pageFlip"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        // Top Mini Bar: Section Title + Bookmark + Audio + Share + Font Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        text = "ص ${currentSection.pageNumber}",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Text(
                    text = "${currentIndex + 1} / $totalSections",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { showFontSlider = !showFontSlider },
                    modifier = Modifier.testTag("btn_font_size")
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatSize,
                        contentDescription = "تغيير حجم الخط",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(
                    onClick = {
                        val textToRead = currentSection.title + " . " + currentSection.paragraphs.joinToString(" ")
                        onSpeakSection(textToRead)
                    },
                    modifier = Modifier.testTag("btn_audio_speak")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "استماع صوتي",
                        tint = EmeraldPrimary
                    )
                }

                IconButton(
                    onClick = {
                        val textToShare = "كتاب: أثر الفكر الغربي المعاصر\n" +
                                "د. مالك الرميمة\n\n" +
                                "${currentSection.title}\n\n" +
                                currentSection.paragraphs.joinToString("\n\n")
                        onShareSection(textToShare)
                    },
                    modifier = Modifier.testTag("btn_share")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "مشاركة المقطع",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }

                IconButton(
                    onClick = { onOpenNoteDialog(currentSection.id) },
                    modifier = Modifier.testTag("btn_note")
                ) {
                    Icon(
                        imageVector = Icons.Default.NoteAdd,
                        contentDescription = "إضافة ملاحظة",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }

                IconButton(
                    onClick = onToggleBookmark,
                    modifier = Modifier.testTag("btn_bookmark")
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "حفظ إشارة مرجعية",
                        tint = if (isBookmarked) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Animated Font Size Controller Bar
        AnimatedVisibility(visible = showFontSlider) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "حجم الخط (${fontSizeSp}sp):",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Slider(
                        value = fontSizeSp.toFloat(),
                        onValueChange = { onFontSizeChanged(it.toInt()) },
                        valueRange = 14f..28f,
                        steps = 7,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Progress line
        LinearProgressIndicator(
            progress = { (currentIndex + 1).toFloat() / totalSections.toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .clip(CircleShape),
            color = GoldPrimary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 3D Realistic Book Page Canvas
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .graphicsLayer {
                    this.rotationY = animatedAngle
                    cameraDistance = 14f * density
                }
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        if (delta > 20 && currentIndex > 0) {
                            onIndexChanged(currentIndex - 1)
                        } else if (delta < -20 && currentIndex < totalSections - 1) {
                            onIndexChanged(currentIndex + 1)
                        }
                    }
                )
                .shadow(8.dp, shape = RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            // Book Page Gutter Gradient
            Box(
                modifier = Modifier
                    .width(16.dp)
                    .fillMaxSize()
                    .align(Alignment.CenterStart)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0x18000000),
                                Color(0x00000000)
                            )
                        )
                    )
            )

            // Page Content (Scrollable if text exceeds height)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Chapter / Section Header
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(
                        text = currentSection.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = (fontSizeSp + 2).sp
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                if (currentSection.subtitle.isNotEmpty()) {
                    Text(
                        text = currentSection.subtitle,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = GoldDark,
                            fontSize = (fontSizeSp - 1).sp
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                // Decorative Islamic Divider
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(GoldPrimary.copy(alpha = 0.4f))
                    )
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .size(12.dp)
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(GoldPrimary.copy(alpha = 0.4f))
                    )
                }

                // Paragraphs
                currentSection.paragraphs.forEachIndexed { idx, p ->
                    // Check if it's a Quranic verse or special callout
                    val isVerse = p.contains("﴿") || p.contains("﴾") || p.startsWith("وقال تعالى") || p.startsWith("قال رسول الله")

                    if (isVerse) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.4f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = p,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = (fontSizeSp + 1).sp,
                                    lineHeight = (fontSizeSp * 1.7).sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    } else {
                        Text(
                            text = p,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = fontSizeSp.sp,
                                lineHeight = (fontSizeSp * 1.65).sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                letterSpacing = 0.3.sp
                            ),
                            textAlign = TextAlign.Justify,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Page Footer signature
                Text(
                    text = "كتاب أثر الفكر الغربي المعاصر • د. مالك الرميمة",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontSize = 11.sp
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }

        // Bottom Page Flip Navigation Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Next Page (in Arabic reading, next page goes forward)
            IconButton(
                onClick = {
                    if (currentIndex > 0) {
                        flipAngle = -15f
                        onIndexChanged(currentIndex - 1)
                        flipAngle = 0f
                    }
                },
                enabled = currentIndex > 0,
                modifier = Modifier
                    .background(
                        if (currentIndex > 0) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                        CircleShape
                    )
                    .testTag("btn_prev_page")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "الصفحة السابقة",
                    tint = if (currentIndex > 0) MaterialTheme.colorScheme.onPrimaryContainer else Color.Gray
                )
            }

            Text(
                text = "اسحب الصفحة للتقليب ثلاثي الأبعاد",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            // Next Page button
            IconButton(
                onClick = {
                    if (currentIndex < totalSections - 1) {
                        flipAngle = 15f
                        onIndexChanged(currentIndex + 1)
                        flipAngle = 0f
                    }
                },
                enabled = currentIndex < totalSections - 1,
                modifier = Modifier
                    .background(
                        if (currentIndex < totalSections - 1) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                        CircleShape
                    )
                    .testTag("btn_next_page")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "الصفحة التالية",
                    tint = if (currentIndex < totalSections - 1) MaterialTheme.colorScheme.onPrimaryContainer else Color.Gray
                )
            }
        }
    }
}
