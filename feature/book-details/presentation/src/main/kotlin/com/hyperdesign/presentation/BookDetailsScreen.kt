package com.hyperdesign.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hyperdesign.design_system.component.AnimatedFavoriteIcon
import com.hyperdesign.design_system.component.ErrorState
import com.hyperdesign.design_system.component.LoadingState
import com.hyperdesign.design_system.component.PosterImage
import com.hyperdesign.design_system.component.RatingBadge
import com.hyperdesign.design_system.theme.spacing
import com.hyperdesign.navigation.bookPosterKey
import com.hyperdesign.navigation.sharedBookElement
import com.hyperdesign.presentation.model.BookDetailsUiModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun BookDetailsScreen(
    bookId: Int,
    onBack: () -> Unit,
    viewModel: BookDetailsViewModel = koinViewModel(key = "Book_details_$bookId") {
        parametersOf(bookId)
    }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    BookDetailsScreenContent(
        state = state,
        onBack = onBack,
        onRetry = { viewModel.sendIntent(BookDetailsIntent.Retry) },
        onToggleFavorite = { viewModel.sendIntent(BookDetailsIntent.ToggleFavorite) },
    )

}

private enum class DetailsPhase { Loading, Error, Content }

@Composable
internal fun BookDetailsScreenContent(
    state: BookDetailsState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onToggleFavorite: () -> Unit,
) {
    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        val phase = when {
            state.isLoading -> DetailsPhase.Loading
            state.error != null -> DetailsPhase.Error
            else -> DetailsPhase.Content
        }
        Crossfade(targetState = phase, label = "detailsPhase") { p ->
            when (p) {
                DetailsPhase.Loading -> LoadingState()
                DetailsPhase.Error -> ErrorState(
                    message = state.error.orEmpty(),
                    retryLabel = stringResource(R.string.details_retry),
                    onRetry = onRetry,
                )
                DetailsPhase.Content -> state.book?.let { Book ->
                    DetailsContent(
                        Book = Book,
                        isFavorite = state.isFavorite,
                        onToggleFavorite = onToggleFavorite,
                    )
                }
            }
        }

        CircleScrim(
            Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(MaterialTheme.spacing.sm),
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.White,
                )
            }
        }
    }
}

@Composable
private fun DetailsContent(
    Book: BookDetailsUiModel,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()

            .navigationBarsPadding()
            .verticalScroll(rememberScrollState()),
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f),
        ) {
            PosterImage(
                url = Book.posterUrl.ifBlank { Book.posterUrl },
                contentDescription = Book.title,
                modifier = Modifier.fillMaxSize(),
            )
            Box(
                Modifier.fillMaxSize().background(
                    Brush.verticalGradient(
                        0.4f to Color.Transparent,
                        1f to MaterialTheme.colorScheme.background,
                    ),
                ),
            )

            CircleScrim(Modifier.align(Alignment.BottomEnd).padding(MaterialTheme.spacing.md)) {
                IconButton(onClick = onToggleFavorite) {
                    AnimatedFavoriteIcon(
                        isFavorite = isFavorite,
                        favoriteTint = MaterialTheme.colorScheme.error,
                        idleTint = Color.White,
                    )
                }
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .offset(y = (-48).dp)
                .padding(horizontal = MaterialTheme.spacing.md),
        ) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                shadowElevation = 8.dp,
                modifier = Modifier
                    .width(110.dp)
                    .aspectRatio(2f / 3f)

                    .sharedBookElement(bookPosterKey(Book.id)),
            ) {
                PosterImage(url = Book.posterUrl, contentDescription = Book.title)
            }
            Column(
                Modifier
                    .padding(start = MaterialTheme.spacing.md)
                    .padding(top = 56.dp),
            ) {
                Text(
                    text = Book.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(Modifier.height(MaterialTheme.spacing.sm))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RatingBadge(ratingLabel = Book.rating.toString())
                }
            }
        }

        Column(
            Modifier
                .fillMaxWidth()
                .offset(y = (-24).dp)
                .padding(horizontal = MaterialTheme.spacing.md)
                .padding(bottom = MaterialTheme.spacing.xl),
        ) {
            Text(
                text = stringResource(R.string.details_overview),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(Modifier.height(MaterialTheme.spacing.sm))
            Text(
                text = Book.overview.ifBlank { stringResource(R.string.details_no_overview) },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun CircleScrim(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.4f)),
        contentAlignment = Alignment.Center,
    ) { content() }
}
