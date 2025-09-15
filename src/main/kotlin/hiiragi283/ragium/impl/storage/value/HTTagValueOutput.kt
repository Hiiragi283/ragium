package hiiragi283.ragium.impl.storage.value

import hiiragi283.ragium.api.codec.BiCodec
import hiiragi283.ragium.api.storage.value.HTValueOutput
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps

internal class HTTagValueOutput(private val lookup: HolderLookup.Provider, private val compoundTag: CompoundTag) : HTValueOutput {
    private val registryOps: RegistryOps<Tag> = lookup.createSerializationContext(NbtOps.INSTANCE)

    //    HTValueOutput    //

    override fun <T : Any> store(key: String, codec: BiCodec<*, T>, value: T?) {
        if (value == null) return
        codec
            .encode(registryOps, value)
            .ifSuccess { compoundTag.put(key, it) }
    }

    override fun putBoolean(key: String, value: Boolean) {
        compoundTag.putBoolean(key, value)
    }

    override fun putByte(key: String, value: Byte) {
        compoundTag.putByte(key, value)
    }

    override fun putShort(key: String, value: Short) {
        compoundTag.putShort(key, value)
    }

    override fun putInt(key: String, value: Int) {
        compoundTag.putInt(key, value)
    }

    override fun putLong(key: String, value: Long) {
        compoundTag.putLong(key, value)
    }

    override fun putFloat(key: String, value: Float) {
        compoundTag.putFloat(key, value)
    }

    override fun putDouble(key: String, value: Double) {
        compoundTag.putDouble(key, value)
    }

    override fun putString(key: String, value: String) {
        compoundTag.putString(key, value)
    }

    override fun child(key: String): HTValueOutput {
        val tagIn = CompoundTag()
        compoundTag.put(key, tagIn)
        return HTTagValueOutput(lookup, tagIn)
    }

    override fun childrenList(key: String): HTValueOutput.ValueOutputList {
        val list = ListTag()
        compoundTag.put(key, list)
        return ValueOutputList(lookup, list)
    }

    override fun <T : Any> list(key: String, codec: BiCodec<*, T>): HTValueOutput.TypedOutputList<T> {
        val list = ListTag()
        compoundTag.put(key, list)
        return TypedOutputList(lookup, list, codec)
    }

    //    ValueOutputList    //

    private class ValueOutputList(private val lookup: HolderLookup.Provider, private val list: ListTag) : HTValueOutput.ValueOutputList {
        override val isEmpty: Boolean
            get() = list.isEmpty()

        override fun addChild(): HTValueOutput {
            val tagIn = CompoundTag()
            list.add(tagIn)
            return HTTagValueOutput(lookup, tagIn)
        }

        override fun discardLast() {
            list.removeLast()
        }
    }

    //    TypedOutputList    //

    private class TypedOutputList<T : Any>(lookup: HolderLookup.Provider, private val list: ListTag, private val codec: BiCodec<*, T>) :
        HTValueOutput.TypedOutputList<T> {
        private val registryOps: RegistryOps<Tag> = lookup.createSerializationContext(NbtOps.INSTANCE)

        override val isEmpty: Boolean
            get() = list.isEmpty()

        override fun add(element: T) {
            codec
                .encode(registryOps, element)
                .ifSuccess(list::add)
        }
    }
}
