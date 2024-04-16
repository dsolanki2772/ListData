package com.demo.listdata

class MainRepository constructor(private val retrofitService: RetrofitService) {

    suspend fun getList(page: Int,limit: Int) : NetworkState<List<Item>> {
        val response = retrofitService.getList(page, limit)
        return if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                NetworkState.Success(responseBody)
            } else {
                NetworkState.Error(response)
            }
        } else {
            NetworkState.Error(response)
        }
    }

}