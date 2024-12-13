package hiiragi283.ragium.api.storage

import net.minecraft.entity.player.PlayerEntity

fun interface HTFluidInteractable {
    fun interactWithFluidStorage(player: PlayerEntity): Boolean
}
