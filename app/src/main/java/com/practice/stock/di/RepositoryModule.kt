package com.practice.stock.di

import com.practice.stock.data.csv.CSVParser
import com.practice.stock.data.csv.CompanyListingsParser
import com.practice.stock.data.csv.IntradayInfoParser
import com.practice.stock.data.repository.StockRepositoryImpl
import com.practice.stock.domain.model.CompanyListing
import com.practice.stock.domain.model.IntradayInfo
import com.practice.stock.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindCompanyListingsParser(
        companyListingsParser: CompanyListingsParser
    ): CSVParser<CompanyListing>


    @Binds
    @Singleton
    abstract fun bindIntradayInfoParser(
        intradayInfoParser: IntradayInfoParser
    ): CSVParser<IntradayInfo>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        repositoryImpl: StockRepositoryImpl
    ):StockRepository


}