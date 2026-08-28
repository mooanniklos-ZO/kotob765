package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BookChapter
import com.example.data.BookInfo
import com.example.data.BookSection
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary

enum class AppNavigationDest {
    COVER_3D,
    READER_3D,
    RENEWERS_TABLE,
    INVASION_MATRIX,
    QUIZ,
    BOOKMARKS,
    REFERENCES
}

@Composable
fun TableOfContentsDrawer(
    bookInfo: BookInfo,
    chapters: List<BookChapter>,
    allSections: List<BookSection>,
    currentSectionIndex: Int,
    currentDest: AppNavigationDest,
    bookmarksCount: Int,
    onNavigate: (AppNavigationDest) -> Unit,
    onSelectSectionIndex: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header: Book Emblem & Title
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = EmeraldPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = EmeraldDark,
                            border = androidx.compose.foundation.BorderStroke(1.dp, GoldLight),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = null,
                                    tint = GoldLight,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "أثر الفكر الغربي المعاصر",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = GoldLight
                            )
                        )
                        Text(
                            text = "إعداد وتأليف: " + bookInfo.author,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.9f)
                            ),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Call, contentDescription = null, tint = GoldLight, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = bookInfo.authorPhone, style = MaterialTheme.typography.labelSmall.copy(color = GoldLight))
                        }
                    }
                }
            }

            // Quick Nav Links
            item {
                Text(
                    text = "الأقسام الرئيسية",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(top = 6.dp, bottom = 4.dp)
                )
            }

            item {
                DrawerNavItem(
                    title = "الغلاف وتصفح 3D",
                    subtitle = "الواجهة الفاخرة للغلاف مع تفاصيل الكتاب",
                    icon = Icons.Default.AutoStories,
                    isSelected = currentDest == AppNavigationDest.COVER_3D,
                    onClick = { onNavigate(AppNavigationDest.COVER_3D) }
                )
            }

            item {
                DrawerNavItem(
                    title = "قراءة الكتاب (تصفح ثلاثي الأبعاد)",
                    subtitle = "قراءة كاملة لكافة صفحات وأبواب الكتاب",
                    icon = Icons.Default.MenuBook,
                    isSelected = currentDest == AppNavigationDest.READER_3D,
                    onClick = { onNavigate(AppNavigationDest.READER_3D) }
                )
            }

            item {
                DrawerNavItem(
                    title = "موسوعة المجددين في الإسلام",
                    subtitle = "جدول المجددين عبر القرون وشروط التجديد",
                    icon = Icons.Default.HistoryEdu,
                    isSelected = currentDest == AppNavigationDest.RENEWERS_TABLE,
                    onClick = { onNavigate(AppNavigationDest.RENEWERS_TABLE) }
                )
            }

            item {
                DrawerNavItem(
                    title = "مصفوفة الغزو الفكري وسبل التحصن",
                    subtitle = "تحليل موازي: الوسائل • الأسباب • الحل الجذري",
                    icon = Icons.Default.Shield,
                    isSelected = currentDest == AppNavigationDest.INVASION_MATRIX,
                    onClick = { onNavigate(AppNavigationDest.INVASION_MATRIX) }
                )
            }

            item {
                DrawerNavItem(
                    title = "اختبر معلوماتك من الكتاب",
                    subtitle = "أسئلة تفاعلية مع توضيحات علمية",
                    icon = Icons.Default.Quiz,
                    isSelected = currentDest == AppNavigationDest.QUIZ,
                    onClick = { onNavigate(AppNavigationDest.QUIZ) }
                )
            }

            item {
                DrawerNavItem(
                    title = "الإشارات المرجعية المحفوظة",
                    subtitle = "عدد الصفحات المحفوظة: $bookmarksCount",
                    icon = Icons.Default.Bookmark,
                    isSelected = currentDest == AppNavigationDest.BOOKMARKS,
                    onClick = { onNavigate(AppNavigationDest.BOOKMARKS) }
                )
            }

            item {
                Divider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                    modifier = Modifier.padding(vertical = 6.dp)
                )
                Text(
                    text = "فهرس الأبواب والفصول",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }

            // Chapters & Sections Tree
            chapters.forEach { chapter ->
                item {
                    Text(
                        text = chapter.title,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = GoldDark
                        ),
                        modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                    )
                }

                items(chapter.sections) { sec ->
                    val overallIndex = allSections.indexOfFirst { it.id == sec.id }
                    val isCurrent = currentSectionIndex == overallIndex && currentDest == AppNavigationDest.READER_3D

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isCurrent) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                                else Color.Transparent
                            )
                            .clickable {
                                if (overallIndex >= 0) {
                                    onSelectSectionIndex(overallIndex)
                                    onNavigate(AppNavigationDest.READER_3D)
                                }
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = if (isCurrent) Icons.Default.CheckCircle else Icons.Default.Star,
                                contentDescription = null,
                                tint = if (isCurrent) EmeraldPrimary else GoldPrimary.copy(alpha = 0.6f),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = sec.title,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }

                        Text(
                            text = "ص ${sec.pageNumber}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun DrawerNavItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) EmeraldPrimary else Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) GoldLight else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}
