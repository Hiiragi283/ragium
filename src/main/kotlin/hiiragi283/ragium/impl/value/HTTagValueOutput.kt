package hiiragi283.ragium.impl.value

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps

internal class HTTagValueOutput(private val provider: HolderLookup.Provider, private val compoundTag: CompoundTag) : HTValueOutput {
    private val registryOps: RegistryOps<Tag> = provider.createSerializationContext(NbtOps.INSTANCE)

    //    HTValueOutput    //

    override fun <T : Any> store(key: String, codec: Codec<T>, value: T?) {
        if (value == null) return
        codec
            .encodeStart(registryOps, value)
            .ifSuccess { compoundTag.put(key, it) }
    }

    override fun isEmpty(): Boolean = compoundTag.isEmpty

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
        return HTTagValueOutput(provider, tagIn)
    }

    override fun childrenList(key: String): HTValueOutput.ValueOutputList {
        val list = ListTag()
        compoundTag.put(key, list)
        return ValueOutputList(provider, list)
    }

    override fun <T : Any> list(key: String, codec: Codec<T>): HTValueOutput.TypedOutputList<T> {
        val list = ListTag()
        compoundTag.put(key, list)
        return TypedOutputList(provider, list, codec)
    }

    //    ValueOutputList    //

    private class ValueOutputList(private val provider: HolderLookup.Provider, private val list: ListTag) : HTValueOutput.ValueOutputList {
        override val isEmpty: Boolean
            get() = list.isEmpty()

        override fun addChild(): HTValueOutput {
            val tagIn = CompoundTag()
            list.add(tagIn)
            return HTTagValueOutput(provider, tagIn)
        }

        override fun discardLast() {
            list.removeLast()
        }
    }

    //    TypedOutputList    //

    private class TypedOutputList<T : Any>(provider: HolderLookup.Provider, private val list: ListTag, private val codec: Codec<T>) :
        HTValueOutput.TypedOutputList<T> {
        private val registryOps: RegistryOps<Tag> = provider.createSerializationContext(NbtOps.INSTANCE)

        override val isEmpty: Boolean
            get() = list.isEmpty()

        override fun add(element: T) {
            codec
                .encodeStart(registryOps, element)
                .ifSuccess(list::add)
        }
    }
}
