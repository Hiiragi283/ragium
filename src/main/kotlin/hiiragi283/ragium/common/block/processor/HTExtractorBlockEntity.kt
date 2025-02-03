package hiiragi283.ragium.common.block.processor

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.extension.canFill
import hiiragi283.ragium.api.extension.canInsert
import hiiragi283.ragium.api.extension.insertOrDrop
import hiiragi283.ragium.api.fluid.HTMachineFluidTank
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.recipe.HTExtractorRecipe
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import hiiragi283.ragium.common.inventory.HTExtractorContainerMenu
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.ItemStackHandler
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper

class HTExtractorBlockEntity(pos: BlockPos, state: BlockState) : HTMachineBlockEntity(RagiumBlockEntityTypes.EXTRACTOR, pos, state) {
    override val machineKey: HTMachineKey = RagiumMachineKeys.EXTRACTOR

    private val itemInput = ItemStackHandler(1)
    private val itemOutput = ItemStackHandler(1)
    private val outputTank = HTMachineFluidTank(8000, this::setChanged)

    private val recipeCache: HTRecipeCache<SingleRecipeInput, HTExtractorRecipe> =
        HTRecipeCache(RagiumRecipeTypes.EXTRACTOR)

    override fun process(level: ServerLevel, pos: BlockPos) {
        // Find matching recipe
        val input = SingleRecipeInput(itemInput.getStackInSlot(0))
        val holder: RecipeHolder<HTExtractorRecipe> = recipeCache.getFirstRecipe(input, level).getOrThrow()
        val recipe: HTExtractorRecipe = holder.value
        // Try to insert outputs
        recipe.itemOutput.ifPresent { output: ItemStack ->
            if (!itemOutput.canInsert(output)) throw HTMachineException.MergeResult(false)
        }
        recipe.fluidOutput.ifPresent { output: FluidStack ->
            if (!outputTank.canFill(output)) throw HTMachineException.MergeResult(false)
        }
        // Insert outputs
        recipe.itemOutput.ifPresent { output: ItemStack ->
            itemOutput.insertOrDrop(level, pos.above(), output)
        }
        recipe.fluidOutput.ifPresent { output: FluidStack ->
            outputTank.fill(output, IFluidHandler.FluidAction.EXECUTE)
        }
        // Decrement input
        itemInput.getStackInSlot(0).shrink(1)
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        HTExtractorContainerMenu(containerId, playerInventory, blockPos, CombinedInvWrapper(itemInput, itemOutput))

    override fun interactWithFluidStorage(player: Player): Boolean = fillPlayerContainer(player, outputTank, true).isSuccess

    override fun getItemHandler(direction: Direction?): CombinedInvWrapper =
        CombinedInvWrapper(HTStorageIO.INPUT.wrapItemHandler(itemInput), HTStorageIO.OUTPUT.wrapItemHandler(itemOutput))

    override fun getFluidHandler(direction: Direction?): IFluidHandler = HTStorageIO.OUTPUT.wrapFluidHandler(outputTank)
}
