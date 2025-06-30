package hiiragi283.ragium.api.storage.fluid

import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.capability.IFluidHandler

interface HTFluidHandler : IFluidHandler {
    val tankRange: IntRange get() = (0 until tanks)

    fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult
}
