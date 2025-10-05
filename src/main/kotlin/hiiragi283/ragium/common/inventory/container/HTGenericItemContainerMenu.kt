package hiiragi283.ragium.common.inventory.container

import hiiragi283.ragium.api.inventory.HTMenuCallback
import hiiragi283.ragium.api.inventory.container.HTItemContainerContext
import hiiragi283.ragium.api.inventory.container.HTItemContainerMenu
import hiiragi283.ragium.api.registry.impl.HTDeferredMenuType
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.item.base.HTContainerItem
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player

@Suppress("DEPRECATION")
abstract class HTGenericItemContainerMenu(
    menuType: HTDeferredMenuType<*>,
    containerId: Int,
    inventory: Inventory,
    context: HTItemContainerContext,
    isClientSide: Boolean,
    final override val rows: Int,
) : HTItemContainerMenu(
        menuType,
        containerId,
        inventory,
        context,
    ),
    HTGenericContainerRows {
    protected val handler: HTItemHandler = when (isClientSide) {
        true -> null
        false -> HTContainerItem.getHandler(stack)
    } ?: createHandler(rows)

    protected abstract fun createHandler(rows: Int): HTItemHandler

    init {
        check(handler.slots >= rows) { "Item handler size ${handler.slots} is smaller than expected $rows" }
        val i: Int = (rows - 3) * 18 + 1

        addSlots(handler)

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
