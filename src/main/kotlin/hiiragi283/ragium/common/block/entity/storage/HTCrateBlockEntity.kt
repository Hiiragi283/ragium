package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.ragium.api.block.attribute.getAttributeTier
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.HTConfigurableBlockEntity
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.common.storage.item.slot.HTVariableItemSlot
import hiiragi283.ragium.common.tier.HTCrateTier
import hiiragi283.ragium.util.HTEnchantmentHelper
import hiiragi283.ragium.util.HTStackSlotHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
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

    override fun getComparatorOutput(state: BlockState, level: Level, pos: BlockPos): Int = HTStackSlotHelper.calculateRedstoneLevel(slot)

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = false
}
