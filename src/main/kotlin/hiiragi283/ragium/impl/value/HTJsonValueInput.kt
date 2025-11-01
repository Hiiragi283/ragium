package hiiragi283.ragium.impl.value

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.value.HTValueInput
import net.minecraft.core.HolderLookup
import net.minecraft.resources.RegistryOps
import kotlin.jvm.optionals.getOrNull

internal class HTJsonValueInput private constructor(private val lookup: HolderLookup.Provider, private val jsonObject: JsonObject) :
    HTValueInput {
        companion object {
            @JvmStatic
            fun create(lookup: HolderLookup.Provider, jsonObject: JsonObject): HTValueInput = when {
                jsonObject.isEmpty -> HTEmptyValueInput
                else -> HTJsonValueInput(lookup, jsonObject)
            }
        }

        private val registryOps: RegistryOps<JsonElement> = lookup.createSerializationContext(JsonOps.INSTANCE)

        private fun getJsonPrimitive(key: String): JsonPrimitive? {
            val jsonIn: JsonElement = jsonObject.get(key) ?: return null
            return jsonIn.takeIf(JsonElement::isJsonPrimitive)?.asJsonPrimitive
        }

        //    HTNbtInput    //

        override fun <T : Any> read(key: String, codec: Codec<T>): T? {
            val jsonIn: JsonElement = jsonObject.get(key) ?: return null
            return codec.parse(registryOps, jsonIn).result().getOrNull()
        }

        override fun child(key: String): HTValueInput? {
            val jsonIn: JsonObject = jsonObject.get(key) as? JsonObject ?: return null
            return create(lookup, jsonIn)
        }

        override fun childOrEmpty(key: String): HTValueInput = child(key) ?: HTEmptyValueInput

        override fun childrenList(key: String): HTValueInput.ValueInputList? {
            val list: JsonArray = jsonObject.get(key) as? JsonArray ?: return null
            return when {
                list.isEmpty -> null
                else -> ValueInputList(lookup, list)
            }
        }

        override fun childrenListOrEmpty(key: String): HTValueInput.ValueInputList = childrenList(key) ?: HTEmptyValueInput.EmptyInputList

        override fun <T : Any> list(key: String, codec: BiCodec<*, T>): HTValueInput.TypedInputList<T>? {
            val list: JsonArray = jsonObject.get(key) as? JsonArray ?: return null
            return when {
                list.isEmpty -> null
                else -> TypedInputList(lookup, list, codec)
            }
        }

        override fun <T : Any> listOrEmpty(key: String, codec: BiCodec<*, T>): HTValueInput.TypedInputList<T> =
            list(key, codec) ?: HTEmptyValueInput.emptyTypedList()

        override fun getBoolean(key: String, defaultValue: Boolean): Boolean =
            getJsonPrimitive(key)?.takeIf(JsonPrimitive::isBoolean)?.asBoolean ?: defaultValue

        override fun getByte(key: String, defaultValue: Byte): Byte =
            getJsonPrimitive(key)?.takeIf(JsonPrimitive::isNumber)?.asByte ?: defaultValue

        override fun getShort(key: String, defaultValue: Short): Short =
            getJsonPrimitive(key)?.takeIf(JsonPrimitive::isNumber)?.asShort ?: defaultValue

        override fun getInt(key: String): Int? = getJsonPrimitive(key)?.takeIf(JsonPrimitive::isNumber)?.asInt

        override fun getInt(key: String, defaultValue: Int): Int =
            getJsonPrimitive(key)?.takeIf(JsonPrimitive::isNumber)?.asInt ?: defaultValue

        override fun getLong(key: String): Long? = getJsonPrimitive(key)?.takeIf(JsonPrimitive::isNumber)?.asLong

        override fun getLong(key: String, defaultValue: Long): Long =
            getJsonPrimitive(key)?.takeIf(JsonPrimitive::isNumber)?.asLong ?: defaultValue

        override fun getFloat(key: String, defaultValue: Float): Float =
            getJsonPrimitive(key)?.takeIf(JsonPrimitive::isNumber)?.asFloat ?: defaultValue

        override fun getDouble(key: String, defaultValue: Double): Double =
            getJsonPrimitive(key)?.takeIf(JsonPrimitive::isNumber)?.asDouble ?: defaultValue

        override fun getString(key: String): String? = getJsonPrimitive(key)?.takeIf(JsonPrimitive::isString)?.asString

        override fun getString(key: String, defaultValue: String): String = getString(key) ?: defaultValue

        //    ValueInputList    //

        private class ValueInputList(private val lookup: HolderLookup.Provider, private val list: JsonArray) : HTValueInput.ValueInputList {
            override val isEmpty: Boolean
                get() = list.isEmpty

            override fun iterator(): Iterator<HTValueInput> = list
                .filterIsInstance<JsonObject>()
                .map { jsonObject: JsonObject ->
                    create(lookup, jsonObject)
                }.iterator()
        }

        //    TypedInputList    //

        private class TypedInputList<T : Any>(
            lookup: HolderLookup.Provider,
            private val list: JsonArray,
            private val codec: BiCodec<*, T>,
        ) : HTValueInput.TypedInputList<T> {
            private val registryOps: RegistryOps<JsonElement> = lookup.createSerializationContext(JsonOps.INSTANCE)

            override val isEmpty: Boolean
                get() = list.isEmpty

            override fun iterator(): Iterator<T> = list
                .mapNotNull { json: JsonElement ->
                    codec.decode(registryOps, json).getOrNull()
                }.iterator()
        }
    }
