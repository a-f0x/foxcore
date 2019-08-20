package ru.f0xdev.appcoreexample.main.users

interface IUsersRepository {
    suspend fun getUsersList(page: Int): List<User>
}

class UserRepository(private val remoteDataSource: IUsersRemoteDataSource) : IUsersRepository {
    override suspend fun getUsersList(page: Int): List<User> {
        return remoteDataSource.getUsersList(page).usersList
    }
}

