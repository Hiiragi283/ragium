package hiiragi283.ragium.api.inventory.container

import hiiragi283.ragium.api.inventory.HTMenuCallback
import hiiragi283.ragium.api.registry.HTDeferredMenuType
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player

/**
 * @see [mekanism.common.inventory.container.tile.MekanismTileContainer]
 */
abstract class HTContainerWithContextMenu<C : Any>(
    menuType: HTDeferredMenuType<*, C>,
    containerId: Int,
    inventory: Inventory,
    val context: C,
) : HTContainerMenu(
        menuType,
        containerId,
        inventory,
    ) {
    init {
        (context as? HTMenuCallback)?.openMenu(inventory.player)
    }

    override fun onClose(player: Player) {
        super.onClose(player)
        (context as? HTMenuCallback)?.closeMenu(player)
    }
}
