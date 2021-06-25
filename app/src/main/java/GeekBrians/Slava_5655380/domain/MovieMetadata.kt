package GeekBrians.Slava_5655380.domain

import android.os.Parcel
import android.os.Parcelable

class MovieMetadata(
    val id: String,
    var index: Int,
    var userScore: Int? = null,
    var originalTitle: String? = null,
    var description: String? = null,
    var posterUri: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeInt(index)
        parcel.writeValue(userScore)
        parcel.writeString(originalTitle)
        parcel.writeString(description)
        parcel.writeString(posterUri)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MovieMetadata> {
        override fun createFromParcel(parcel: Parcel): MovieMetadata {
            return MovieMetadata(parcel)
        }

        override fun newArray(size: Int): Array<MovieMetadata?> {
            return arrayOfNulls(size)
        }
    }
}