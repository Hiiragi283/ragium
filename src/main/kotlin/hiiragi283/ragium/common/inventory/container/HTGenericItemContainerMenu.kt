package hiiragi283.ragium.common.inventory.container

import hiiragi283.ragium.api.inventory.HTMenuCallback
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.inventory.container.HTBaseGenericContainerMenu
import hiiragi283.ragium.api.inventory.container.HTItemContainerMenu
import hiiragi283.ragium.api.registry.HTDeferredMenuType
import hiiragi283.ragium.api.storage.HTMultiCapability
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.common.storage.item.HTItemStackSlot
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

class HTGenericItemContainerMenu(
    menuType: HTDeferredMenuType<*>,
    containerId: Int,
    inventory: Inventory,
    hand: InteractionHand,
    stack: ItemStack,
    isClientSide: Boolean,
    override val rows: Int,
) : HTItemContainerMenu(
        menuType,
        containerId,
        inventory,
        hand,
        stack,
    ),
    HTBaseGenericContainerMenu {
    companion object {
        @JvmStatic
        fun createSlots(rows: Int): HTItemHandler = HTItemStackHandler(
            (0 until rows * 9).map { index: Int ->
                HTItemStackSlot.at(
                    null,
                    HTSlotHelper.getSlotPosX(index % 9),
                    HTSlotHelper.getSlotPosY(index / 9),
                )
            },
            null,
        )

        @JvmStatic
        fun oneRow(
            containerId: Int,
            inventory: Inventory,
            hand: InteractionHand,
            stack: ItemStack,
            isRemote: Boolean,
        ): HTGenericItemContainerMenu =
            HTGenericItemContainerMenu(RagiumMenuTypes.GENERIC_9x1, containerId, inventory, hand, stack, isRemote, 1)
    }

    val handler: HTItemHandler = when (isClientSide) {
        true -> null
        false -> HTMultiCapability.ITEM.getCapability(stack) as? HTItemHandler
    } ?: createSlots(rows)

    init {
        check(handler.slots >= rows) { "Item handler size ${handler.slots} is smaller than expected $rows" }
        val i: Int = (rows - 3) * 18 + 1

        for (y: Int in (0 until rows)) {
            for (x: Int in (0 until 9)) {
                handler.getItemSlot(x + y * 9, handler.getItemSideFor())?.createContainerSlot()?.let(::addSlot)
            }
        }

        addPlayerInv(inventory, i)
    }

    override fun onOpen(player: Player) {
        super.onOpen(player)
        (handler as? HTMenuCallback)?.openMenu(player)
    }

    override fun onClose(player: Player) {
        super.onClose(player)
        (handler as? HTMenuCallback)?.closeMenu(player)
    }
}
