package az.pashabank.starter.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import org.koin.core.scope.Scope


actual inline fun <reified T : RoomDatabase> Scope.createRoomDatabase(
    dbName: String,
    builder: RoomDatabase.Builder<T>.() -> T,
): T {
    val appContext = get<Context>().applicationContext
    val dbFile = appContext.getDatabasePath(dbName)
    return Room.databaseBuilder<T>(context = appContext, name = dbFile.absolutePath)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .builder()


}