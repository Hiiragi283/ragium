package hiiragi283.ragium.common.inventory.container

import hiiragi283.ragium.api.inventory.HTMenuCallback
import hiiragi283.ragium.api.inventory.container.HTItemContainerContext
import hiiragi283.ragium.api.registry.impl.HTDeferredMenuType
import hiiragi283.ragium.api.storage.capability.HTItemCapabilities
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
        Dist.DEDICATED_SERVER -> HTItemCapabilities.getItemHandler(stack)
    } ?: createHandler(rows)

    protected abstract fun createHandler(rows: Int): HTItemHandler

    init {
        check(handler.slots >= rows) { "Item handler size ${handler.slots} is smaller than expected $rows" }
        (handler as? HTMenuCallback)?.openMenu(inventory.player)
        val i: Int = (rows - 3) * 18 + 1

        addSlots(handler)

        addPlayerInv(inventory, i)
    }

    override fun removed(player: Player) {
        super.removed(player)
        (handler as? HTMenuCallback)?.closeMenu(player)
    }
}
