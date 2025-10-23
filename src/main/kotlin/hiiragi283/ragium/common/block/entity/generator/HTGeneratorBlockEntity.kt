package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.ragium.api.storage.holder.HTEnergyStorageHolder
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.storage.energy.HTEnergyBatteryWrapper
import hiiragi283.ragium.common.storage.holder.HTSimpleEnergyStorageHolder
import hiiragi283.ragium.common.variant.HTGeneratorVariant
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

/**
 * 電力を生産する設備に使用される[HTMachineBlockEntity]の拡張クラス
 */
abstract class HTGeneratorBlockEntity(val variant: HTGeneratorVariant, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(variant.blockEntityHolder, pos, state) {
    //    Ticking    //

    override val energyUsage: Int = variant.energyRate

    //    Energy Storage    //

    final override fun initializeEnergyStorage(listener: HTContentListener): HTEnergyStorageHolder =
        HTSimpleEnergyStorageHolder.output(this, HTEnergyBatteryWrapper { getter(level) })
}
