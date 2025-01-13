package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.fluid.HTTieredFluidHandler
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTRecipeProcessor
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidUtil
import net.neoforged.neoforge.items.IItemHandler
import java.util.function.Supplier

abstract class HTRecipeProcessorBlockEntity(type: Supplier<out BlockEntityType<*>>, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(type, pos, state) {
    protected abstract val itemHandler: IItemHandler

    protected abstract val fluidHandler: HTTieredFluidHandler

    protected abstract val processor: HTRecipeProcessor

    override fun onUpdateTier(oldTier: HTMachineTier, newTier: HTMachineTier) {
        fluidHandler.onUpdateTier(oldTier, newTier)
    }

    final override fun process(level: ServerLevel, pos: BlockPos) {
        checkMultiblockOrThrow()
        processor.process(level, tier)
    }

    final override fun interactWithFluidStorage(player: Player): Boolean =
        FluidUtil.interactWithFluidHandler(player, InteractionHand.MAIN_HAND, fluidHandler)

    //    HTBlockEntityHandlerProvider    //

    final override fun getItemHandler(direction: Direction?): IItemHandler? = itemHandler

    final override fun getFluidHandler(direction: Direction?): HTTieredFluidHandler = fluidHandler
}
