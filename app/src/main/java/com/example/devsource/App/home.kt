package com.example.devsource.App

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.devsource.Homepage.AuthState
import com.example.devsource.Homepage.AuthViewModel
import com.example.devsource.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

@Composable
fun Home(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    navController: NavController,
) {
    val context = LocalContext.current
    val authState = authViewModel.authState.observeAsState()
    var newsItems by remember { mutableStateOf<List<NewsItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    val lazyColumnState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val colorScheme = MaterialTheme.colorScheme

    val primaryColor = colorScheme.primary
    val secondaryColor = colorScheme.secondary
    val tertiaryColor = colorScheme.tertiary
    val primaryContainerColor = colorScheme.primaryContainer
    val secondaryContainerColor = colorScheme.secondaryContainer

    val darkCardBackground = Color(0xFF252525)

    val primaryText = Color.White
    val secondaryText = Color.White.copy(alpha = 0.7f)
    val tertiaryText = Color.White.copy(alpha = 0.5f)

    val scrollToFocusAreas: () -> Unit = {
        coroutineScope.launch {
            lazyColumnState.animateScrollToItem(1)
        }
    }

    DisposableEffect(Unit) {
        val database = FirebaseDatabase.getInstance()
        val newsRef = database.getReference("news")

        val newsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val newsList = mutableListOf<NewsItem>()
                for (newsSnapshot in snapshot.children) {
                    val title = newsSnapshot.child("title").getValue(String::class.java) ?: ""
                    val content = newsSnapshot.child("content").getValue(String::class.java) ?: ""
                    val timestamp = newsSnapshot.child("timestamp").getValue(String::class.java) ?: ""
                    val category = newsSnapshot.child("category").getValue(String::class.java) ?: "General"
                    newsList.add(NewsItem(title, content, timestamp, category))
                }
                newsItems = newsList
                isLoading = false
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Failed to read news data: ${error.message}")
                isLoading = false
            }
        }

        newsRef.addValueEventListener(newsListener)
        onDispose {
            newsRef.removeEventListener(newsListener)
        }
    }

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        LazyColumn(
            state = lazyColumnState,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(24.dp),
                                spotColor = tertiaryColor.copy(alpha = 0.3f),
                                clip = true
                            )
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF212121),
                                        Color(0xFF121212)
                                    )
                                ),
                                shape = RoundedCornerShape(24.dp)
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .shadow(
                                        elevation = 8.dp,
                                        shape = CircleShape,
                                        spotColor = primaryColor.copy(alpha = 0.5f),
                                        ambientColor = primaryColor.copy(alpha = 0.2f)
                                    )
                                    .background(
                                        brush = Brush.radialGradient(
                                            colors = listOf(
                                                primaryColor.copy(alpha = 0.2f),
                                                Color(0xFF151515)
                                            )
                                        ),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.logo2),
                                    contentDescription = "DevSource Logo",
                                    modifier = Modifier.size(80.dp),
                                    colorFilter = ColorFilter.tint(Color.White.copy(alpha = 0.95f))
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = "Welcome to DevSource",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                ),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Box(
                                modifier = Modifier
                                    .width(120.dp)
                                    .height(2.dp)
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                primaryColor.copy(alpha = 0.7f),
                                                primaryColor.copy(alpha = 0.7f),
                                                Color.Transparent
                                            )
                                        ),
                                        shape = RoundedCornerShape(1.dp)
                                    )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Explore, learn and build with us!",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF252525)
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "DevSource is a community of developers working together to build amazing projects. Join our teams and start creating!",
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center,
                                    color = Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.padding(16.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Card(
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.Black),
                                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(
                                        elevation = 6.dp,
                                        shape = RoundedCornerShape(20.dp),
                                        spotColor = tertiaryColor.copy(alpha = 0.3f),
                                        clip = true
                                    )
                            ) {
                                Box {
                                    Image(
                                        painter = painterResource(id = R.drawable.grp2),
                                        contentDescription = "Developer Group",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(180.dp)
                                    )

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(180.dp)
                                            .background(
                                                Brush.verticalGradient(
                                                    colors = listOf(
                                                        Color.Transparent,
                                                        Color.Black.copy(alpha = 0.7f)
                                                    ),
                                                    startY = 180f * 0.5f
                                                )
                                            )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = scrollToFocusAreas,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primaryColor
                                ),
                                elevation = ButtonDefaults.buttonElevation(
                                    defaultElevation = 4.dp,
                                    pressedElevation = 2.dp
                                )
                            ) {
                                Text(
                                    "Explore Below",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Scroll Down",
                                    modifier = Modifier.size(20.dp),
                                    tint = Color.Black
                                )
                            }
                        }
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Our Focus Areas",
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = secondaryColor
                        ),
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .height(3.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        secondaryColor,
                                        secondaryColor,
                                        Color.Transparent
                                    )
                                ),
                                shape = RoundedCornerShape(1.dp)
                            )
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                InfiniteDomainBadges(
                    primaryColor = primaryColor,
                    secondaryColor = secondaryColor,
                    tertiaryColor = tertiaryColor,
                    primaryContainerColor = primaryContainerColor,
                    secondaryContainerColor = secondaryContainerColor
                )
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Weekly News",
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = secondaryColor
                        ),
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .height(3.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        secondaryColor,
                                        secondaryColor,
                                        Color.Transparent
                                    )
                                ),
                                shape = RoundedCornerShape(1.dp)
                            )
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = secondaryColor)
                    }
                }
            } else if (newsItems.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = darkCardBackground
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No news updates available",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                color = tertiaryText
                            )
                        }
                    }
                }
            } else {
                items(newsItems) { newsItem ->
                    NewsCard(
                        newsItem,
                        primaryColor,
                        secondaryColor,
                        tertiaryColor,
                        primaryContainerColor
                    )
                }
            }
        }
    }
}

@Composable
fun NewsCard(
    newsItem: NewsItem,
    primaryColor: Color = MaterialTheme.colorScheme.primary,
    secondaryColor: Color = MaterialTheme.colorScheme.secondary,
    tertiaryColor: Color = MaterialTheme.colorScheme.tertiary,
    accentColor: Color = MaterialTheme.colorScheme.primaryContainer
) {
    var expanded by remember { mutableStateOf(false) }
    val cardBackground = Color(0xFF1A1A1A)
    var isTextTruncated by remember { mutableStateOf(false) }
    var isTitleTruncated by remember { mutableStateOf(false) }

    val (icon, iconColor) = when {
        newsItem.category.contains("Android", ignoreCase = true) ->
            Pair(Icons.Default.Android, Color(0xFF3DDC84))
        newsItem.category.contains("Web", ignoreCase = true) ->
            Pair(Icons.Default.Language, Color(0xFF4285F4))
        newsItem.category.contains("Game", ignoreCase = true) ->
            Pair(Icons.Default.SportsEsports, Color(0xFFE91E63))
        newsItem.category.contains("DevOps", ignoreCase = true) ->
            Pair(Icons.Default.Groups, Color(0xFFFF9800))
        newsItem.category.contains("AI", ignoreCase = true) ||
                newsItem.category.contains("ML", ignoreCase = true) ->
            Pair(Icons.Default.Science, Color(0xFFFFEB3B))
        else -> Pair(Icons.Default.Code, primaryColor)
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackground),
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .shadow(
                elevation = 6.dp,
                spotColor = iconColor.copy(alpha = 0.5f),
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            iconColor.copy(alpha = 0.15f),
                            cardBackground.copy(alpha = 0.98f)
                        ),
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(0f, 800f)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(28.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = newsItem.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        maxLines = if (expanded) Int.MAX_VALUE else 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(),
                        onTextLayout = { result ->
                            isTitleTruncated = result.hasVisualOverflow
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    iconColor.copy(alpha = 0.5f),
                                    iconColor.copy(alpha = 0.2f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = newsItem.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f),
                    maxLines = if (expanded) Int.MAX_VALUE else 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 24.sp,
                    onTextLayout = { result ->
                        isTextTruncated = result.hasVisualOverflow
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Date on the left
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = iconColor.copy(alpha = 0.15f)
                        )
                    ) {
                        Text(
                            text = newsItem.date,
                            style = MaterialTheme.typography.bodySmall,
                            color = iconColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    // Read More button on the right (if needed)
                    if (isTextTruncated || isTitleTruncated || expanded) {
                        TextButton(
                            onClick = { expanded = !expanded },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = iconColor
                            )
                        ) {
                            Text(
                                if (expanded) "Show Less" else "Read More",
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Icon(
                                imageVector = if (expanded)
                                    Icons.Default.KeyboardArrowUp else
                                    Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
private fun DomainBadge(icon: ImageVector, label: String, color: Color) {
    val badgeColor = when (label) {
        "Android Dev" -> Color(0xFF3DDC84)  // Android green
        "Web Dev" -> Color(0xFF4285F4)      // Web blue
        "Game Dev" -> Color(0xFFE91E63)     // Game pink
        "DevOps" -> Color(0xFFFF9800)       // Orange for DevOps
        "AI/ML" -> Color(0xFFFFEB3B)        // Yellow for AI/ML
        else -> color
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1D1D1D)
        ),
        modifier = Modifier
            .width(130.dp)
            .shadow(
                elevation = 8.dp,
                spotColor = badgeColor.copy(alpha = 0.5f),
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .shadow(
                        elevation = 6.dp,
                        shape = CircleShape,
                        spotColor = badgeColor.copy(alpha = 0.5f)
                    )
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                badgeColor.copy(alpha = 0.2f),
                                Color(0xFF151515)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = badgeColor,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = badgeColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = badgeColor,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
@Composable
fun InfiniteDomainBadges(
    primaryColor: Color,
    secondaryColor: Color,
    tertiaryColor: Color,
    primaryContainerColor: Color,
    secondaryContainerColor: Color
) {
    val domainBadges = listOf(
        Triple(Icons.Default.Language, "Web Dev", primaryColor),
        Triple(Icons.Default.Android, "Android Dev", secondaryColor),
        Triple(Icons.Default.SportsEsports, "Game Dev", tertiaryColor),
        Triple(Icons.Default.Groups, "DevOps", primaryContainerColor),
        Triple(Icons.Default.Science, "AI/ML", secondaryContainerColor)
    )

    val repeatedList = List(100) { index -> domainBadges[index % domainBadges.size] }

    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        listState.scrollToItem(repeatedList.size / 2)
    }

    LazyRow(
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp),
        flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    ) {
        items(repeatedList.size) { index ->
            val (icon, label, color) = repeatedList[index]
            DomainBadge(icon, label, color)
        }
    }
}

data class NewsItem(
    val title: String,
    val content: String,
    val date: String,
    val category: String = "General"
)