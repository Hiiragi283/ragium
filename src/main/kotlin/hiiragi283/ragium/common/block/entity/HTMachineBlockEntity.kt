package hiiragi283.ragium.common.block.entity

import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.upgrade.HTUpgradeHelper
import hiiragi283.ragium.common.block.HTMachineBlock
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumFluidConfigType
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState
import java.util.function.IntSupplier

abstract class HTMachineBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTUpgradableBlockEntity(type, pos, state) {
    abstract fun getConfig(): HTMachineConfig

    protected fun getTankCapacity(type: RagiumFluidConfigType): IntSupplier {
        val baseCapacity: IntSupplier = getConfig().tankMap[type] ?: error("Undefined tank capacity for ${type.serializedName}")
        return IntSupplier { HTUpgradeHelper.getFluidCapacity(this, baseCapacity.asInt) }
    }

    fun isActive(): Boolean = isActive(this.blockState)

    fun isActive(state: BlockState): Boolean = state.getOptionalValue(HTMachineBlock.IS_ACTIVE).orElse(false)

    open fun setupMenu(widgetHolder: HTWidgetHolder) {}

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
