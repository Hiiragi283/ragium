package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.block.entity.HTFluidGeneratorBlockEntity
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack

class HTCombustionGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTFluidGeneratorBlockEntity(
        RagiumBlockEntityTypes.COMBUSTION_GENERATOR,
        pos,
        state,
        RagiumMachineKeys.COMBUSTION_GENERATOR,
    ) {
    override fun isFluidValid(stack: FluidStack): Boolean =
        stack.`is`(RagiumFluidTags.NON_NITRO_FUEL) || stack.`is`(RagiumFluidTags.NITRO_FUEL)

    override fun getFuelAmount(stack: FluidStack): Int = when {
        stack.`is`(RagiumFluidTags.NON_NITRO_FUEL) -> 100
        stack.`is`(RagiumFluidTags.NITRO_FUEL) -> 10
        else -> 0
    }
}
