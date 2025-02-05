package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.capability.HTHandlerSerializer
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.fluid.HTCombinedFluidHandler
import hiiragi283.ragium.api.fluid.HTMachineFluidTank
import hiiragi283.ragium.api.item.HTMachineItemHandler
import hiiragi283.ragium.api.recipe.HTMachineRecipeInput
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTRefineryRecipe
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import hiiragi283.ragium.common.inventory.HTExtractorContainerMenu
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidUtil
import net.neoforged.neoforge.items.IItemHandlerModifiable

class HTRefineryBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.REFINERY, pos, state, RagiumMachineKeys.REFINERY) {
    private val itemOutput = HTMachineItemHandler(1, this::setChanged)
    private val inputTank = HTMachineFluidTank(8000, this::setChanged)
    private val outputTank = HTMachineFluidTank(8000, this::setChanged)

    override val handlerSerializer: HTHandlerSerializer = HTHandlerSerializer.of(
        listOf(itemOutput.createSlot(0)),
        listOf(inputTank, outputTank),
    )

    private val recipeCache: HTRecipeCache<HTMachineRecipeInput, HTRefineryRecipe> =
        HTRecipeCache(RagiumRecipeTypes.REFINERY)

    override fun process(level: ServerLevel, pos: BlockPos) {
        // Find matching recipe
        val input: HTMachineRecipeInput = HTMachineRecipeInput.of(pos, inputTank.getFluidInTank(0))
        val holder: RecipeHolder<HTRefineryRecipe> = recipeCache.getFirstRecipe(input, level).getOrThrow()
        val recipe: HTRefineryRecipe = holder.value
        // Try to insert outputs
        recipe.canInsert(itemOutput, outputTank)
        // Insert outputs
        recipe.insertOutputs(itemOutput, outputTank, level, pos)
        // Decrement input
        inputTank.getFluidInTank(0).shrink(recipe.input.amount())
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        HTExtractorContainerMenu(containerId, playerInventory, blockPos, itemOutput)

    override fun interactWithFluidStorage(player: Player): Boolean = when {
        fillPlayerContainer(player, outputTank, true).isSuccess -> true
        FluidUtil.interactWithFluidHandler(player, InteractionHand.MAIN_HAND, inputTank) -> true
        else -> false
    }

    override fun getItemHandler(direction: Direction?): IItemHandlerModifiable = HTStorageIO.OUTPUT.wrapItemHandler(itemOutput)

    override fun getFluidHandler(direction: Direction?): HTCombinedFluidHandler = HTCombinedFluidHandler(
        HTStorageIO.INPUT.wrapFluidHandler(inputTank),
        HTStorageIO.OUTPUT.wrapFluidHandler(outputTank),
    )
}
