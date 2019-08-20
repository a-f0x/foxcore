package ru.f0xdev.appcoreexample.main.users

interface IUsersInteractor {

    suspend fun loadUsers(): List<UsersListItem>
}

class UsersInteractor(private val usersRepository: IUsersRepository) : IUsersInteractor {

    override suspend fun loadUsers(): List<UsersListItem> {
        return usersRepository.getUsersList(1).map {
            UsersListItem(it.id, it.email, it.firstName, it.lastName, it.avatar)
        }
    }

}