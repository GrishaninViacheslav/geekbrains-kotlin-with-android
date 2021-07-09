package GeekBrians.Slava_5655380.domain.model.repositoryimpl.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(MovieUserDataEntity::class), version = 1, exportSchema = false)
abstract class MovieUserDataDB : RoomDatabase() {
    abstract fun movieUserDataDao(): MovieUserDataDAO
}