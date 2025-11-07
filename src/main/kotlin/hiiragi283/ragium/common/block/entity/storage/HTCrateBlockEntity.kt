package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.ragium.api.block.attribute.getAttributeTier
import hiiragi283.ragium.api.item.component.HTItemContents
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.HTConfigurableBlockEntity
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.storage.item.slot.HTVariableItemStackSlot
import hiiragi283.ragium.common.tier.HTCrateTier
import hiiragi283.ragium.common.util.HTItemHelper
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentMap
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

class HTCrateBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTConfigurableBlockEntity(blockHolder, pos, state) {
    private lateinit var tier: HTCrateTier

    override fun initializeVariables() {
        tier = blockHolder.getAttributeTier()
    }

    lateinit var slot: HTItemStackSlot
        private set

    override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder {
        val builder: HTBasicItemSlotHolder.Builder = HTBasicItemSlotHolder.builder(this)
        slot = builder.addSlot(
            HTSlotInfo.BOTH,
            HTVariableItemStackSlot.create(listener, { stack: ImmutableItemStack? ->
                val capacity: Int = HTItemSlot.getMaxStackSize(stack) * tier.getMultiplier()
                HTItemHelper.processStorageCapacity(this.getLevel()?.random, enchantment, capacity)
            }, 0, 0),
        )
        return builder.build()
    }

    override fun doDropItems(): Boolean = false

    override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int = HTStackSlotHelper.calculateRedstoneLevel(slot)

    //    Save & Read    //

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        super.applyImplicitComponents(componentInput)
        componentInput.get(RagiumDataComponents.ITEM_CONTENT)?.getOrNull(0)?.let(slot::setStackUnchecked)
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        components.set(RagiumDataComponents.ITEM_CONTENT, HTItemContents.of(slot.getStack()))
    }

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = false
}
