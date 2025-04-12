package com.example.traderapp.data.model

data class UserData(val email: String = "",
                    val balance: Double = 1000.00,
                    val tradeVolume: Int = 0,
                    val profit: Int = 0,
                    val cryptoList: List<CryptoDto> = emptyList(),
                    val initialTotalBalance: Double = 1000.00,
                    )
