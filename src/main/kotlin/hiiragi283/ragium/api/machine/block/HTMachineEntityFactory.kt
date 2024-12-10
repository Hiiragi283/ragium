package hiiragi283.ragium.api.machine.block

import hiiragi283.ragium.api.machine.HTMachineKey
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

fun interface HTMachineEntityFactory {
    companion object {
        @JvmStatic
        fun of(factory: (BlockPos, BlockState) -> HTMachineBlockEntityBase?): HTMachineEntityFactory =
            HTMachineEntityFactory { pos: BlockPos, state: BlockState, _: HTMachineKey ->
                factory(pos, state)
            }
    }

    fun create(pos: BlockPos, state: BlockState, key: HTMachineKey): HTMachineBlockEntityBase?
}
