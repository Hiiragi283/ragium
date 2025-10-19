package hiiragi283.ragium.api.storage.fluid

import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidUtil
import net.neoforged.neoforge.fluids.capability.IFluidHandler

/**
 * 液体コンテナとやり取りを行うインターフェース
 */
fun interface HTFluidInteractable {
    /**
     * 指定した引数から内部の液体ストレージにアクセスします。
     */
    fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult

    /**
     * 指定した引数から内部の液体ストレージにアクセスします。
     * @param handler 内部の液体ストレージ
     */
    fun interactWith(player: Player, hand: InteractionHand, handler: IFluidHandler): ItemInteractionResult = when {
        FluidUtil.interactWithFluidHandler(player, hand, handler) ->
            ItemInteractionResult.sidedSuccess(player.level().isClientSide)

        else -> ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
    }

    /**
     * 指定した引数から内部の液体ストレージにアクセスします。
     * @param tank 内部の液体ストレージ
     */
    fun interactWith(player: Player, hand: InteractionHand, tank: HTFluidTank): ItemInteractionResult =
        interactWith(player, hand, HTFluidHandler { listOf(tank) })
}
