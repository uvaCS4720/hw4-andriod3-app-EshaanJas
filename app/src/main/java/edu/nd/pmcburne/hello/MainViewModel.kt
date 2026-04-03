package edu.nd.pmcburne.hello

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.nd.pmcburne.hello.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).placemarkDao()

    private val _allTags = MutableStateFlow<List<String>>(emptyList())
    val allTags: StateFlow<List<String>> = _allTags

    private val _selectedTag = MutableStateFlow("core")
    val selectedTag: StateFlow<String> = _selectedTag

    private val _filteredPlacemarks = MutableStateFlow<List<PlacemarkEntity>>(emptyList())
    val filteredPlacemarks: StateFlow<List<PlacemarkEntity>> = _filteredPlacemarks

    init {
        viewModelScope.launch {
            // fetch from API and insert (IGNORE strategy handles dedup)
            try {
                val dtos = RetrofitInstance.api.getPlacemarks()
                val entities = dtos.map {
                    PlacemarkEntity(it.id, it.name, it.description, it.tagList,
                        it.visualCenter.latitude, it.visualCenter.longitude)
                }
                dao.insertAll(entities)
            } catch (e: Exception) {
                // if offline, just use cached DB data
            }

            // load tags (flatten + deduplicate + sort)
            val tags = dao.getAll()
                .flatMap { it.tagList }
                .distinct()
                .sorted()
            _allTags.value = tags

            // load initial filtered markers
            updateFilter("core")
        }
    }

    fun updateFilter(tag: String) {
        _selectedTag.value = tag
        viewModelScope.launch {
            _filteredPlacemarks.value = dao.getAll().filter { tag in it.tagList }
        }
    }
}