package hiiragi283.ragium.api.machine

import hiiragi283.ragium.api.block.HTMachineBlockEntityBase
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

/**
 * A custom [net.minecraft.block.entity.BlockEntityType.BlockEntityFactory] for [HTMachineBlockEntityBase]
 */
fun interface HTMachineEntityFactory {
    companion object {
        /**
         * Create a new [HTMachineEntityFactory] instance for the class which has constant [HTMachineBlockEntityBase.key]
         */
        @JvmStatic
        fun of(factory: (BlockPos, BlockState) -> HTMachineBlockEntityBase?): HTMachineEntityFactory =
            HTMachineEntityFactory { pos: BlockPos, state: BlockState, _: HTMachineKey ->
                factory(pos, state)
            }
    }

    /**
     * Create new [HTMachineBlockEntityBase] instance or null
     */
    fun create(pos: BlockPos, state: BlockState, key: HTMachineKey): HTMachineBlockEntityBase?
}
