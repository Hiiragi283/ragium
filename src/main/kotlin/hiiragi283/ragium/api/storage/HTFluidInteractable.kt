package hiiragi283.ragium.api.storage

import net.minecraft.entity.player.PlayerEntity

/**
 * プレイヤーとのストレージの干渉を扱うインターフェース
 */
fun interface HTFluidInteractable {
    fun interactWithFluidStorage(player: PlayerEntity): Boolean
}
