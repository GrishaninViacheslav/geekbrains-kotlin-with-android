package GeekBrians.Slava_5655380.domain.model.repositoryimpl.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieUserDataEntity(
    @PrimaryKey
    val id: String,
    val userScore: Int
)