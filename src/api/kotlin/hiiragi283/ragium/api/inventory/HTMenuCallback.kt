package hiiragi283.ragium.api.inventory

import net.minecraft.world.entity.player.Player

/**
 * GUIが開かれる/閉じられる時に呼び出されるインターフェース
 */
interface HTMenuCallback {
    fun openMenu(player: Player) {}

    fun closeMenu(player: Player) {}
}
