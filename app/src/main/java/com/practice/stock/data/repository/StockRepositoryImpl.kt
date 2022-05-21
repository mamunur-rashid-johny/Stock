package com.practice.stock.data.repository

import com.practice.stock.data.csv.CSVParser
import com.practice.stock.data.local.StockDatabase
import com.practice.stock.data.mapper.toCompanyInfo
import com.practice.stock.data.mapper.toCompanyListing
import com.practice.stock.data.mapper.toCompanyListingEntity
import com.practice.stock.data.remote.StockApi
import com.practice.stock.domain.model.CompanyInfo
import com.practice.stock.domain.model.CompanyListing
import com.practice.stock.domain.model.IntradayInfo
import com.practice.stock.domain.repository.StockRepository
import com.practice.stock.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api: StockApi,
    private val db: StockDatabase,
    private val companyListingsParser: CSVParser<CompanyListing>,
    private val intradayInfoParser: CSVParser<IntradayInfo>
) : StockRepository {
    private val dao = db.dao
    override suspend fun getCompanyListings(
        fetchRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListings = dao.searchCompanyListings(query)
            emit(Resource.Success(
                data = localListings.map { it.toCompanyListing() }
            ))

            val isDbEmpty = localListings.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchRemote
            if (shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteListings = try {
                val response = api.getListings()
                companyListingsParser.parse(response.byteStream())

            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            }

            remoteListings?.let { listings ->
                dao.clearCompanyListings()
                dao.insertCompanyListings(listings.map { it.toCompanyListingEntity() })
                emit(Resource.Success(dao.searchCompanyListings("").map { it.toCompanyListing() }))
                emit(Resource.Loading(false))
            }

        }
    }

    override suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfo>> {
        return try {
            val response = api.getGraphData(symbol)
            val results = intradayInfoParser.parse(response.byteStream())
            Resource.Success(results)
        }catch (e:IOException){
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load intraday info."
            )
        }catch (e:HttpException){
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load intraday info."
            )
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
       return try {
           val results = api.getCompanyInfo(symbol)
           Resource.Success(results.toCompanyInfo())
       }catch (e:IOException){
           e.printStackTrace()
           Resource.Error(
               message = "Couldn't load intraday info."
           )
       }catch (e:HttpException){
           e.printStackTrace()
           Resource.Error(
               message = "Couldn't load intraday info."
           )
       }
    }
}