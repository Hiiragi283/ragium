package hiiragi283.ragium.common.block.generator

import hiiragi283.ragium.api.block.entity.HTFluidGeneratorBlockEntity
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.storage.fluid.HTFluidVariant
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class HTThermalGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTFluidGeneratorBlockEntity(
        RagiumBlockEntityTypes.THERMAL_GENERATOR,
        pos,
        state,
        HTMachineType.THERMAL_GENERATOR,
    ) {
    override fun isFluidValid(variant: HTFluidVariant): Boolean = variant.isIn(RagiumFluidTags.THERMAL_FUEL)

    override fun getFuelAmount(variant: HTFluidVariant): Int = when {
        variant.isIn(RagiumFluidTags.THERMAL_FUEL) -> 100
        else -> 0
    }
}
