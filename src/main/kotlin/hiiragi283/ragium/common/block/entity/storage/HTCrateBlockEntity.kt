package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.storage.amount.HTAmountView
import hiiragi283.core.api.storage.holder.HTItemSlotHolder
import hiiragi283.core.common.gui.widget.HTItemWidget
import hiiragi283.core.support.storage.item.HTItemStackResourceSlot
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.support.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.support.storage.holder.HTSlotInfo
import hiiragi283.ragium.support.storage.item.HTVariableItemSlot
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

open class HTCrateBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : HTStorageBlockEntity(type, pos, state) {
    constructor(pos: BlockPos, state: BlockState) : this(RagiumBlockEntityTypes.CRATE.get(), pos, state)

    lateinit var slot: HTItemStackResourceSlot
        private set

    final override fun createItemHandler(listener: HTContentListener): HTItemSlotHolder? {
        val builder = HTBasicItemSlotHolder.Builder(this)
        slot = builder.addSlot(HTSlotInfo.BOTH, createSlot(listener))
        return builder.build()
    }

    protected open fun createSlot(listener: HTContentListener): HTItemStackResourceSlot = HTVariableItemSlot.create(listener, { capacityComponent.getCapacity(RagiumConfig.SERVER.crateCapacity) })

    final override fun getAmountView(): HTAmountView = slot

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        widgetHolder.rows = 1

        widgetHolder += createSlotWidget()
    }

    protected open fun createSlotWidget(): HTItemWidget = HTItemWidget.Container(
        slot,
        HTSlotHelper.getSlotPosX(4),
        HTSlotHelper.getSlotPosY(0),
        HTBackgroundType.NONE,
    )
}
