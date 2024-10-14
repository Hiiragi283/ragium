package hiiragi283.ragium.api.component

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.extension.codecOf
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.extension.packetCodecOf
import net.minecraft.block.*
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

enum class HTRemoverDynamiteBehaviors : StringIdentifiable {
    BEDROCK {
        override fun onBlockHit(world: World, hitResult: BlockHitResult) {
            val bottomY: Int = world.bottomY
            ChunkPos(hitResult.blockPos).forEach(bottomY + 1..bottomY + 5) { pos ->
                if (world.getBlockState(pos).isOf(Blocks.BEDROCK)) {
                    world.removeBlock(pos, false)
                }
            }
        }
    },
    DRAIN {
        override fun onBlockHit(world: World, hitResult: BlockHitResult) {
            ChunkPos(hitResult.blockPos).forEach(world.bottomY + 1..hitResult.blockPos.y) { pos ->
                val state: BlockState = world.getBlockState(pos)
                val block: Block = state.block
                // drain fluid
                (block as? FluidDrainable)?.let {
                    if (!it.tryDrainFluid(null, world, pos, state).isEmpty) {
                        return@forEach
                    }
                }
                // remove fluid block
                if (block is FluidBlock) {
                    world.setBlockState(pos, Blocks.AIR.defaultState, Block.NOTIFY_ALL)
                }
                if (state.isOf(Blocks.SEAGRASS) ||
                    state.isOf(Blocks.TALL_SEAGRASS) ||
                    state.isOf(Blocks.KELP_PLANT) ||
                    state.isOf(
                        Blocks.KELP,
                    )
                ) {
                    world.setBlockState(pos, Blocks.AIR.defaultState, Block.NOTIFY_ALL)
                }
            }
        }
    },
    FLATTEN {
        override fun onBlockHit(world: World, hitResult: BlockHitResult) {
            val hitY: Int = hitResult.blockPos.y
            val minY: Int = when (hitResult.side) {
                Direction.UP -> hitY + 1
                else -> hitY
            }
            ChunkPos(hitResult.blockPos).forEach(minY..hitY + 15) { pos: BlockPos ->
                world.setBlockState(pos, Blocks.AIR.defaultState, Block.NOTIFY_ALL)
            }
        }
    },
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTRemoverDynamiteBehaviors> = codecOf(entries)

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTRemoverDynamiteBehaviors> = packetCodecOf(entries)
    }

    abstract fun onBlockHit(world: World, hitResult: BlockHitResult)

    override fun asString(): String = name.lowercase()
}
