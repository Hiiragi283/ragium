package hiiragi283.ragium.common.component

import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.common.entity.HTDynamiteEntity
import net.minecraft.block.*
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
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
            ChunkPos(hitResult.blockPos).forEach(minY..world.height) { pos: BlockPos ->
                world.setBlockState(pos, Blocks.AIR.defaultState, Block.NOTIFY_ALL)
            }
        }
    },
    ;

    abstract fun onBlockHit(world: World, hitResult: BlockHitResult)

    fun onBlockHit(entity: HTDynamiteEntity, hitResult: HitResult) {
        if (hitResult is BlockHitResult) {
            onBlockHit(entity.world, hitResult)
        }
    }

    override fun asString(): String = name.lowercase()
}
