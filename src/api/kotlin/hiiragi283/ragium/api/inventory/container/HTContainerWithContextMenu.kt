package hiiragi283.ragium.api.inventory.container

import hiiragi283.ragium.api.registry.HTDeferredMenuType
import net.minecraft.world.entity.player.Inventory

/**
 * @see [mekanism.common.inventory.container.tile.MekanismTileContainer]
 */
abstract class HTContainerWithContextMenu<C>(
    menuType: HTDeferredMenuType<*, C>,
    containerId: Int,
    inventory: Inventory,
    val context: C,
) : HTContainerMenu(
        menuType,
        containerId,
        inventory,
    )
