package hiiragi283.ragium.common.machine

import com.mojang.serialization.Codec
import hiiragi283.ragium.common.Ragium
import io.netty.buffer.ByteBuf
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.data.client.ModelIds
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.util.Identifier
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.function.ValueLists
import java.util.function.IntFunction

enum class HTMachineTier(val casingTex: Identifier, val baseBlock: Block) : StringIdentifiable {
    NONE(Ragium.id("block/ragi_alloy_block"), Blocks.SMOOTH_STONE),
    PRIMITIVE(Ragium.id("block/ragi_alloy_block"), Blocks.BRICKS),
    BASIC(Ragium.id("block/ragi_steel_block"), Blocks.POLISHED_BLACKSTONE_BRICKS),
    ADVANCED(Ragium.id("block/refined_ragi_steel_block"), Blocks.END_STONE_BRICKS),
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

    //    StringIdentifiable    //

    override fun asString(): String = name.lowercase()
}
