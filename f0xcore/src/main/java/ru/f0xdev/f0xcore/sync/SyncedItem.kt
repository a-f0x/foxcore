package ru.f0xdev.f0xcore.sync

import com.google.gson.annotations.SerializedName


interface SyncedItem {
    var status: Status
    var id: Int

    class Status(
        @SerializedName("type")
        val type: Int,
        @SerializedName("text")
        val text: String
    ) {
        companion object {
            const val NOT_SYNCED = 0
            const val SYNC_ERROR = 1
            const val SYNC_IN_PROCESS = 2
            const val SYNC_SUCCESS = 3

            private const val NAME_ERROR = "Synchronization error"
            private const val NAME_SYNC_IN_PROCESS = "Synchronization..."
            private const val NAME_NOT_SYNCED = "Not synchronized"
            private const val NAME_SYNCHRONIZED = "Synchronized"

            fun createSyncErrorStatus(name: String? = null) = Status(SYNC_ERROR, name ?: NAME_ERROR)

            fun creatySyncProcessStatus(name: String? = null) =
                Status(SYNC_IN_PROCESS, name ?: NAME_SYNC_IN_PROCESS)

            fun createNotSyncedStatus(name: String? = null) = Status(NOT_SYNCED, NAME_NOT_SYNCED)

            fun createSyncSuccess(name: String? = null) =
                Status(SYNC_SUCCESS, name ?: NAME_SYNCHRONIZED)

        }
    }
}


