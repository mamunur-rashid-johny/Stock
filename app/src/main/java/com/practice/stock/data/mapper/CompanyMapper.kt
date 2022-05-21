package com.practice.stock.data.mapper

import com.practice.stock.data.local.CompanyListingEntity
import com.practice.stock.data.remote.dto.CompanyInfoDto
import com.practice.stock.domain.model.CompanyInfo
import com.practice.stock.domain.model.CompanyListing


fun CompanyListingEntity.toCompanyListing(): CompanyListing {
    return CompanyListing(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyListing.toCompanyListingEntity(): CompanyListingEntity {
    return CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyInfoDto.toCompanyInfo(): CompanyInfo {
    return CompanyInfo(
        symbol = symbol?:"",
        description = description?:"",
        name = name?:"",
        country = country?:"",
        industry = industry?:""
    )
}