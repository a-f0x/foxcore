package ru.f0xdev.appcoreexample.main.users

import kotlin.random.Random

interface IUsersInteractor {

    suspend fun loadUsers(): List<UsersListItem>
}

class UsersInteractor(private val usersRepository: IUsersRepository) : IUsersInteractor {

    private val statuses = arrayOf("Online", " Offline")

    override suspend fun loadUsers(): List<UsersListItem> {
        return usersRepository.getUsersList(1).map {
            UsersListItem(
                it.id,
                it.email,
                it.firstName,
                it.lastName,
                it.avatar,
                status = statuses[Random.nextInt(0, statuses.size)]
            )
        }.sortedByDescending { it.status }
    }

}