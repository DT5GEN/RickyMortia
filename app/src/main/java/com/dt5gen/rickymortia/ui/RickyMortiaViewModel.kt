package com.dt5gen.rickymortia.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dt5gen.rickymortia.data.local.CharacterLocalRepository
import com.dt5gen.rickymortia.data.remote.CharacterSyncRepository
import com.dt5gen.rickymortia.data.CharactersPagingRepository
import com.dt5gen.rickymortia.data.local.CharacterEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class RickyMortiaViewModel @Inject constructor(
    private val localRepo: CharacterLocalRepository,
    private val syncRepo: CharacterSyncRepository,
    private val pagingRepo: CharactersPagingRepository
) : ViewModel() {

    val message = localRepo.countFlow()
        .map { count -> "Characters in DB: $count" }
        .stateIn(
            viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = "Characters in DB: 0"
        )

    val characters: Flow<PagingData<CharacterEntity>> =
        pagingRepo.pagedCharacters(pageSize = 20)

    init {
        Log.d("SyncRepo", "GreetingViewModel init started")
        viewModelScope.launch {
            runCatching { syncRepo.syncFirstPage() }
                .onFailure { Log.e("SyncRepo", "sync error", it) }
        }
    }

    /** Лайк/анлайк персонажа. */
    fun onLikeClick(item: CharacterEntity) {
        viewModelScope.launch {
            try {
                localRepo.setLiked(item.id, !item.isLiked)
            } catch (t: Throwable) {
                Log.e("LocalRepo", "setLiked error", t)
            }
        }
    }

    /** Отметить/снять «изучено». */
    fun onStudiedClick(item: CharacterEntity) {
        viewModelScope.launch {
            try {
                localRepo.setStudied(item.id, !item.isStudied)
            } catch (t: Throwable) {
                Log.e("LocalRepo", "setStudied error", t)
            }
        }
    }
}