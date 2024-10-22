package com.example.studention


// api/TuApiService.kt
import retrofit2.http.GET
import retrofit2.http.Path



interface ApiService{

    @GET("/")
    suspend fun getBase() : BaseResponse
    //Sin saber el tipo de dato que devuelve la API, se deja suspend fun getBase() y se cambia el tipo de dato a String


    @GET("/votes")
    suspend fun getVotes() : List<VoteResponse>

    @GET("/votes/{id}")
    suspend fun getVote(@Path("id") id: Int) : VoteResponse




}
