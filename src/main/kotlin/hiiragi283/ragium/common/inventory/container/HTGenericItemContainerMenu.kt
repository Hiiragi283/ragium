package hiiragi283.ragium.common.inventory.container

import hiiragi283.ragium.api.inventory.HTMenuCallback
import hiiragi283.ragium.api.inventory.container.HTItemContainerContext
import hiiragi283.ragium.api.inventory.container.HTItemContainerMenu
import hiiragi283.ragium.api.registry.impl.HTDeferredMenuType
import hiiragi283.ragium.api.storage.capability.RagiumCapabilities
import hiiragi283.ragium.api.storage.item.HTItemHandler
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.neoforged.api.distmarker.Dist

@Suppress("DEPRECATION")
abstract class HTGenericItemContainerMenu(
    menuType: HTDeferredMenuType.OnHand<*>,
    containerId: Int,
    inventory: Inventory,
    context: HTItemContainerContext,
    isClientSide: Dist,
    final override val rows: Int,
) : HTItemContainerMenu(
        menuType,
        containerId,
        inventory,
        context,
    ),
    HTGenericContainerRows {
    protected val handler: HTItemHandler = when (isClientSide) {
        Dist.CLIENT -> null
        Dist.DEDICATED_SERVER -> RagiumCapabilities.ITEM.getSlottedCapability(stack)
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
