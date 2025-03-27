package com.mudrichenkoevgeny.movierating.network.restapi

import com.mudrichenkoevgeny.core.model.network.ReviewNetwork
import com.mudrichenkoevgeny.core.model.network.TagNetwork
import com.mudrichenkoevgeny.core.network.result.RestApiResult
import com.mudrichenkoevgeny.movierating.model.network.GenreNetwork
import com.mudrichenkoevgeny.movierating.model.network.MovieNetwork
import com.mudrichenkoevgeny.movierating.model.network.response.MovieListResponse
import com.mudrichenkoevgeny.movierating.model.network.response.ReviewListResponse
import com.mudrichenkoevgeny.movierating.network.requestbody.SaveReviewRequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieRestApi {

    @GET("movie/tags")
    suspend fun getTags(): RestApiResult<List<TagNetwork>>

    @GET("movie/genres")
    suspend fun getGenres(): RestApiResult<List<GenreNetwork>>

    @GET("movie/feed")
    suspend fun getGlobalFeed(@Query("page") page: Int): RestApiResult<MovieListResponse>

    @GET("movie/user-reviews")
    suspend fun getUserFeed(@Query("page") page: Int): RestApiResult<MovieListResponse>

    @GET("movie")
    suspend fun getMovie(@Query("id") id: String): RestApiResult<MovieNetwork>

    @GET("movie/user-reviews")
    suspend fun getReviewList(
        @Query("movieId") movieId: String,
        @Query("page") page: Int
    ): RestApiResult<ReviewListResponse>

    @POST("movie/save-review")
    suspend fun saveReview(@Body request: SaveReviewRequestBody): RestApiResult<ReviewNetwork>

    @DELETE("movie/delete-review/{id}")
    suspend fun deleteReview(@Path("id") id: String): RestApiResult<Unit>
}