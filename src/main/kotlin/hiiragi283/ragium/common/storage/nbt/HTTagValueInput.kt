package hiiragi283.ragium.common.storage.nbt

import hiiragi283.ragium.api.data.BiCodec
import hiiragi283.ragium.api.storage.value.HTValueInput
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.NumericTag
import net.minecraft.nbt.StringTag
import net.minecraft.nbt.Tag
import net.minecraft.nbt.TagType
import net.minecraft.resources.RegistryOps
import kotlin.jvm.optionals.getOrNull

internal class HTTagValueInput private constructor(private val lookup: HolderLookup.Provider, private val compoundTag: CompoundTag) :
    HTValueInput {
        companion object {
            @JvmStatic
            fun create(lookup: HolderLookup.Provider, compoundTag: CompoundTag): HTValueInput = when {
                compoundTag.isEmpty -> HTEmptyValueInput
                else -> HTTagValueInput(lookup, compoundTag)
            }
        }

        private val registryOps: RegistryOps<Tag> = lookup.createSerializationContext(NbtOps.INSTANCE)

        private inline fun <reified T : Tag> getTypedTag(key: String, type: TagType<T>): T? {
            val tagIn: Tag = compoundTag.get(key) ?: return null
            val tagType: TagType<*> = tagIn.type
            return if (tagType == type) tagIn as T else null
        }

        private fun getNumericTag(key: String): NumericTag? {
            val tagIn: Tag = compoundTag.get(key) ?: return null
            return tagIn as? NumericTag
        }

        //    HTNbtInput    //

        override fun <T : Any> read(key: String, codec: BiCodec<*, T>): T? {
            val tagIn: Tag = compoundTag.get(key) ?: return null
            return codec.decode(registryOps, tagIn).result().getOrNull()
        }

        override fun child(key: String): HTValueInput? {
            val tagIn: CompoundTag = getTypedTag(key, CompoundTag.TYPE) ?: return null
            return create(lookup, tagIn)
        }

        override fun childOrEmpty(key: String): HTValueInput = child(key) ?: HTEmptyValueInput

        override fun childrenList(key: String): HTValueInput.ValueInputList? {
            val tagIn: ListTag = getTypedTag(key, ListTag.TYPE) ?: return null
            return when {
                tagIn.isEmpty() -> null
                else -> ValueInputList(lookup, tagIn)
            }
        }

        override fun childrenListOrEmpty(key: String): HTValueInput.ValueInputList = childrenList(key) ?: HTEmptyValueInput.EmptyInputList

        override fun <T : Any> list(key: String, codec: BiCodec<*, T>): HTValueInput.TypedInputList<T>? {
            val tagIn: ListTag = getTypedTag(key, ListTag.TYPE) ?: return null
            return when {
                tagIn.isEmpty() -> null
                else -> TypedInputList(lookup, tagIn, codec)
            }
        }

        override fun <T : Any> listOrEmpty(key: String, codec: BiCodec<*, T>): HTValueInput.TypedInputList<T> =
            list(key, codec) ?: HTEmptyValueInput.emptyTypedList()

        override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
            val tagIn: NumericTag = getNumericTag(key) ?: return defaultValue
            return tagIn.asByte != 0.toByte()
        }

        override fun getByte(key: String, defaultValue: Byte): Byte {
            val tagIn: NumericTag = getNumericTag(key) ?: return defaultValue
            return tagIn.asByte
        }

        override fun getShort(key: String, defaultValue: Short): Short {
            val tagIn: NumericTag = getNumericTag(key) ?: return defaultValue
            return tagIn.asShort
        }

        override fun getInt(key: String): Int? {
            val tagIn: NumericTag = getNumericTag(key) ?: return null
            return tagIn.asInt
        }

        override fun getInt(key: String, defaultValue: Int): Int {
            val tagIn: NumericTag = getNumericTag(key) ?: return defaultValue
            return tagIn.asInt
        }

        override fun getLong(key: String): Long? {
            val tagIn: NumericTag = getNumericTag(key) ?: return null
            return tagIn.asLong
        }

        override fun getLong(key: String, defaultValue: Long): Long {
            val tagIn: NumericTag = getNumericTag(key) ?: return defaultValue
            return tagIn.asLong
        }

        override fun getFloat(key: String, defaultValue: Float): Float {
            val tagIn: NumericTag = getNumericTag(key) ?: return defaultValue
            return tagIn.asFloat
        }

        override fun getDouble(key: String, defaultValue: Double): Double {
            val tagIn: NumericTag = getNumericTag(key) ?: return defaultValue
            return tagIn.asDouble
        }

        override fun getString(key: String): String? {
            val tagIn: StringTag = getTypedTag(key, StringTag.TYPE) ?: return null
            return tagIn.asString
        }

        override fun getString(key: String, defaultValue: String): String = getString(key) ?: defaultValue

        //    ValueInputList    //

        private class ValueInputList(private val lookup: HolderLookup.Provider, private val list: ListTag) : HTValueInput.ValueInputList {
            override val isEmpty: Boolean
                get() = list.isEmpty()

            override fun iterator(): Iterator<HTValueInput> = list
                .filterIsInstance<CompoundTag>()
                .map { compoundTag: CompoundTag ->
                    create(lookup, compoundTag)
                }.iterator()
        }

        //    TypedInputList    //

        private class TypedInputList<T : Any>(lookup: HolderLookup.Provider, private val list: ListTag, private val codec: BiCodec<*, T>) :
            HTValueInput.TypedInputList<T> {
            private val registryOps: RegistryOps<Tag> = lookup.createSerializationContext(NbtOps.INSTANCE)

            override val isEmpty: Boolean
                get() = list.isEmpty()

            override fun iterator(): Iterator<T> = list
                .mapNotNull { tag: Tag ->
                    codec.decode(registryOps, tag).result().get()
                }.iterator()
        }
    }
