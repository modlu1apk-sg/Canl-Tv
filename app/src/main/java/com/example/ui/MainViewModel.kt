package com.example.ui

import androidx.lifecycle.ViewModel
import com.example.data.TvChannel
import com.example.data.TvChannelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine

class MainViewModel : ViewModel() {

    private val _allChannels = MutableStateFlow(TvChannelProvider.channels)
    
    private val _selectedCategory = MutableStateFlow("Tümü")
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _activeChannel = MutableStateFlow<TvChannel?>(null)
    val activeChannel = _activeChannel.asStateFlow()

    private val _isAboutOpened = MutableStateFlow(false)
    val isAboutOpened = _isAboutOpened.asStateFlow()

    // Combined state for filtered channels
    val filteredChannels = combine(
        _allChannels,
        _selectedCategory,
        _searchQuery
    ) { channels, category, query ->
        channels.filter { channel ->
            val matchesCategory = (category == "Tümü" || channel.category == category)
            val matchesQuery = channel.name.contains(query, ignoreCase = true) ||
                    channel.category.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.combine(_activeChannel) { filtered, active ->
        // Put active channel first if it exists, or keep regular order
        filtered
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun playChannel(channel: TvChannel) {
        _activeChannel.value = channel
    }

    fun stopChannel() {
        _activeChannel.value = null
    }

    fun toggleAbout(open: Boolean) {
        _isAboutOpened.value = open
    }
}
