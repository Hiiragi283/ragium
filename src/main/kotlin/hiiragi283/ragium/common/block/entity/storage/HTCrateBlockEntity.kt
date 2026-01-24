package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.storage.amount.HTAmountView
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.upgrade.HTUpgradeHelper
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.common.storge.item.HTVariableItemSlot
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

open class HTCrateBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTStorageBlockEntity(type, pos, state) {
    constructor(pos: BlockPos, state: BlockState) : this(RagiumBlockEntityTypes.CRATE, pos, state)

    lateinit var slot: HTItemSlot.Basic
        private set

    final override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        slot = builder.addSlot(HTSlotInfo.BOTH, createSlot(listener))
    }

    protected open fun createSlot(listener: HTContentListener): HTItemSlot.Basic =
        HTVariableItemSlot.create(listener, { HTUpgradeHelper.getItemCapacity(this, RagiumConfig.COMMON.crateCapacity.asInt) })

    final override fun getAmountView(): HTAmountView = slot
}
