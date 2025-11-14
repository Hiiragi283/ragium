package hiiragi283.ragium.impl.value

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import net.minecraft.core.HolderLookup
import net.minecraft.resources.RegistryOps

internal class HTJsonValueOutput(private val provider: HolderLookup.Provider, private val jsonObject: JsonObject) : HTValueOutput {
    private val registryOps: RegistryOps<JsonElement> = provider.createSerializationContext(JsonOps.INSTANCE)

    //    HTValueOutput    //

    override fun <T : Any> store(key: String, codec: Codec<T>, value: T?) {
        if (value == null) return
        codec
            .encodeStart(registryOps, value)
            .ifSuccess { jsonObject.add(key, it) }
    }

    override fun isEmpty(): Boolean = jsonObject.isEmpty

    override fun putBoolean(key: String, value: Boolean) {
        jsonObject.addProperty(key, value)
    }

    override fun putByte(key: String, value: Byte) {
        jsonObject.addProperty(key, value)
    }

    override fun putShort(key: String, value: Short) {
        jsonObject.addProperty(key, value)
    }

    override fun putInt(key: String, value: Int) {
        jsonObject.addProperty(key, value)
    }

    override fun putLong(key: String, value: Long) {
        jsonObject.addProperty(key, value)
    }

    override fun putFloat(key: String, value: Float) {
        jsonObject.addProperty(key, value)
    }

    override fun putDouble(key: String, value: Double) {
        jsonObject.addProperty(key, value)
    }

    override fun putString(key: String, value: String) {
        jsonObject.addProperty(key, value)
    }

    override fun child(key: String): HTValueOutput {
        val jsonIn = JsonObject()
        jsonObject.add(key, jsonIn)
        return HTJsonValueOutput(provider, jsonIn)
    }

    override fun childrenList(key: String): HTValueOutput.ValueOutputList {
        val list = JsonArray()
        jsonObject.add(key, list)
        return ValueOutputList(provider, list)
    }

    override fun <T : Any> list(key: String, codec: Codec<T>): HTValueOutput.TypedOutputList<T> {
        val list = JsonArray()
        jsonObject.add(key, list)
        return TypedOutputList(provider, list, codec)
    }

    //    ValueOutputList    //

    private class ValueOutputList(private val provider: HolderLookup.Provider, private val list: JsonArray) :
        HTValueOutput.ValueOutputList {
        override val isEmpty: Boolean
            get() = list.isEmpty

        override fun addChild(): HTValueOutput {
            val jsonIn = JsonObject()
            list.add(jsonIn)
            return HTJsonValueOutput(provider, jsonIn)
        }

        override fun discardLast() {
            list.remove(list.size() - 1)
        }
    }

    //    TypedOutputList    //

    private class TypedOutputList<T : Any>(provider: HolderLookup.Provider, private val list: JsonArray, private val codec: Codec<T>) :
        HTValueOutput.TypedOutputList<T> {
        private val registryOps: RegistryOps<JsonElement> = provider.createSerializationContext(JsonOps.INSTANCE)

        override val isEmpty: Boolean
            get() = list.isEmpty

        override fun add(element: T) {
            codec
                .encodeStart(registryOps, element)
                .ifSuccess(list::add)
        }
    }
}
