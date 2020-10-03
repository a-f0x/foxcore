package ru.f0xdev.f0xcore.base.navigation.routing

import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router
import java.util.*

class LocalCiceroneHolder {
    private val containers: HashMap<String, Cicerone<Router>> = HashMap()

    fun getCicerone(containerTag: String): Cicerone<Router> {
        if (containers.containsKey(containerTag)) {
            return containers[containerTag]!!
        }

        val cicerone = Cicerone.create()
        containers[containerTag] = cicerone
        return cicerone
    }
}
