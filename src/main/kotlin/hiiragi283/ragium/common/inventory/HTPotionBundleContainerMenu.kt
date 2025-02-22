package hiiragi283.ragium.common.inventory

import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.extension.forEachSlot
import hiiragi283.ragium.api.inventory.HTContainerMenu
import hiiragi283.ragium.api.inventory.HTSlotPos
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ClickType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.ItemContainerContents
import net.neoforged.neoforge.items.ItemStackHandler

class HTPotionBundleContainerMenu(containerId: Int, inventory: Inventory) :
    HTContainerMenu(
        RagiumMenuTypes.POTION_BUNDLE,
        containerId,
        inventory,
        BlockPos.ZERO,
    ) {
    private val potionHandler: ItemStackHandler = object : ItemStackHandler(9) {
        override fun isItemValid(slot: Int, stack: ItemStack): Boolean = stack.`is`(Items.POTION)
    }

    init {
        // potion slots
        addSlot(potionHandler, 0, 0, 0)
        addSlot(potionHandler, 1, 1, 0)
        addSlot(potionHandler, 2, 2, 0)
        addSlot(potionHandler, 3, 3, 0)
        addSlot(potionHandler, 4, 4, 0)
        addSlot(potionHandler, 5, 5, 0)
        addSlot(potionHandler, 6, 6, 0)
        addSlot(potionHandler, 7, 7, 0)
        addSlot(potionHandler, 8, 8, 0)
        // player inventory
        addPlayerInv(-HTSlotPos.getSlotPosY(1), true)
        // load from stack
        val stack: ItemStack = inventory.getSelected()
        if (!stack.isEmpty && stack.has(RagiumComponentTypes.ITEM_CONTENT)) {
            setStackInHandler(potionHandler, stack)
        }
    }

    companion object {
        @JvmStatic
        private fun setStackInHandler(handler: ItemStackHandler, stack: ItemStack) {
            val content: ItemContainerContents =
                stack.getOrDefault(RagiumComponentTypes.ITEM_CONTENT, ItemContainerContents.EMPTY)
            handler.forEachSlot { slot: Int ->
                val stackIn: ItemStack = when (slot >= content.slots) {
                    true -> ItemStack.EMPTY
                    false -> content.getStackInSlot(slot)
                }
                handler.setStackInSlot(slot, stackIn)
            }
        }
    }

    override val inputSlots: IntRange = (0..8)
    override val outputSlots: IntRange = IntRange.EMPTY

    override fun clicked(
        slotId: Int,
        button: Int,
        clickType: ClickType,
        player: Player,
    ) {
        if (clickType == ClickType.SWAP && button == this.inventory.selected) {
            return
        }
        super.clicked(slotId, button, clickType, player)
    }

    override fun removed(player: Player) {
        val stack: ItemStack = this.inventory.getSelected()
        if (!stack.isEmpty && stack.`is`(RagiumItems.POTION_BUNDLE)) {
            stack.set(
                RagiumComponentTypes.ITEM_CONTENT,
                ItemContainerContents.fromItems(
                    buildList { potionHandler.forEach(this::add) },
                ),
            )
        }
        super.removed(player)
    }
}
