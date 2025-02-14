package hiiragi283.ragium.common.block.generator

import hiiragi283.ragium.api.block.entity.HTFluidGeneratorBlockEntity
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack

class HTThermalGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTFluidGeneratorBlockEntity(
        RagiumBlockEntityTypes.THERMAL_GENERATOR,
        pos,
        state,
        HTMachineType.THERMAL_GENERATOR,
    ) {
    override fun isFluidValid(stack: FluidStack): Boolean = stack.`is`(RagiumFluidTags.THERMAL_FUEL)

    override fun getFuelAmount(stack: FluidStack): Int = when {
        stack.`is`(RagiumFluidTags.THERMAL_FUEL) -> 100
        else -> 0
    }

    override val hasMenu: Boolean = false
}
