package hiiragi283.ragium.api.content

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.registry.Registry
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Identifier

sealed interface HTRegistryEntryList<T : Any> : Iterable<T> {
    companion object {
        @JvmStatic
        fun <T : Any> codec(registry: Registry<T>): Codec<HTRegistryEntryList<T>> =
            Codec.either(TagKey.codec(registry.key), registry.entryCodec).xmap(
                { either: Either<TagKey<T>, RegistryEntry<T>> -> either.map({ ofTag(it, registry) }, ::of) },
                { entryList: HTRegistryEntryList<T> -> entryList.storage.mapRight(registry::getEntry) },
            )

        @JvmStatic
        fun <T : Any> packetCodec(registry: Registry<T>): PacketCodec<RegistryByteBuf, HTRegistryEntryList<T>> =
            object : PacketCodec<RegistryByteBuf, HTRegistryEntryList<T>> {
                override fun decode(buf: RegistryByteBuf): HTRegistryEntryList<T> {
                    val isTag: Boolean = buf.readBoolean()
                    val id: Identifier = buf.readIdentifier()
                    return when (isTag) {
                        true -> ofTag(TagKey.of(registry.key, id), registry)
                        false -> of(registry.getEntry(id).orElseThrow())
                    }
                }

                override fun encode(buf: RegistryByteBuf, value: HTRegistryEntryList<T>) {
                    value.storage
                        .ifLeft { tagKey: TagKey<T> ->
                            buf.writeBoolean(true)
                            buf.writeIdentifier(tagKey.id)
                        }.ifRight { entry: T ->
                            buf.writeBoolean(false)
                            buf.writeIdentifier(registry.getId(entry))
                        }
                }
            }

        @JvmStatic
        fun <T : Any> of(entry: RegistryEntry<T>): HTRegistryEntryList<T> = of(entry.value())

        @JvmStatic
        fun <T : Any> of(entry: T): HTRegistryEntryList<T> = Direct(entry)

        @JvmStatic
        fun <T : Any> ofTag(tagKey: TagKey<T>, registry: Registry<T>): HTRegistryEntryList<T> = Tag(tagKey, registry)
    }

    val isEmpty: Boolean

    val size: Int

    val storage: Either<TagKey<T>, T>

    operator fun get(index: Int): T

    fun getText(transform: (T) -> Text): MutableText = storage.map(TagKey<T>::getName, transform).copy()

    //    Direct    //

    private class Direct<T : Any>(val entry: T) : HTRegistryEntryList<T> {
        override val isEmpty: Boolean = false

        override val size: Int = 1

        override val storage: Either<TagKey<T>, T> = Either.right(entry)

        override fun get(index: Int): T = when (index) {
            0 -> entry
            else -> throw IndexOutOfBoundsException()
        }

        override fun iterator(): Iterator<T> = listOf(entry).iterator()
    }

    //    Tag    //

    private class Tag<T : Any>(val tagKey: TagKey<T>, val registry: Registry<T>) : HTRegistryEntryList<T> {
        private val entries: List<T>
            get() = registry.iterateEntries(tagKey).map(RegistryEntry<T>::value)

        override val isEmpty: Boolean
            get() = entries.isEmpty()

        override val size: Int
            get() = entries.size

        override val storage: Either<TagKey<T>, T> = Either.left(tagKey)

        override fun get(index: Int): T = entries[index]

        override fun iterator(): Iterator<T> = entries.iterator()
    }
}
