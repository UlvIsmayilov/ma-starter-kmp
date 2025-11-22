package az.pashabank.starter.data.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.scope.Scope
import platform.Foundation.NSHomeDirectory

actual inline fun <reified T : RoomDatabase> Scope.createRoomDatabase(
    dbName: String,
    builder: RoomDatabase.Builder<T>.() -> T,
): T {
    val dbFilePath = NSHomeDirectory() + "/" + dbName
    return Room.databaseBuilder<T>(name = dbFilePath, factory = { T::class.instantiateImpl() })
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .builder()

}