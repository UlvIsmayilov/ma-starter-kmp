package az.pashabank.starter.data.di

import androidx.room.RoomDatabase
import org.koin.core.scope.Scope

expect inline fun <reified T : RoomDatabase> Scope.createRoomDatabase(
    dbName: String,
    builder: RoomDatabase.Builder<T>.() -> T,
): T