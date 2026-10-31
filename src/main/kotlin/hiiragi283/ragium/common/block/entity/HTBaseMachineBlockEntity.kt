package hiiragi283.ragium.common.block.entity

import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.ragium.api.machine.HTMachineUpgrade
import hiiragi283.ragium.api.machine.HTMachineUpgradeHelper
import hiiragi283.ragium.common.block.RagiumBlockStateProperties
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import java.util.function.IntSupplier

abstract class HTBaseMachineBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTConfigurableBlockEntity(type, pos, state),
    HTBlockEntityWithMenu {
    protected fun getTankCapacity(): IntSupplier = RagiumConfig.SERVER.machineTankCapacity

    fun isActive(): Boolean = isActive(this.blockState)

    fun isActive(state: BlockState): Boolean = state.getOptionalValue(RagiumBlockStateProperties.IS_ACTIVE).orElseGet { false }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {}

    /**
     * @since 26.1.9
     */
    val combinedUpgrade: HTMachineUpgrade get() = HTMachineUpgradeHelper.getCombinedUpgrade(this.components())

    /**
     * @since 26.1.9
     */
    val customKeys: Set<String> = combinedUpgrade.customKeys

    //    Ticking    //

    final override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        val lastActive: Boolean = isActive(state)
        val result: Boolean = onUpdateMachine(level, pos, state)
        // 以前の結果と異なる場合は更新する
        if (result != lastActive) {
            val newState: BlockState = when {
                state.hasProperty(RagiumBlockStateProperties.IS_ACTIVE) -> state.setValue(RagiumBlockStateProperties.IS_ACTIVE, result)
                else -> state
            }
            level.setBlockAndUpdate(pos, newState)
            setChanged()
        }
        return result
    }

    protected abstract fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean
}
