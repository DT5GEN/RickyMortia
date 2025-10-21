package com.dt5gen.rickymortia.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import com.dt5gen.rickymortia.data.local.CharacterDao
import com.dt5gen.rickymortia.data.local.CharacterEntity
import com.dt5gen.rickymortia.data.remote.RickAndMortyApi
import com.dt5gen.rickymortia.data.remote.dto.CharacterDto
import com.dt5gen.rickymortia.data.remote.dto.toEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@Composable
fun CharacterDetailsRoute(
    onBack: () -> Unit,
    viewModel: CharacterDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    CharacterDetailsScreen(
        state = state,
        onBack = onBack,
        onRetry = { viewModel.reload(force = true) },
        onToggleFavorite = { viewModel.toggleFavorite() },
        onToggleStudied = { viewModel.toggleStudied() },
        onTranslate = { viewModel.toggleTranslate() }
    )
}

/* ============================ STATE ============================ */

data class DetailsState(
    val loading: Boolean = false,
    val entity: CharacterEntity? = null,
    val translated: Boolean = false,
    val error: Throwable? = null
)

/* ============================ VM ============================ */

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    private val api: RickAndMortyApi,
    private val dao: CharacterDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val id: Long = savedStateHandle.get<Long>("id")
        ?: error("CharacterDetails: 'id' is required")

    private val _state = MutableStateFlow(DetailsState(loading = true))
    val state: StateFlow<DetailsState> = _state

    init {
        reload(force = false)
    }

    fun reload(force: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(loading = true, error = null) }

            // 1) пробуем взять из БД
            val local = dao.getById(id)
            if (local != null && !force) {
                _state.update { it.copy(loading = false, entity = local) }
                // параллельно тихо подтянем сеть и освежим кэш
                launch { refreshFromNetwork(local) }
                return@launch
            }

            // 2) тянем из сети и кэшируем
            try {
                refreshFromNetwork(old = local)
            } catch (t: Throwable) {
                _state.update { it.copy(loading = false, error = t, entity = local) }
            }
        }
    }

    private suspend fun refreshFromNetwork(old: CharacterEntity?) {
        val dto: CharacterDto = api.getCharacter(id) // метод в  Api  есть
        val merged: CharacterEntity =
            dto.toEntity(old) // расширение-маппер с переносом локальных полей
        dao.upsertAll(listOf(merged)) // вставка/обновление (REPLACE)
        _state.update { it.copy(loading = false, entity = merged, error = null) }
    }

    fun toggleFavorite() {
        val current = _state.value.entity ?: return
        val newValue = !current.isFavorite
        viewModelScope.launch(Dispatchers.IO) {
            dao.setFavorite(id = current.id, value = newValue)
            _state.update { it.copy(entity = it.entity?.copy(isFavorite = newValue)) }
        }
    }

    fun toggleStudied() {
        val current = _state.value.entity ?: return
        val newValue = !current.isStudied
        viewModelScope.launch(Dispatchers.IO) {
            dao.setStudied(id = current.id, value = newValue)
            _state.update { it.copy(entity = it.entity?.copy(isStudied = newValue)) }
        }
    }

    fun toggleTranslate() {
        _state.update { it.copy(translated = !it.translated) }

    }
}

/* ============================ UI ============================ */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterDetailsScreen(
    state: DetailsState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onToggleFavorite: () -> Unit,
    onToggleStudied: () -> Unit,
    onTranslate: () -> Unit
) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = {
                    Text(
                        text = state.entity?.name.orEmpty(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        },
        floatingActionButton = {
            // круглая кнопка перевода (пока — переключатель состояния)
            FloatingActionButton(onClick = onTranslate, shape = CircleShape) {
                Text("文", style = MaterialTheme.typography.titleMedium)
            }
        }
    ) { inner ->
        when {
            state.loading && state.entity == null -> {
                Box(Modifier
                    .fillMaxSize()
                    .padding(inner), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            state.error != null && state.entity == null -> {
                Box(Modifier
                    .fillMaxSize()
                    .padding(inner), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Не удалось загрузить персонажа",
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.height(8.dp))
                        FilledTonalButton(onClick = onRetry) { Text("Повторить") }
                    }
                }
            }

            else -> {
                val item = state.entity ?: return@Scaffold
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(inner)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Обложка
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        AsyncImage(
                            model = item.imageUrl,
                            contentDescription = item.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.matchParentSize()
                        )

                        // Лайк поверх
                        Row(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(12.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.65f))
                        ) {
                            IconButton(onClick = onToggleFavorite) {
                                if (item.isFavorite)
                                    Icon(
                                        Icons.Filled.Favorite,
                                        contentDescription = "Remove from favorites",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                else
                                    Icon(
                                        Icons.Outlined.FavoriteBorder,
                                        contentDescription = "Add to favorites"
                                    )
                            }
                        }
                    }

                    // Основные поля
                    Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        Text(
                            item.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(6.dp))
                        AssistChipRow(
                            status = item.status,
                            species = item.species,
                            gender = item.gender,
                            type = item.type
                        )
                        Spacer(Modifier.height(12.dp))

                        InfoRow(label = "Origin", value = item.originName)
                        InfoRow(label = "Location", value = item.locationName)
                        if (item.episodesCsv.isNotBlank())
                            InfoRow(label = "Episodes", value = item.episodesCsv)

                        Spacer(Modifier.height(16.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            FilledTonalButton(onClick = onToggleStudied) {
                                Icon(Icons.Filled.CheckCircle, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text(if (item.isStudied) "Отмечено как изучено" else "Отметить как изучено")
                            }
                            Spacer(Modifier.width(12.dp))
                            if (item.studiedCorrect > 0) {
                                Text(
                                    text = "Верных ответов: ${item.studiedCorrect}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                        if (state.error != null) {
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = "Последняя ошибка: ${state.error.message}",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}

/* ------------------------- helpers ------------------------- */

@Composable
private fun AssistChipRow(
    status: String,
    species: String,
    gender: String,
    type: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (status.isNotBlank()) MiniChip(status)
        if (species.isNotBlank()) MiniChip(species)
        if (gender.isNotBlank()) MiniChip(gender)
        if (type.isNotBlank()) MiniChip(type)
    }
}

@Composable
private fun MiniChip(text: String) {
    AssistChip(
        onClick = {},
        label = { Text(text) },
        shape = RoundedCornerShape(12.dp),
        colors = AssistChipDefaults.assistChipColors()
    )
}

@Composable
private fun InfoRow(label: String, value: String) {
    if (value.isBlank()) return
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.widthIn(min = 84.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}