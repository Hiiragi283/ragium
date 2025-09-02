package hiiragi283.ragium.api.inventory.container

import hiiragi283.ragium.api.inventory.HTMenuCallback
import hiiragi283.ragium.api.registry.impl.HTDeferredMenuType
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player

/**
 * [C]を受け取る[HTContainerMenu]の拡張クラス
 * @see [mekanism.common.inventory.container.tile.MekanismTileContainer]
 */
abstract class HTContainerWithContextMenu<C : Any>(
    menuType: HTDeferredMenuType<*>,
    containerId: Int,
    inventory: Inventory,
    val context: C,
) : HTContainerMenu(
        menuType,
        containerId,
        inventory,
    ) {
    override fun onOpen(player: Player) {
        super.onOpen(player)
        (context as? HTMenuCallback)?.openMenu(player)
    }

    override fun onClose(player: Player) {
        super.onClose(player)
        (context as? HTMenuCallback)?.closeMenu(player)
    }
}
