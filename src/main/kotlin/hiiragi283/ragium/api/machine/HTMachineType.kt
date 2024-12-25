package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.identifiedCodec
import hiiragi283.ragium.api.extension.identifiedPacketCodec
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
        val CODEC: Codec<HTMachineType> = identifiedCodec(entries)

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTMachineType> = identifiedPacketCodec(entries)
    }

    val blockTag: TagKey<Block> = TagKey.of(RegistryKeys.BLOCK, RagiumAPI.id("machines/${asString()}"))
    val itemTag: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, RagiumAPI.id("machines/${asString()}"))

    override fun asString(): String = name.lowercase()
}
