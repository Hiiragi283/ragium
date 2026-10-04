package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.ragium.api.util.HTStorageHelper
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.storage.base.HTBaseTankBlockEntity
import hiiragi283.ragium.common.transfer.fluid.HTCreativeFluidTank
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.core.component.DataComponentMap
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.transfer.fluid.FluidResource

class HTCreativeTankBlockEntity(pos: BlockPos, state: BlockState) :
    HTBaseTankBlockEntity(RagiumBlockEntityTypes.CREATIVE_TANK.get(), pos, state) {
    override lateinit var tank: HTCreativeFluidTank
        private set

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        tank = builder.addSlot(HTSlotInfo.INPUT, HTCreativeFluidTank())
    }

    //    Sync    //

    override fun applyImplicitComponents(components: DataComponentGetter) {
        super.applyImplicitComponents(components)
        tank.resource = HTStorageHelper.getFluid(components).let(FluidResource::of)
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        HTStorageHelper.updateFluid(components, tank.resource.toStack(1))
    }
}
