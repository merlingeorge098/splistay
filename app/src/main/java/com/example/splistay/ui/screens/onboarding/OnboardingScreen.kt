package com.example.splistay.ui.screens.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

data class OnboardingPage(
    val title: String,
    val description: String,
    val imageUrl: String
)

val onboardingPages = listOf(
    OnboardingPage(
        "Welcome to SplitStay",
        "Track expenses effortlessly. Share costs without the awkward conversations, all in one zen place.",
        "https://lh3.googleusercontent.com/aida-public/AB6AXuCsyaEDw8mdbiorjPfbgHTurp018Gke7Rk7iZSAzMwEPKEVT6AKHB65xRtw8YVIj50e3S6ew4lDlXtqmDGHnzTDolQO19GkRXEMttCNPcYhutQl7i0f6iivCTgokvPFMAI9lQo2Tk31s6hOXTnTRom0fePyeyh0wTtSoCE95DEDeV5h9r5zBqmIxcap8k53snUjgGMDAFJg0oth4f3uK8gtooalkRjaLYrDGaSqP6E-3TH7siQCJKV6WA"
    ),
    OnboardingPage(
        "Never Miss Chores",
        "Coordinate cleaning turns and shared tasks. Get notified when it's your turn to keep the harmony.",
        "https://lh3.googleusercontent.com/aida-public/AB6AXuCsyaEDw8mdbiorjPfbgHTurp018Gke7Rk7iZSAzMwEPKEVT6AKHB65xRtw8YVIj50e3S6ew4lDlXtqmDGHnzTDolQO19GkRXEMttCNPcYhutQl7i0f6iivCTgokvPFMAI9lQo2Tk31s6hOXTnTRom0fePyeyh0wTtSoCE95DEDeV5h9r5zBqmIxcap8k53snUjgGMDAFJg0oth4f3uK8gtooalkRjaLYrDGaSqP6E-3TH7siQCJKV6WA" // Placeholder
    ),
    OnboardingPage(
        "Settle Fairly",
        "Transparent debt tracking and easy UPI settlements. No more disputes over who owes whom.",
        "https://lh3.googleusercontent.com/aida-public/AB6AXuCsyaEDw8mdbiorjPfbgHTurp018Gke7Rk7iZSAzMwEPKEVT6AKHB65xRtw8YVIj50e3S6ew4lDlXtqmDGHnzTDolQO19GkRXEMttCNPcYhutQl7i0f6iivCTgokvPFMAI9lQo2Tk31s6hOXTnTRom0fePyeyh0wTtSoCE95DEDeV5h9r5zBqmIxcap8k53snUjgGMDAFJg0oth4f3uK8gtooalkRjaLYrDGaSqP6E-3TH7siQCJKV6WA" // Placeholder
    )
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    onGuest: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val scope = rememberCoroutineScope()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SplitStay",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                TextButton(onClick = onFinish) {
                    Text("Skip", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Carousel
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { pageIndex ->
                OnboardingContent(onboardingPages[pageIndex])
            }

            // Bottom Actions
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Indicators
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    repeat(onboardingPages.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .height(8.dp)
                                .width(if (isSelected) 32.dp else 8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                )
                        )
                    }
                }

                Button(
                    onClick = {
                        if (pagerState.currentPage < onboardingPages.size - 1) {
                            scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                        } else {
                            onFinish()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(if (pagerState.currentPage == onboardingPages.size - 1) "Get Started" else "Next")
                        Spacer(Modifier.width(8.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null)
                    }
                }

                OutlinedButton(
                    onClick = { /* Google Sign In */ },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text("Sign in with Google")
                }

                TextButton(onClick = onGuest) {
                    Text("Continue as Guest", color = MaterialTheme.colorScheme.outline)
                }
            }
        }
    }
}

@Composable
fun OnboardingContent(page: OnboardingPage) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
                .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = page.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(0.8f),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(Modifier.height(32.dp))

        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 40.dp)
        )
    }
}
