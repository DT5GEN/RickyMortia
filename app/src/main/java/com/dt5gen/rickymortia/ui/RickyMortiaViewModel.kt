package com.dt5gen.rickymortia.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dt5gen.rickymortia.data.local.CharacterLocalRepository
import com.dt5gen.rickymortia.data.remote.CharacterSyncRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import com.dt5gen.rickymortia.data.local.CharacterEntity
import com.dt5gen.rickymortia.data.CharactersPagingRepository

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
            runCatching {
                syncRepo.syncFirstPage()
            }.onFailure {
                Log.e("SyncRepo", "sync error", it)
            }
        }
    }
}