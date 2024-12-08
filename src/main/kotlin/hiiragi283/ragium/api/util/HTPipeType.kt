package hiiragi283.ragium.api.util

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.extension.codecOf
import hiiragi283.ragium.api.extension.longText
import hiiragi283.ragium.api.extension.packetCodecOf
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.tags.RagiumBlockTags
import hiiragi283.ragium.api.util.HTPipeType.entries
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage
import net.minecraft.block.BlockState
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import java.util.function.Consumer

enum class HTPipeType(val isItem: Boolean, val isFluid: Boolean) : StringIdentifiable {
    ALL(true, true),
    ITEM(true, false),
    FLUID(false, true),
    NONE(false, false),
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTPipeType> = codecOf(entries)

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTPipeType> = packetCodecOf(entries)

        @JvmStatic
        fun canConnect(
            world: World,
            pos: BlockPos,
            dir: Direction,
            type: HTPipeType,
        ): Boolean {
            val posTo: BlockPos = pos.offset(dir)
            val stateTo: BlockState = world.getBlockState(posTo)
            if (stateTo.isIn(RagiumBlockTags.PIPE_CONNECTABLES)) {
                return true
            }
            val existItemStorage: Boolean = ItemStorage.SIDED.find(
                world,
                posTo,
                dir.opposite,
            ) != null
            val existFluidStorage: Boolean = FluidStorage.SIDED.find(
                world,
                posTo,
                dir.opposite,
            ) != null
            return when (type) {
                ALL -> existItemStorage || existFluidStorage
                ITEM -> existItemStorage
                FLUID -> existFluidStorage
                NONE -> false
            }
        }
    }

    fun getItemCount(tier: HTMachineTier): Long = when (tier) {
        HTMachineTier.PRIMITIVE -> 8
        HTMachineTier.BASIC -> 16
        HTMachineTier.ADVANCED -> 32
    }

    fun getFluidCount(tier: HTMachineTier): Long = when (tier) {
        HTMachineTier.PRIMITIVE -> FluidConstants.INGOT
        HTMachineTier.BASIC -> FluidConstants.BOTTLE
        HTMachineTier.ADVANCED -> FluidConstants.BUCKET
    }

    fun appendTooltip(tooltip: Consumer<Text>, tier: HTMachineTier) {
        if (isItem) {
            tooltip.accept(
                Text
                    .translatable(
                        RagiumTranslationKeys.TRANSPORTER_ITEM_SPEED,
                        longText(getItemCount(tier)).formatted(Formatting.WHITE),
                    ).formatted(Formatting.GRAY),
            )
        }
        if (isFluid) {
            tooltip.accept(
                Text
                    .translatable(
                        RagiumTranslationKeys.TRANSPORTER_FLUID_SPEED,
                        longText(getFluidCount(tier)).formatted(Formatting.WHITE),
                    ).formatted(Formatting.GRAY),
            )
        }
    }

    //    StringIdentifiable    //

    override fun asString(): String = name.lowercase()
}
