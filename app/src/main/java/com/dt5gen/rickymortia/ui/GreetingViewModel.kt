package com.dt5gen.rickymortia.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dt5gen.rickymortia.data.CharacterLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class GreetingViewModel @Inject constructor(
    private val localRepo: CharacterLocalRepository
) : ViewModel() {

    // Реактивно наблюдаем за количеством записей в таблице
    val message = localRepo.countFlow()
        .map { count -> "Characters in DB: $count" }
        .stateIn(viewModelScope, SharingStarted.Eagerly, "Characters in DB: 0")

    // При старте один раз "засеем" одну запись (только для теста)
    init {
        viewModelScope.launch {
            localRepo.seedOneIfEmpty()
        }
    }
}
