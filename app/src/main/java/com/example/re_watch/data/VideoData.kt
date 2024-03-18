import android.os.Parcel
import android.os.Parcelable

data class VideoData(
    val userDisplayName: String,
    val uploadTime: String,
    val videoUrl: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userDisplayName)
        parcel.writeString(uploadTime)
        parcel.writeString(videoUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VideoData> {
        override fun createFromParcel(parcel: Parcel): VideoData {
            return VideoData(parcel)
        }

        override fun newArray(size: Int): Array<VideoData?> {
            return arrayOfNulls(size)
        }
    }
}
