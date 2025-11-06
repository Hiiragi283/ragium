package hiiragi283.ragium.api.inventory.container

import hiiragi283.ragium.api.inventory.HTMenuCallback
import hiiragi283.ragium.api.registry.impl.HTDeferredMenuType
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player

/**
 * [C]を受け取る[HTContainerMenu]の拡張クラス
 * @see mekanism.common.inventory.container.tile.MekanismTileContainer
 */
abstract class HTContainerWithContextMenu<C : Any>(
    menuType: HTDeferredMenuType.WithContext<*, C>,
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

    override fun removed(player: Player) {
        super.removed(player)
        (context as? HTMenuCallback)?.closeMenu(player)
    }
}
