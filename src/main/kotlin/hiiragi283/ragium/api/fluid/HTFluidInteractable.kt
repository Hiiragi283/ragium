package hiiragi283.ragium.api.fluid

import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.fluids.FluidActionResult
import net.neoforged.neoforge.fluids.FluidUtil
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.wrapper.PlayerMainInvWrapper

/**
 * プレイヤーとのストレージの干渉を扱うインターフェース
 */
fun interface HTFluidInteractable {
    fun interactWithFluidStorage(player: Player): Boolean

    fun drainPlayerContainer(player: Player, dest: IFluidHandler, doDrain: Boolean): FluidActionResult = FluidUtil.tryEmptyContainerAndStow(
        player.getItemInHand(InteractionHand.MAIN_HAND),
        dest,
        PlayerMainInvWrapper(player.inventory),
        Int.MAX_VALUE,
        player,
        doDrain,
    )

    fun fillPlayerContainer(player: Player, source: IFluidHandler, doFill: Boolean): FluidActionResult = FluidUtil.tryFillContainerAndStow(
        player.getItemInHand(InteractionHand.MAIN_HAND),
        source,
        PlayerMainInvWrapper(player.inventory),
        Int.MAX_VALUE,
        player,
        doFill,
    )
}
