package hiiragi283.ragium.api.machine

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

/**
 * [HTMachineBlockEntity]を返すファクトリー
 */
fun interface HTMachineEntityFactory {
    companion object {
        /**
         * 固定の[HTMachineKey]を持った[HTMachineBlockEntity]向けの[HTMachineEntityFactory]
         */
        @JvmStatic
        fun of(factory: (BlockPos, BlockState) -> HTMachineBlockEntity?): HTMachineEntityFactory =
            HTMachineEntityFactory { pos: BlockPos, state: BlockState, _: HTMachineKey ->
                factory(pos, state)
            }
    }

    fun create(pos: BlockPos, state: BlockState, key: HTMachineKey): HTMachineBlockEntity?
}
