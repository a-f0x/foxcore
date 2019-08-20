package ru.f0xdev.appcoreexample.main.settings

import kotlinx.coroutines.delay
import ru.f0xdev.appcoreexample.main.users.UsersListItem

interface IProfileRepository {

    suspend fun loadProfile(): UserProfile
}


class ProfileRepository : IProfileRepository {
    override suspend fun loadProfile(): UserProfile {
        delay(500)
        return UserProfile(
            UsersListItem(1, "eve.holt@reqres.in", "TestFirstName", "TestLastName", "")
        )
    }

}