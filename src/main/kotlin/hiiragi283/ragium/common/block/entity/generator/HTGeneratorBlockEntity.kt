package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.api.storage.holder.HTEnergyStorageHolder
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.storage.holder.HTSimpleEnergyStorageHolder
import hiiragi283.ragium.common.variant.HTGeneratorVariant
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.IEnergyStorage

abstract class HTGeneratorBlockEntity(protected val variant: HTGeneratorVariant, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(variant.blockEntityHolder, pos, state) {
    final override val energyUsage: Int = variant.energyRate

    final override fun createStorageHolder(energyStorage: IEnergyStorage): HTEnergyStorageHolder =
        HTSimpleEnergyStorageHolder.output(this, energyStorage)
}
