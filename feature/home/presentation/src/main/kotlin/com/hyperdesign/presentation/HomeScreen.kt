package com.hyperdesign.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.hyperdesign.books_app.common.presentation.R
import com.hyperdesign.design_system.component.EmptyState
import com.hyperdesign.design_system.component.ErrorState
import com.hyperdesign.design_system.component.OfflineBanner
import com.hyperdesign.design_system.component.PosterCard
import com.hyperdesign.design_system.component.PosterGridPlaceholder
import com.hyperdesign.navigation.bookPosterKey
import com.hyperdesign.navigation.sharedBookElement
import com.hyperdesign.presentation.components.HeroCarousel
import com.hyperdesign.presentation.error.toUserMessage
import com.hyperdesign.presentation.model.BooksUiModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

private const val HERO_COUNT = 6

private enum class BooksPhase { Loading, Error, Empty, Content }
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val books = viewModel.pagedBooks.collectAsLazyPagingItems()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeEffect.NavigateToDetails -> {
//                    onOpenDetails(effect.movieId)
                }
                is HomeEffect.ShowSnackbar ->
                    scope.launch { snackbarHostState.showSnackbar(effect.message) }
            }
        }
    }

    HomeScreenContent(
        state = state,
        books = books,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::sendIntent,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreenContent(
    state: HomeState,
    books: LazyPagingItems<BooksUiModel>,
    snackbarHostState: SnackbarHostState,
    onIntent: (HomeIntent) -> Unit,
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.books_title),
                        fontWeight = FontWeight.Bold,
                    )
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->

        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (state.isOffline) {
                OfflineBanner(message = stringResource(R.string.books_offline))
            }

            val refresh = books.loadState.refresh

            var userRefreshing by rememberSaveable { mutableStateOf(false) }
            LaunchedEffect(refresh) {
                if (userRefreshing && refresh !is LoadState.Loading) userRefreshing = false
            }


            val phase = when {
                refresh is LoadState.Loading && books.itemCount == 0 -> BooksPhase.Loading
                refresh is LoadState.Error && books.itemCount == 0 -> BooksPhase.Error
                refresh is LoadState.NotLoading && books.itemCount == 0 -> BooksPhase.Empty
                else -> BooksPhase.Content
            }

            Crossfade(targetState = phase, label = "BooksPhase") { p ->
                when (p) {
                    BooksPhase.Loading -> PosterGridPlaceholder(minItemWidth = 160.dp, count = 12)

                    BooksPhase.Error -> ErrorState(
                        message = (refresh as? LoadState.Error)?.error.toUserMessage(
                            fallback = stringResource(R.string.books_error),
                        ),
                        retryLabel = stringResource(R.string.books_retry),
                        onRetry = { books.retry() },
                    )

                    BooksPhase.Empty -> EmptyState(
                        message = stringResource(R.string.books_empty),
                    )

                    BooksPhase.Content -> PullToRefreshBox(
                        isRefreshing = userRefreshing,
                        onRefresh = {
                            userRefreshing = true
                            books.refresh()
                        },
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        BooksGrid(books = books, onIntent = onIntent)
                    }
                }
            }


        }

    }


}

@Composable
private fun BooksGrid(
    books: LazyPagingItems<BooksUiModel>,
    onIntent: (HomeIntent) -> Unit,
) {
    val heroItems = remember(books.itemSnapshotList) {
        (0 until minOf(HERO_COUNT, books.itemCount)).mapNotNull { books.peek(it) }
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        if (heroItems.isNotEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column {
                    HeroCarousel(
                        books = heroItems,
                        onClick = {
//                            onIntent(HomeIntent.OpenDetails(it))
                                  },
                    )
                    Spacer(Modifier.height(4.dp))
                }
            }
        }

        items(

            count = books.itemCount,
            key = books.itemKey { it.id },
        ) { index ->
            books[index]?.let { book ->
                PosterCard(
                    posterUrl = book.posterUrl,
                    title = book.title,
                    ratingLabel = book.ratingLabel,
                    isFavorite = book.isFavorite,
                    onToggleFavorite = { onIntent(HomeIntent.ToggleFavorite(book.id)) },
                    onClick = { onIntent(HomeIntent.OpenDetails(book.id)) },

                    modifier = Modifier
                        .animateItem()
                        .sharedBookElement(bookPosterKey(book.id)),
                )
            }
        }
    }
}