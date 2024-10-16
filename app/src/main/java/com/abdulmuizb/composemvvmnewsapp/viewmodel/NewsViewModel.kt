package com.abdulmuizb.composemvvmnewsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulmuizb.composemvvmnewsapp.api.NewsApiService
import com.abdulmuizb.composemvvmnewsapp.model.Article
import com.abdulmuizb.composemvvmnewsapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsApiService: NewsApiService
) : ViewModel(){
    private val _articlesState = MutableStateFlow< UiState<List<Article>> >(UiState.Loading)
    val articlesState: StateFlow< UiState<List<Article>> > get() = _articlesState.asStateFlow()
    init {
        fetchTopHeadlines()
    }

    private fun fetchTopHeadlines() {
        viewModelScope.launch {
            _articlesState.value = UiState.Loading
            try {
                val response = newsApiService.getTopHeadlines()
                println("API response: $response")
                if (response.articles.isNotEmpty()) {
                    _articlesState.value = UiState.Success(response.articles)
                }else {
                    _articlesState.value = UiState.Error("No articles found")
                }
            }catch (e: Exception){
                e.printStackTrace()
                _articlesState.value = UiState.Error(e.localizedMessage ?: "Unknown Error")
            }
        }
    }
}