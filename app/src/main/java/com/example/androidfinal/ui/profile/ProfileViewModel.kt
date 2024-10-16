package com.example.androidfinal.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidfinal.db.model.FilmWithGenres
import com.example.androidfinal.domain.GetFilmsHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getFilmsHistoryUseCase: GetFilmsHistoryUseCase
) : ViewModel() {
    val allFilms: Flow<List<FilmWithGenres>> = getFilmsHistoryUseCase.executeAllHistoryFilms()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(500L),
            initialValue = emptyList()
        )

    val filmsViewed: Flow<List<FilmWithGenres>> = getFilmsHistoryUseCase.executeAllViewedFilms()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(500L),
            initialValue = emptyList()
        )

    val collectionsList = getFilmsHistoryUseCase.executeCollectionsList()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(500L),
            initialValue = emptyList()
        )

    fun clearCacheCollection(collectionName: String) {
        when (collectionName) {
            "CACHE" -> { viewModelScope.launch { getFilmsHistoryUseCase.clearHistoryFilms() } }
            "VIEWED" -> { viewModelScope.launch { getFilmsHistoryUseCase.clearViewedFilms() }
            }
        }
    }

    fun addNewCollection(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getFilmsHistoryUseCase.addNewCollection(name)
        }
    }

}