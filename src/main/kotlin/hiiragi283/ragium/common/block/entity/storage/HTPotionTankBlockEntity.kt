package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.transfer.fluid.HTBasicFluidTank
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.util.HTStorageHelper
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.storage.base.HTBaseTankBlockEntity
import hiiragi283.ragium.common.transfer.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.core.component.DataComponentMap
import net.minecraft.world.level.block.state.BlockState

class HTPotionTankBlockEntity(pos: BlockPos, state: BlockState) :
    HTBaseTankBlockEntity(RagiumBlockEntityTypes.POTION_TANK.get(), pos, state) {
    override lateinit var tank: HTBasicFluidTank
        private set

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        tank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidTank.create(
                RagiumConfig.SERVER.tankCapacity,
                listener,
                filter = HTPotionHelper::hasAnyEffect
            )
        )
    }

    //    Sync    //

    override fun applyImplicitComponents(components: DataComponentGetter) {
        super.applyImplicitComponents(components)
        tank.setStack(HTStorageHelper.getFluid(components))
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        HTStorageHelper.updateFluid(components, tank.getStackCopy())
    }
}
