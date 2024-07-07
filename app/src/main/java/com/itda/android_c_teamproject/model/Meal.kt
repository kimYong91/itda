package com.itda.android_c_teamproject.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "FoodDiet")
data class Meal(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "mealType") var mealType: String,
    @ColumnInfo(name = "content") var content: String,
    @ColumnInfo(name = "energy") var energy: Float,
    @ColumnInfo(name = "protein") var protein: Float,
    @ColumnInfo(name = "fat") var fat: Float,
    @ColumnInfo(name = "carbs") var carbs: Float,
    @ColumnInfo(name = "date") var date: Date = Date()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        Date(parcel.readLong()) // Date 읽기 추가
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(mealType)
        parcel.writeString(content)
        parcel.writeFloat(energy)
        parcel.writeFloat(protein)
        parcel.writeFloat(fat)
        parcel.writeFloat(carbs)
        parcel.writeLong(date.time) // Date 쓰기 추가
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Meal> {
        override fun createFromParcel(parcel: Parcel): Meal {
            return Meal(parcel)
        }

        override fun newArray(size: Int): Array<Meal?> {
            return arrayOfNulls(size)
        }
    }
}