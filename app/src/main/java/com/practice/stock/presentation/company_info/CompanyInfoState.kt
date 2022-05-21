package com.practice.stock.presentation.company_info

import com.practice.stock.domain.model.CompanyInfo
import com.practice.stock.domain.model.IntradayInfo

data class CompanyInfoState(
    val stockInfos: List<IntradayInfo> = emptyList(),
    val companyInfo: CompanyInfo? = null,
    val isLoading:Boolean = false,
    val error:String?=null
)
