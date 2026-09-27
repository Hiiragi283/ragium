package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.lib.transfer.fluid.HTBasicFluidTank
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.data.RagiumDataComponents
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.storage.base.HTBaseTankBlockEntity
import hiiragi283.ragium.common.transfer.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.core.component.DataComponentMap
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.SimpleFluidContent

class HTTankBlockEntity(pos: BlockPos, state: BlockState) :
    HTBaseTankBlockEntity(RagiumBlockEntityTypes.TANK.get(), pos, state) {
    override lateinit var tank: HTBasicFluidTank
        private set

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        tank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidTank.create(RagiumConfig.SERVER.tankCapacity, listener)
        )
    }

    //    Sync    //

    override fun applyImplicitComponents(components: DataComponentGetter) {
        super.applyImplicitComponents(components)
        components.get(RagiumDataComponents.FLUID)?.copy()?.let(tank::setStack)
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        val content: SimpleFluidContent = tank.getStackCopy().let(SimpleFluidContent::copyOf)
        if (!content.isEmpty) {
            components.set(RagiumDataComponents.FLUID, content)
        }
    }
}
