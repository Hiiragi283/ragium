package hiiragi283.ragium.common.block.generator

import hiiragi283.ragium.api.block.entity.HTFluidGeneratorBlockEntity
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.storage.fluid.HTFluidVariant
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class HTCombustionGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTFluidGeneratorBlockEntity(
        RagiumBlockEntityTypes.COMBUSTION_GENERATOR,
        pos,
        state,
        HTMachineType.COMBUSTION_GENERATOR,
    ) {
    override fun isFluidValid(variant: HTFluidVariant): Boolean =
        variant.isIn(RagiumFluidTags.NON_NITRO_FUEL) || variant.isIn(RagiumFluidTags.NITRO_FUEL)

    override fun getFuelAmount(variant: HTFluidVariant): Int = when {
        variant.isIn(RagiumFluidTags.NON_NITRO_FUEL) -> 100
        variant.isIn(RagiumFluidTags.NITRO_FUEL) -> 10
        else -> 0
    }
}
