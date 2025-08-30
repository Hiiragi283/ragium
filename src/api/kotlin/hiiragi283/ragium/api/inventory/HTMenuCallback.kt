package hiiragi283.ragium.api.inventory

import hiiragi283.ragium.api.inventory.container.HTContainerMenu
import net.minecraft.world.entity.player.Player

/**
 * GUIが開かれる/閉じられる時に呼び出されるインターフェース
 * @see [HTContainerMenu.onOpen]
 * @see [HTContainerMenu.onClose]
 */
interface HTMenuCallback {
    fun openMenu(player: Player) {}

    fun closeMenu(player: Player) {}
}
