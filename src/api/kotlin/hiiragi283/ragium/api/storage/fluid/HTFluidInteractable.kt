package hiiragi283.ragium.api.storage.fluid

import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidUtil
import net.neoforged.neoforge.fluids.capability.IFluidHandler

fun interface HTFluidInteractable {
    fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult

    fun interactWith(player: Player, hand: InteractionHand, handler: IFluidHandler): ItemInteractionResult = when {
        FluidUtil.interactWithFluidHandler(player, hand, handler) ->
            ItemInteractionResult.sidedSuccess(player.level().isClientSide)

        else -> ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
    }

    fun interactWith(player: Player, hand: InteractionHand, tank: HTFluidTank): ItemInteractionResult =
        interactWith(player, hand, tank.toSingleHandler())
}
