package com.example.android.marsrealestate.network

import android.util.Log
import com.squareup.moshi.*
import com.squareup.moshi.internal.Util


class MyAdapter : JsonAdapter<List<MarsProperty>>() {
    val moshi = Moshi.Builder().build()
    private val options: JsonReader.Options =
        JsonReader.Options.of("id", "img_src", "type", "price")

    private val stringAdapter: JsonAdapter<String> = moshi.adapter(
        String::class.java, emptySet(),
        "id"
    )

    private val doubleAdapter: JsonAdapter<Double> = moshi.adapter(
        Double::class.java, emptySet(),
        "price"
    )

    @FromJson
    override fun fromJson(reader: JsonReader): List<MarsProperty>? {

        val list = mutableListOf<MarsProperty>()
        reader.beginArray()
        while (reader.hasNext()) {
            list.add(readObject(reader))

        }
        reader.endArray()

        Log.i("List",": $list")
        return list
    }


    private fun readObject(reader: JsonReader): MarsProperty {
        var id: String? = null
        var imgSrcUrl: String? = null
        var type: String? = null
        var price: Double? = null
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.selectName(options)) {
                0 -> id = stringAdapter.fromJson(reader)
                1 -> imgSrcUrl = stringAdapter.fromJson(reader)
                2 -> type = stringAdapter.fromJson(reader)
                3 -> price = doubleAdapter.fromJson(reader)
                -1 -> {
                    reader.skipName()
                    reader.skipValue()
                }

            }

        }
        reader.endObject()
        return MarsProperty(
            id = id ?: throw Util.missingProperty("id", "id", reader),
            imgSrcUrl = imgSrcUrl ?: throw Util.missingProperty("imgSrcUrl", "img_src", reader),
            type = type ?: throw Util.missingProperty("type", "type", reader),
            price = price ?: throw Util.missingProperty("price", "price", reader)
        )

    }

    override fun toJson(writer: JsonWriter, value: List<MarsProperty>?) {
        TODO("Not yet implemented")
    }


}