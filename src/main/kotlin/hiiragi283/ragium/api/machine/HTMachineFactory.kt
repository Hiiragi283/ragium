package hiiragi283.ragium.api.machine

import hiiragi283.ragium.common.block.entity.HTMachineBlockEntityBase
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

fun interface HTMachineFactory {
    companion object {
        @JvmStatic
        fun of(factory: (BlockPos, BlockState, HTMachineTier) -> HTMachineBlockEntityBase): HTMachineFactory =
            HTMachineFactory { pos: BlockPos, state: BlockState, _: HTMachineType, tier: HTMachineTier ->
                factory(pos, state, tier)
            }
    }

    fun createMachine(
        pos: BlockPos,
        state: BlockState,
        type: HTMachineType,
        tier: HTMachineTier,
    ): HTMachineBlockEntityBase
}
