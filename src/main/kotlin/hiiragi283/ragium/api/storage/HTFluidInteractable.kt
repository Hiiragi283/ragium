package hiiragi283.ragium.api.storage

import net.minecraft.entity.player.PlayerEntity

/**
 * Handle [PlayerEntity] interaction to [net.fabricmc.fabric.api.transfer.v1.storage.Storage]
 */
fun interface HTFluidInteractable {
    /**
     * @return true if interaction succeeded, or false if not
     */
    fun interactWithFluidStorage(player: PlayerEntity): Boolean
}
