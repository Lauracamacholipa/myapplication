package com.example.myapplication.features.movies.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import com.example.myapplication.features.movies.domain.model.MovieModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MoviesScreen(
    modifier: Modifier = Modifier,
    vm: MoviesViewModel = koinViewModel(),
    navigateToDetail: (movie: MovieModel) -> Unit
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        vm.fetchMovies()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Películas Populares",
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (val st = state) {
            is MoviesViewModel.MoviesUiState.Init -> {
                Text("Esperando...")
            }
            is MoviesViewModel.MoviesUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is MoviesViewModel.MoviesUiState.Error -> {
                Text("Error: ${st.message}", color = Color.Red)
            }
            is MoviesViewModel.MoviesUiState.Success -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(st.movies) { index, movie ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable{
                                    navigateToDetail(movie)
                                }
                        ) {
                            AsyncImage(
                                model = movie.posterURL,
                                contentDescription = movie.title,
                                modifier = Modifier
                                    .height(150.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                movie.title,
                                fontSize = 14.sp,
                                maxLines = 2
                            )
                            IconButton(
                                onClick = {
                                    vm.toggleFavourite(movie.id, movie.favourite)
                                },
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .size(32.dp)
                            ) {
                                Icon(
                                    imageVector = if (movie.favourite) {
                                        Icons.Filled.Favorite
                                    } else {
                                        Icons.Outlined.FavoriteBorder
                                    },
                                    contentDescription = if (movie.favourite) {
                                        "Quitar de favoritos"
                                    } else {
                                        "Agregar a favoritos"
                                    },
                                    tint = if (movie.favourite) Color.Red else Color.Gray
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}