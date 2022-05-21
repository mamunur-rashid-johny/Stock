package com.practice.stock.presentation.company_info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.stock.domain.repository.StockRepository
import com.practice.stock.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: StockRepository
) : ViewModel() {

    var state by mutableStateOf(CompanyInfoState())

    init {
        viewModelScope.launch {
            val symbol = savedStateHandle.get<String>("symbol") ?: return@launch
            state = state.copy(isLoading = true)
            val companyInfoResults = async { repository.getCompanyInfo(symbol) }
            val intradayResult = async { repository.getIntradayInfo(symbol) }
            when (val result = companyInfoResults.await()) {
                is Resource.Success -> {
                    state = state.copy(
                        companyInfo = result.data,
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.message,
                        companyInfo = null
                    )
                }
                else -> Unit
            }


            when (val result = intradayResult.await()) {
                is Resource.Success -> {
                    state = state.copy(
                        stockInfos =result.data?: emptyList() ,
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.message,
                        companyInfo = null
                    )
                }
                else -> Unit
            }
        }
    }

}