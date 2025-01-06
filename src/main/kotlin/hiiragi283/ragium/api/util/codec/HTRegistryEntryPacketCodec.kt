package hiiragi283.ragium.api.util.codec

import hiiragi283.ragium.api.extension.toList
import hiiragi283.ragium.api.util.HTRegistryEntryList
import hiiragi283.ragium.api.util.HTTagValueGetter
import io.netty.buffer.ByteBuf
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

class HTRegistryEntryPacketCodec<E : Any>(
    private val registryKey: RegistryKey<out Registry<E>>,
    private val entryCodec: PacketCodec<ByteBuf, E>,
    private val valueGetter: HTTagValueGetter<E>,
) : PacketCodec<RegistryByteBuf, HTRegistryEntryList<E>> {
    constructor(registry: Registry<E>) : this(
        registry.key,
        PacketCodecs.codec(registry.codec),
        registry::iterateEntries,
    )

    override fun decode(buf: RegistryByteBuf): HTRegistryEntryList<E> {
        val bool: Boolean = buf.readBoolean()
        if (bool) {
            val id: Identifier = buf.readIdentifier()
            val tagKey: TagKey<E> = TagKey.of(registryKey, id)
            return HTRegistryEntryList.fromTag(tagKey, valueGetter)
        } else {
            val entries: List<E> = entryCodec.toList().decode(buf)
            return HTRegistryEntryList.direct(entries)
        }
    }

    override fun encode(buf: RegistryByteBuf, value: HTRegistryEntryList<E>) {
        value.storage.map(
            { tagKey: TagKey<E> ->
                buf.writeBoolean(true)
                buf.writeIdentifier(tagKey.id)
            },
            { entries: List<E> ->
                buf.writeBoolean(false)
                entryCodec.toList().encode(buf, entries)
            },
        )
    }
}
