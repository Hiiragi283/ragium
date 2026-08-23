package hiiragi283.ragium.common.gui.menu

import hiiragi283.lib.gui.sync.HTSyncType
import hiiragi283.lib.gui.sync.HTSyncableSlot
import hiiragi283.lib.gui.widget.HTWidget
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.gui.factory.HTWidgetHolderContext
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.MenuType

/**
 * [HTWidgetHolder]に基づいた[HTContainerMenu]の拡張クラスです。
 * @see net.minecraft.world.inventory.ChestMenu
 */
class HTWidgetContainerMenu(
    menuType: MenuType<*>,
    containerId: Int,
    inventory: Inventory,
    context: HTWidgetHolderContext,
) : HTContainerMenu<HTWidgetHolderContext>(menuType, containerId, inventory, context) {
    private val widgets: MutableList<HTWidget> = mutableListOf()
    val widgetHolder: HTWidgetHolder = WidgetHolderImpl()

    init {
        context.setup(inventory.player, widgetHolder)
        // Player Inventory
        addPlayerInv(inventory, (widgetHolder.rows - 4) * 18)
    }

    override fun stillValid(player: Player): Boolean = context.stillValid(player)

    private inner class WidgetHolderImpl : HTWidgetHolder {
        override fun <WIDGET : HTWidget> addWidget(widget: WIDGET): WIDGET {
            widgets += widget
            RagiumAPI.LOGGER.debug("Added widget: {}", widget)
            if (widget is HTItemWidget.Container) {
                addSlot(widget.slot)
            }
            return widget
        }

        override fun track(slot: HTSyncableSlot, type: HTSyncType) {
            this@HTWidgetContainerMenu.addTrackedSlot(slot, type)
            RagiumAPI.LOGGER.debug("Added syncable slot: {} for {}", slot, type)
        }

        override var rows: Int = 3

        override fun iterator(): Iterator<HTWidget> = widgets.iterator()
    }
}
