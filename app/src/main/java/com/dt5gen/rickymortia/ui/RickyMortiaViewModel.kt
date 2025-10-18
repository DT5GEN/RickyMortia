package com.dt5gen.rickymortia.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dt5gen.rickymortia.data.CharactersPagingRepository
import com.dt5gen.rickymortia.data.local.CharacterEntity
import com.dt5gen.rickymortia.data.local.CharacterLocalRepository
import com.dt5gen.rickymortia.data.remote.CharacterSyncRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RickyMortiaViewModel @Inject constructor(
    private val localRepo: CharacterLocalRepository,
    private val syncRepo: CharacterSyncRepository,
    private val pagingRepo: CharactersPagingRepository
) : ViewModel() {

    /* ---------- Поиск ---------- */

    // состояние поискового запроса
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun onSearchChange(value: String) {
        _searchQuery.value = value
    }

    /* ---------- Пейджинг с учётом поиска ---------- */

    val characters: Flow<PagingData<CharacterEntity>> =
        searchQuery
            .debounce(250)
            .distinctUntilChanged()
            .flatMapLatest { q ->
                pagingRepo.pagedCharacters(
                    pageSize = 20,
                    query = q.takeIf { it.isNotBlank() } // null/blank -> без фильтра
                )
            }
            .cachedIn(viewModelScope)

    /* ---------- Синхронизация ---------- */

    init {
        Log.d("SyncRepo", "RickyMortiaViewModel init started")
        viewModelScope.launch {
            runCatching { syncRepo.syncFirstPage() }
                .onFailure { t -> Log.e("SyncRepo", "sync error", t) }
        }
    }

    /* ---------- Обработчики кликов карточки ---------- */

    fun onLikeClick(item: CharacterEntity) {
        viewModelScope.launch {
            runCatching { localRepo.setLiked(item.id, !item.isLiked) }
                .onFailure { t -> Log.e("LocalRepo", "setLiked error", t) }
        }
    }

    fun onStudiedClick(item: CharacterEntity) {
        viewModelScope.launch {
            runCatching { localRepo.setStudied(item.id, !item.isStudied) }
                .onFailure { t -> Log.e("LocalRepo", "setStudied error", t) }
        }
    }
}