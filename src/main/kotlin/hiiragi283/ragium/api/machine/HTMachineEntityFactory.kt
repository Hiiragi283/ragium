package hiiragi283.ragium.api.machine

import hiiragi283.ragium.api.block.HTMachineBlockEntityBase
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

/**
 * [HTMachineBlockEntityBase]を返すファクトリー
 */
fun interface HTMachineEntityFactory {
    companion object {
        /**
         * 固定の[HTMachineKey]を持った[HTMachineBlockEntityBase]向けの[HTMachineEntityFactory]
         */
        @JvmStatic
        fun of(factory: (BlockPos, BlockState) -> HTMachineBlockEntityBase?): HTMachineEntityFactory =
            HTMachineEntityFactory { pos: BlockPos, state: BlockState, _: HTMachineKey ->
                factory(pos, state)
            }
    }

    fun create(pos: BlockPos, state: BlockState, key: HTMachineKey): HTMachineBlockEntityBase?
}
