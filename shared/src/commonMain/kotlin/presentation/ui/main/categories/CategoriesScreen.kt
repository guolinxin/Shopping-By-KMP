package presentation.ui.main.categories

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import business.domain.main.Category
import coil3.compose.rememberAsyncImagePainter
import presentation.component.CircleButton
import presentation.component.DefaultScreenUI
import presentation.component.Spacer_16dp
import presentation.component.Spacer_8dp
import presentation.component.noRippleClickable
import presentation.ui.main.categories.view_model.CategoriesEvent
import presentation.ui.main.categories.view_model.CategoriesState

@Composable
fun CategoriesScreen(
    state: CategoriesState,
    events: (CategoriesEvent) -> Unit,
    popup: () -> Unit,
    navigateToSearch: (Int) -> Unit,
) {

    DefaultScreenUI(
        queue = state.errorQueue,
        onRemoveHeadFromQueue = { events(CategoriesEvent.OnRemoveHeadFromQueue) },
        progressBarState = state.progressBarState,
        networkState = state.networkState,
        onTryAgain = { events(CategoriesEvent.OnRetryNetwork) }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CircleButton(
                    imageVector = Icons.Filled.ArrowBack,
                    onClick = { popup() }
                )
                Text("Categories", style = MaterialTheme.typography.titleLarge)
                Spacer_8dp()
            }


            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)
            ) {
                items(state.categories, key = { it.id }) {
                    CategoryBox(it, modifier = Modifier.weight(1f)) {
                        navigateToSearch(it.id)
                    }
                }
            }

        }
    }
}


@Composable
private fun CategoryBox(category: Category, modifier: Modifier, onCategoryClick: () -> Unit) {
    Box(modifier = modifier.padding(8.dp), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.noRippleClickable {
                onCategoryClick()
            }
        ) {
            Box(
                modifier = Modifier.background(
                    MaterialTheme.colorScheme.primary.copy(.2f),
                    CircleShape
                ).size(80.dp)
                    .padding(12.dp)
            ) {

                Image(
                    painter = rememberAsyncImagePainter(category.icon),
                    null,
                    modifier = Modifier.fillMaxSize().size(65.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer_8dp()
            Text(
                category.name,
                style = MaterialTheme.typography.labelLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}