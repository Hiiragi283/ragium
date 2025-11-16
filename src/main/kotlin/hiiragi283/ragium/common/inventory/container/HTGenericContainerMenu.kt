package hiiragi283.ragium.common.inventory.container

import hiiragi283.ragium.api.registry.impl.HTDeferredMenuType
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player

/**
 * @see net.minecraft.world.inventory.ChestMenu
 */
class HTGenericContainerMenu(
    menuType: HTDeferredMenuType.WithContext<*, HTItemHandler>,
    containerId: Int,
    inventory: Inventory,
    context: HTItemHandler,
    override val rows: Int,
) : HTContainerWithContextMenu<HTItemHandler>(
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
        val slots: Int = context.getSlots(context.getItemSideFor())
        check(slots >= rows) { "Item context size $slots is smaller than expected $rows" }
        val i: Int = (rows - 3) * 18 + 1

        addSlots(context)

        addPlayerInv(inventory, i)
    }

    override fun stillValid(player: Player): Boolean = true
}
