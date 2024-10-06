package hiiragi283.ragium.api.machine

import hiiragi283.ragium.common.block.entity.HTMachineBlockEntityBase
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

fun interface HTMachineFactory {
    fun createMachine(
        pos: BlockPos,
        state: BlockState,
        type: HTMachineType,
        tier: HTMachineTier,
    ): HTMachineBlockEntityBase
}
