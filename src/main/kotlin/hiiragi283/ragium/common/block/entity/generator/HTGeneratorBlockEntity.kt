package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.api.storage.energy.HTEnergyFilter
import hiiragi283.ragium.api.storage.energy.HTFilteredEnergyStorage
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.util.variant.HTGeneratorVariant
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.IEnergyStorage

abstract class HTGeneratorBlockEntity(protected val variant: HTGeneratorVariant, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(variant.blockEntityHolder, pos, state) {
    final override val energyUsage: Int = variant.energyRate

    final override fun wrapNetworkToExternal(network: IEnergyStorage): IEnergyStorage =
        HTFilteredEnergyStorage(network, HTEnergyFilter.EXTRACT_ONLY)
}
