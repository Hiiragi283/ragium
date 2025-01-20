package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.extension.dropStacks
import hiiragi283.ragium.api.fluid.HTTieredFluidTank
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTRecipeProcessor
import hiiragi283.ragium.common.recipe.HTMachineRecipeProcessor
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidUtil
import net.neoforged.neoforge.items.ItemStackHandler
import java.util.function.Supplier

abstract class HTRecipeProcessorBlockEntity(type: Supplier<out BlockEntityType<*>>, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(type, pos, state) {
    protected abstract val itemHandler: ItemStackHandler

    protected abstract val tanks: Array<out HTTieredFluidTank>

    protected abstract val processor: HTRecipeProcessor

    protected fun createMachineProcessor(
        itemInputs: IntArray,
        itemOutputs: IntArray,
        catalystIndex: Int,
        fluidInputs: IntArray,
        fluidOutputs: IntArray,
    ): HTMachineRecipeProcessor = HTMachineRecipeProcessor(
        machineKey,
        itemHandler,
        itemInputs,
        itemOutputs,
        catalystIndex,
        tanks::getOrNull,
        fluidInputs,
        fluidOutputs,
    )

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        tag.put(ITEM_KEY, itemHandler.serializeNBT(registries))
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        itemHandler.deserializeNBT(registries, tag.getCompound(ITEM_KEY))
    }

    override fun onUpdateTier(oldTier: HTMachineTier, newTier: HTMachineTier) {
        for (tank: HTTieredFluidTank in tanks) {
            tank.onUpdateTier(oldTier, newTier)
        }
    }

    final override fun process(level: ServerLevel, pos: BlockPos) {
        checkMultiblockOrThrow()
        processor.process(level, machineTier).getOrThrow()
    }

    final override fun interactWithFluidStorage(player: Player): Boolean {
        for (tank: HTTieredFluidTank in tanks.reversed()) {
            if (FluidUtil.interactWithFluidHandler(player, InteractionHand.MAIN_HAND, tank)) return true
        }
        return false
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        itemHandler.dropStacks(level, pos)
    }
}
