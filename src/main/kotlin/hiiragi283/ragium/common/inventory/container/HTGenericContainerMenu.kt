package hiiragi283.ragium.common.inventory.container

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.inventory.container.HTContainerWithContextMenu
import hiiragi283.ragium.api.registry.HTDeferredMenuType
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.world.entity.player.Inventory
import net.neoforged.neoforge.items.IItemHandler

/**
 * @see [net.minecraft.world.inventory.ChestMenu]
 */
class HTGenericContainerMenu(
    menuType: HTDeferredMenuType<*, IItemHandler>,
    containerId: Int,
    inventory: Inventory,
    context: IItemHandler,
    val rows: Int,
) : HTContainerWithContextMenu<IItemHandler>(
        menuType,
        containerId,
        inventory,
        context,
    ) {
    companion object {
        @JvmStatic
        fun oneRow(containerId: Int, inventory: Inventory, handler: IItemHandler): HTGenericContainerMenu =
            HTGenericContainerMenu(RagiumMenuTypes.GENERIC_9x1, containerId, inventory, handler, 1)

        @JvmStatic
        fun threeRow(containerId: Int, inventory: Inventory, handler: IItemHandler): HTGenericContainerMenu =
            HTGenericContainerMenu(RagiumMenuTypes.GENERIC_9x3, containerId, inventory, handler, 3)
    }

    init {
        check(context.slots >= rows) { "Item handler size ${context.slots} is smaller than expected $rows" }
        val i: Int = (rows - 3) * 18 + 1

        for (y: Int in (0 until rows)) {
            for (x: Int in (0 until 9)) {
                addInputSlot(context, x + y * 9, HTSlotHelper.getSlotPosX(x), HTSlotHelper.getSlotPosY(y))
            }
        }

        addPlayerInv(inventory, i)
    }
}
