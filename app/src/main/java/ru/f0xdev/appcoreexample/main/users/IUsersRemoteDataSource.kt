package ru.f0xdev.appcoreexample.main.users

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query


data class UsersPages(
    @SerializedName("page")
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("data")
    val usersList: List<User>
)


interface IUsersRemoteDataSource {
    suspend fun getUsersList(page: Int): UsersPages
}


class UsersRemoteDataSource(private val api: IUsersRestApi) : IUsersRemoteDataSource {
    override suspend fun getUsersList(page: Int): UsersPages {
        return api.loadUsers(page)
    }

    interface IUsersRestApi {
        @GET("/api/users")
        suspend fun loadUsers(@Query("page") page: Int): UsersPages
    }
}