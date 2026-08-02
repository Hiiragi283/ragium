package hiiragi283.ragium.common.block.entity

import hiiragi283.core.api.gui.sync.HTSyncType
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.support.gui.sync.HTFluidSyncSlot
import hiiragi283.core.support.gui.sync.HTItemSyncSlot
import hiiragi283.core.support.storage.fluid.HTFluidStackResourceSlot
import hiiragi283.core.support.storage.item.HTItemStackResourceSlot
import hiiragi283.ragium.api.block.entity.HTBlockEntityWithMenu
import hiiragi283.ragium.common.block.HTMachineBlock
import hiiragi283.ragium.config.RagiumConfig
import java.util.function.IntSupplier
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

abstract class HTMachineBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTConfigurableBlockEntity(type, pos, state),
    HTBlockEntityWithMenu {
    protected fun getTankCapacity(): IntSupplier = RagiumConfig.COMMON.machine.tankCapacity

    fun isActive(): Boolean = isActive(this.blockState)

    fun isActive(state: BlockState): Boolean = state.getOptionalValue(HTMachineBlock.IS_ACTIVE).orElseGet { false }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {}

    protected fun HTWidgetHolder.track(slot: HTFluidStackResourceSlot, syncType: HTSyncType = HTSyncType.S2C) {
        this.track(HTFluidSyncSlot(slot), syncType)
    }

    protected fun HTWidgetHolder.track(slot: HTItemStackResourceSlot, syncType: HTSyncType = HTSyncType.S2C) {
        this.track(HTItemSyncSlot(slot), syncType)
    }

    //    Ticking    //

    final override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        val lastActive: Boolean = isActive(state)
        val result: Boolean = onUpdateMachine(level, pos, state)
        // 以前の結果と異なる場合は更新する
        if (result != lastActive) {
            val newState: BlockState = when {
                state.hasProperty(HTMachineBlock.IS_ACTIVE) -> state.setValue(HTMachineBlock.IS_ACTIVE, result)
                else -> state
            }
            level.setBlockAndUpdate(pos, newState)
            setChanged()
        }
        return result
    }

    protected abstract fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean
}
