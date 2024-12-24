package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.codecOf
import hiiragi283.ragium.api.extension.packetCodecOf
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.StringIdentifiable

/**
 * 機械のタイプを表す列挙型
 */
enum class HTMachineType : StringIdentifiable {
    GENERATOR,
    PROCESSOR,
    CONSUMER,
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTMachineType> = codecOf(entries)

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTMachineType> = packetCodecOf(entries)
    }

    val blockTag: TagKey<Block> = TagKey.of(RegistryKeys.BLOCK, RagiumAPI.id("machines/${asString()}"))
    val itemTag: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, RagiumAPI.id("machines/${asString()}"))

    override fun asString(): String = name.lowercase()
}
