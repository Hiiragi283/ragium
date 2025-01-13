package hiiragi283.ragium.api.machine

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

/**
 * [HTMachineBlockEntityBase]を返すファクトリー
 */
fun interface HTMachineEntityFactory {
    companion object {
        /**
         * 固定の[HTMachineKey]を持った[HTMachineBlockEntityBase]向けの[HTMachineEntityFactory]
         */
        @JvmStatic
        fun of(factory: (BlockPos, BlockState) -> BlockEntity?): HTMachineEntityFactory =
            HTMachineEntityFactory { pos: BlockPos, state: BlockState, _: HTMachineKey ->
                factory(pos, state)
            }
    }

    fun create(
        pos: BlockPos,
        state: BlockState,
        key: HTMachineKey,
    ): BlockEntity?
}
