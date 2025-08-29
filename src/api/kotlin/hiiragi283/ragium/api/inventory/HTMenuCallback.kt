package hiiragi283.ragium.api.inventory

import net.minecraft.world.entity.player.Player

interface HTMenuCallback {
    fun openMenu(player: Player) {}

    fun closeMenu(player: Player) {}
}
