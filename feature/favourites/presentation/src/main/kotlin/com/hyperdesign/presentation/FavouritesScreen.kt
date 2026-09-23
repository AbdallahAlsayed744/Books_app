package com.hyperdesign.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hyperdesign.books_app.common.presentation.R
import com.hyperdesign.design_system.component.EmptyState
import com.hyperdesign.design_system.component.PosterCard
import com.hyperdesign.design_system.component.PosterGridPlaceholder
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavouritesScreen(
    onOpenDetails: (Int) -> Unit,
    viewModel: FavoritesViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is FavoritesEffect.NavigateToDetails -> onOpenDetails(effect.bookId)
            }
        }
    }

    FavoritesScreenContent(state = state, onIntent = viewModel::sendIntent)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreenContent(
    state: FavoritesState,
    onIntent: (FavoritesIntent) -> Unit,
){

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.favorites_title),
                        fontWeight = FontWeight.Bold,
                    )
                },
            )
        },
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> PosterGridPlaceholder()
                state.isEmpty -> EmptyState(message = stringResource(R.string.favorites_empty))
                else -> LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(state.items, key = { it.id }) { book ->
                        PosterCard(
                            posterUrl = book.posterUrl,
                            title = book.title,
                            ratingLabel = book.rating.toString(),
                            isFavorite = true,
                            onToggleFavorite = { onIntent(FavoritesIntent.Remove(book.id)) },
                            onClick = { onIntent(FavoritesIntent.OpenDetails(book.id)) },
                            modifier = Modifier.animateItem(),
                        )
                    }
                }
            }
        }
    }
}
