package hiiragi283.ragium.common.block

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.extension.codecOf
import hiiragi283.ragium.api.extension.packetCodecOf
import hiiragi283.ragium.api.tags.RagiumBlockTags
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage
import net.minecraft.block.BlockState
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

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

    override fun asString(): String = name.lowercase()
}
