package GeekBrians.Slava_5655380.domain.model.repositoryimpl.room

import androidx.room.*

@Dao
interface MovieUserDataDAO {
    @Query("SELECT * FROM MovieUserDataEntity WHERE id LIKE :movieId")
    fun getDataById (movieId: String): MovieUserDataEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: MovieUserDataEntity)

    @Update
    fun update(entity: MovieUserDataEntity)
}