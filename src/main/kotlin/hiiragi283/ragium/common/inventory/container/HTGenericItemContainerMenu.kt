package hiiragi283.ragium.common.inventory.container

import hiiragi283.ragium.api.inventory.HTMenuCallback
import hiiragi283.ragium.api.inventory.container.HTItemContainerMenu
import hiiragi283.ragium.api.registry.HTDeferredMenuType
import hiiragi283.ragium.api.storage.HTMultiCapability
import hiiragi283.ragium.api.storage.item.HTItemHandler
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

abstract class HTGenericItemContainerMenu(
    menuType: HTDeferredMenuType<*>,
    containerId: Int,
    inventory: Inventory,
    hand: InteractionHand,
    stack: ItemStack,
    isClientSide: Boolean,
    final override val rows: Int,
) : HTItemContainerMenu(
        menuType,
        containerId,
        inventory,
        hand,
        stack,
    ),
    HTGenericContainerRows {
    protected val handler: HTItemHandler = when (isClientSide) {
        true -> null
        false -> HTMultiCapability.ITEM.getCapability(stack) as? HTItemHandler
    } ?: createHandler(rows)

    protected abstract fun createHandler(rows: Int): HTItemHandler

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
