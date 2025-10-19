package com.dt5gen.rickymortia.ui.screens


import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.dt5gen.rickymortia.ui.RickyMortiaViewModel
import com.dt5gen.rickymortia.ui.theme.CharacterCard


private val ScreenGutter = 16.dp
private val SearchHeight = 44.dp

@Composable
fun CharactersListScreen(
    modifier: Modifier = Modifier,
    viewModel: RickyMortiaViewModel,
    onOpenDetails: (Long) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val gridState = rememberLazyGridState()

    // Сбрасываем фокус когда пользователь начинает скроллить список
    LaunchedEffect(gridState.isScrollInProgress) {
        if (gridState.isScrollInProgress) focusManager.clearFocus()
    }

    val query by viewModel.searchQuery.collectAsState()
    val lazyPagingItems = viewModel.characters.collectAsLazyPagingItems()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            SearchTopBar(
                value = query,
                onChange = viewModel::onSearchChange
            )
        }
    ) { inner ->
        // Любой тап по контенту —  снимает фокус
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(inner)
                .pointerInput(Unit) {
                    detectTapGestures { focusManager.clearFocus() }
                }
        ) {
            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    count = lazyPagingItems.itemCount
                ) { index ->
                    val item = lazyPagingItems[index] ?: return@items
                    CharacterCard(
                        item = item,
                        onClick = { onOpenDetails(item.id)},
                        onLike = { viewModel.onLikeClick(item) },
                        onStudied = { viewModel.onStudiedClick(item) }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)


@Composable
private fun SearchTopBar(
    value: String,
    onChange: (String) -> Unit,
    placeholder: String = "Search characters",
) {
    val focusManager = LocalFocusManager.current
    var focused by remember { mutableStateOf(false) }

    val lineColor by animateColorAsState(
        targetValue = if (focused || value.isNotBlank())
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.outlineVariant,
        label = "search-underline"
    )

    TopAppBar(
        windowInsets = WindowInsets(0),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ScreenGutter)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(SearchHeight)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    val interaction = remember { MutableInteractionSource() }

                    BasicTextField(
                        value = value,
                        onValueChange = onChange,
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Search,
                            keyboardType = KeyboardType.Text
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = { focusManager.clearFocus() }
                        ),
                        modifier = Modifier
                            .matchParentSize()
                            .padding(horizontal = 16.dp)
                            .onFocusChanged { focused = it.isFocused },
                        decorationBox = { inner ->
                            TextFieldDefaults.DecorationBox(
                                value = value,
                                innerTextField = inner,
                                enabled = true,
                                singleLine = true,
                                isError = false,
                                visualTransformation = VisualTransformation.None,
                                interactionSource = interaction,
                                placeholder = { Text(placeholder) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Search,
                                        contentDescription = null
                                    )
                                },
                                trailingIcon = {
                                    if (value.isNotBlank()) {
                                        IconButton(onClick = {
                                            onChange("")
                                            focusManager.clearFocus()
                                        }) {
                                            Icon(
                                                imageVector = Icons.Filled.Clear,
                                                contentDescription = "Clear"
                                            )
                                        }
                                    }
                                },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                ),
                                contentPadding = PaddingValues(vertical = 10.dp)
                            )
                        }
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .height(3.dp)
                            .background(lineColor)
                    )
                }
            }
        }
    )
}