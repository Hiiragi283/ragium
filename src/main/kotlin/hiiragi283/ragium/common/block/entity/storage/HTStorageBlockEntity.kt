package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.storage.amount.HTAmountView
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.util.HTStackSlotHelper
import hiiragi283.ragium.common.block.entity.HTUpgradableBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import org.apache.commons.lang3.math.Fraction

abstract class HTStorageBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTUpgradableBlockEntity(type, pos, state) {
    protected abstract fun getAmountView(): HTAmountView

    final override fun markDirtyComparator() {
        level?.updateNeighbourForOutputSignal(blockPos, blockState.block)
    }

    final override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int =
        HTStackSlotHelper.calculateRedstoneLevel(getAmountView())

    open fun setupMenu(widgetHolder: HTWidgetHolder) {}

    //    Ticking    //

    private var oldScale: Fraction = Fraction.ZERO

    final override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // 保持する量の変化があれば更新させる
        val scale: Fraction = getAmountView().getLevelAsFraction()
        if (scale != this.oldScale) {
            this.oldScale = scale
            return true
        }
        return false
    }
}
