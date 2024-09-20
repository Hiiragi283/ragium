package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.common.block.HTKineticNode
import hiiragi283.ragium.common.block.entity.HTBaseBlockEntity
import hiiragi283.ragium.common.block.entity.HTKineticProcessor
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

abstract class HTKineticGeneratorBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTBaseBlockEntity(type, pos, state) {
    companion object {
        @JvmStatic
        private fun findProcessor(world: World, pos: BlockPos, state: BlockState): BlockPos? {
            val toDirection: Direction = state.get(Properties.HORIZONTAL_FACING)
            val toPos: BlockPos = pos.offset(toDirection)
            return when (val toBlock: Block = world.getBlockState(toPos).block) {
                is HTKineticNode -> toBlock.findProcessor(world, toPos, toDirection.opposite)
                is HTKineticProcessor -> toPos
                else -> null
            }
        }
    }

    var ticks: Int = 0
        protected set
    private var processorCache: HTKineticProcessor? = null

    abstract fun canProvidePower(world: World, pos: BlockPos, state: BlockState): Boolean

    override fun tick(world: World, pos: BlockPos, state: BlockState) {
        if (ticks >= 20) {
            ticks = 0
            if (canProvidePower(world, pos, state)) {
                // try to load processorCache if not null
                val endPos: BlockPos? = findProcessor(world, pos, state)
                processorCache?.let { cache: HTKineticProcessor ->
                    val cachedPos: BlockPos = cache.getPos()
                    if (cache == world.getBlockEntity(cachedPos) && cachedPos == endPos) {
                        cache.onActive(world, cachedPos)
                    } else {
                        cache.onInactive(world, cachedPos)
                    }
                    return
                }
                // try to find new processorCache
                endPos?.let { processorPos: BlockPos ->
                    (world.getBlockEntity(processorPos) as? HTKineticProcessor)?.let {
                        processorCache = it
                        it.onActive(world, processorPos)
                        return
                    }
                }
                processorCache = null
            }
        } else {
            ticks++
        }
    } 
}
