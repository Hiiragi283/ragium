package hiiragi283.ragium.api.machine.block

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

fun interface HTMachineEntityFactory {
    companion object {
        @JvmStatic
        fun of(factory: (BlockPos, BlockState, HTMachineTier) -> HTMachineBlockEntityBase?): HTMachineEntityFactory =
            HTMachineEntityFactory { pos: BlockPos, state: BlockState, _: HTMachineKey, tier: HTMachineTier ->
                factory(pos, state, tier)
            }
    }

    fun create(
        pos: BlockPos,
        state: BlockState,
        key: HTMachineKey,
        tier: HTMachineTier,
    ): HTMachineBlockEntityBase?
}
