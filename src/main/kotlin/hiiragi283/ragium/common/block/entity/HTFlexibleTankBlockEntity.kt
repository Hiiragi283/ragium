package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.block.entity.HTFluidInteractable
import hiiragi283.ragium.api.block.entity.HTHandlerBlockEntity
import hiiragi283.ragium.api.network.HTNbtCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.capability.IFluidHandler

class HTFlexibleTankBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntity(TODO(), pos, state),
    HTFluidInteractable,
    HTHandlerBlockEntity {
    private fun getTank(): IFluidHandler? = null

    override fun writeNbt(writer: HTNbtCodec.Writer) {}

    override fun readNbt(reader: HTNbtCodec.Reader) {}

    //    HTHandlerBlockEntity    //

    override fun getFluidHandler(direction: Direction?): IFluidHandler? = getTank()

    //    HTFluidInteractable    //

    override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult {
        val tank: IFluidHandler = getTank() ?: return ItemInteractionResult.FAIL
        return interactWith(player, hand, tank)
    }
}
