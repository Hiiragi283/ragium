package hiiragi283.ragium.common.machine

import com.mojang.serialization.Codec
import hiiragi283.ragium.common.Ragium
import io.netty.buffer.ByteBuf
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.data.client.ModelIds
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.function.ValueLists
import java.util.function.IntFunction

enum class HTMachineTier(
    val casingTex: Identifier,
    val baseBlock: Block,
    val recipeCost: Long,
    rarity: Rarity,
) : StringIdentifiable {
    NONE(Ragium.id("block/ragi_alloy_block"), Blocks.SMOOTH_STONE, 80, Rarity.COMMON),
    PRIMITIVE(Ragium.id("block/ragi_alloy_block"), Blocks.BRICKS, 320, Rarity.UNCOMMON),
    BASIC(Ragium.id("block/ragi_steel_block"), Blocks.POLISHED_BLACKSTONE_BRICKS, 1280, Rarity.RARE),
    ADVANCED(Ragium.id("block/refined_ragi_steel_block"), Blocks.END_STONE_BRICKS, 5120, Rarity.EPIC),
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTMachineTier> = StringIdentifiable.createCodec(HTMachineTier::values)

        @JvmField
        val INT_FUNCTION: IntFunction<HTMachineTier> =
            ValueLists.createIdToValueFunction(
                HTMachineTier::ordinal,
                entries.toTypedArray(),
                ValueLists.OutOfBoundsHandling.WRAP,
            )

        @JvmField
        val PACKET_CODEC: PacketCodec<ByteBuf, HTMachineTier> =
            PacketCodecs.indexed(INT_FUNCTION, HTMachineTier::ordinal)
    }

    val baseTex: Identifier = ModelIds.getBlockModelId(baseBlock)

    val translationKey: String = "machine_tier.ragium.${asString()}"
    val text: Text = Text.translatable(translationKey).formatted(rarity.formatting)

    val energyCapacity: Long = recipeCost * 16

    //    StringIdentifiable    //

    override fun asString(): String = name.lowercase()
}
