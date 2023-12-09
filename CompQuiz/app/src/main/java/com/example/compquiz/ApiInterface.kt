package com.example.compquiz

import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("api.php?amount=10&category=18&difficulty=easy&type=multiple")
    fun getQuestion():Call<QuestionModel>

}