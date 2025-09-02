package hiiragi283.ragium.common.inventory.container

import hiiragi283.ragium.api.inventory.container.HTContainerWithContextMenu
import hiiragi283.ragium.api.registry.impl.HTDeferredMenuType
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.items.IItemHandler

/**
 * @see [net.minecraft.world.inventory.ChestMenu]
 */
class HTGenericContainerMenu(
    menuType: HTDeferredMenuType<*>,
    containerId: Int,
    inventory: Inventory,
    context: HTItemHandler,
    override val rows: Int,
) : HTContainerWithContextMenu<IItemHandler>(
        menuType,
        containerId,
        inventory,
        context,
    ),
    HTGenericContainerRows {
    companion object {
        @JvmStatic
        fun threeRow(containerId: Int, inventory: Inventory, handler: HTItemHandler): HTGenericContainerMenu =
            HTGenericContainerMenu(RagiumMenuTypes.UNIVERSAL_BUNDLE, containerId, inventory, handler, 3)
    }

    init {
        check(context.slots >= rows) { "Item context size ${context.slots} is smaller than expected $rows" }
        val i: Int = (rows - 3) * 18 + 1

        for (y: Int in (0 until rows)) {
            for (x: Int in (0 until 9)) {
                context.getItemSlot(x + y * 9, context.getItemSideFor())?.createContainerSlot()?.let(::addSlot)
            }
        }

        addPlayerInv(inventory, i)
    }

    override fun stillValid(player: Player): Boolean = true
}
