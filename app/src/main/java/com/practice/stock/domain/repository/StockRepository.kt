package com.practice.stock.domain.repository

import com.practice.stock.domain.model.CompanyInfo
import com.practice.stock.domain.model.CompanyListing
import com.practice.stock.domain.model.IntradayInfo
import com.practice.stock.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getCompanyListings(
        fetchRemote:Boolean,
        query:String
    ):Flow<Resource<List<CompanyListing>>>

    suspend fun getIntradayInfo(
        symbol:String
    ):Resource<List<IntradayInfo>>

    suspend fun getCompanyInfo(
        symbol:String
    ):Resource<CompanyInfo>
}