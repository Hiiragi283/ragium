package hiiragi283.ragium.api.capability.fluid

import net.minecraft.world.entity.player.Player

/**
 * プレイヤーとのストレージの干渉を扱うインターフェース
 */
fun interface HTFluidInteractable {
    fun interactWithFluidStorage(player: Player): Boolean
}
