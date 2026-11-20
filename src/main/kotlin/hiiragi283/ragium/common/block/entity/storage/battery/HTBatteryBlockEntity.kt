package hiiragi283.ragium.common.block.entity.storage.battery

import hiiragi283.lib.transfer.energy.HTEnergyStorageUtil
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.transfer.energy.HTVariableEnergyStorage
import hiiragi283.ragium.common.transfer.holder.HTBasicEnergyStorageHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.core.component.DataComponentMap
import net.minecraft.world.level.block.state.BlockState

class HTBatteryBlockEntity(pos: BlockPos, state: BlockState) : HTBaseBatteryBlockEntity(RagiumBlockEntityTypes.BATTERY.get(), pos, state) {
    override lateinit var storage: HTVariableEnergyStorage

    override fun createEnergyStorage(builder: HTBasicEnergyStorageHolder.Builder, listener: Runnable) {
        storage = builder.addStorage(HTSlotInfo.BOTH, HTVariableEnergyStorage.create(::getBatteryCapacity, listener))
    }

    //    Sync    //

    override fun applyImplicitComponents(components: DataComponentGetter) {
        super.applyImplicitComponents(components)
        storage.setAmount(HTEnergyStorageUtil.getEnergy(components))
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        HTEnergyStorageUtil.updateEnergy(components, storage.amount)
    }
}
