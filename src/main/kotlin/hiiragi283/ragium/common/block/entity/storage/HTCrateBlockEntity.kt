package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.ragium.api.block.attribute.getAttributeTier
import hiiragi283.ragium.api.item.component.HTItemContents
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.HTConfigurableBlockEntity
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.common.storage.item.slot.HTVariableItemSlot
import hiiragi283.ragium.common.tier.HTCrateTier
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.util.HTEnchantmentHelper
import hiiragi283.ragium.util.HTStackSlotHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentMap
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Consumer

class HTCrateBlockEntity(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTConfigurableBlockEntity(blockHolder, pos, state) {
    private lateinit var tier: HTCrateTier

    override fun initializeVariables() {
        tier = blockHolder.getAttributeTier()
    }

    lateinit var slot: HTBasicItemSlot
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        slot = builder.addSlot(
            HTSlotInfo.BOTH,
            HTVariableItemSlot.create(listener, { stack: ImmutableItemStack? ->
                val capacity: Int = HTItemSlot.getMaxStackSize(stack) * tier.getMultiplier()
                HTEnchantmentHelper.processStorageCapacity(this.getLevel()?.random, enchantment, capacity)
            }, 0, 0),
        )
    }

    override fun collectItemDrops(consumer: Consumer<ImmutableItemStack>) {}

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
