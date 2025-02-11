package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.capability.HTHandlerSerializer
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.energy.HTMachineEnergyData
import hiiragi283.ragium.api.fluid.HTMachineFluidTank
import hiiragi283.ragium.api.item.HTMachineItemHandler
import hiiragi283.ragium.api.recipe.HTRefineryRecipe
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeInput
import hiiragi283.ragium.api.recipe.base.HTRecipeCache
import hiiragi283.ragium.api.util.HTRelativeDirection
import hiiragi283.ragium.common.fluid.HTReadOnlyFluidHandler
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import hiiragi283.ragium.common.inventory.HTRefineryContainerMenu
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandlerModifiable

class HTRefineryBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.REFINERY, pos, state, RagiumMachineKeys.REFINERY) {
    private val itemOutput: HTMachineItemHandler = RagiumAPI.getInstance().createItemHandler(this::setChanged)
    private val inputTank: HTMachineFluidTank = RagiumAPI.getInstance().createTank(this::setChanged)
    private val outputTank: HTMachineFluidTank = RagiumAPI.getInstance().createTank(this::setChanged)

    override val handlerSerializer: HTHandlerSerializer = HTHandlerSerializer.of(
        listOf(itemOutput.createSlot(0)),
        listOf(inputTank, outputTank),
    )

    private val recipeCache: HTRecipeCache<HTMachineRecipeInput, HTRefineryRecipe> =
        HTRecipeCache(RagiumRecipeTypes.REFINERY)

    override fun updateEnchantments(newEnchantments: ItemEnchantments) {
        super.updateEnchantments(newEnchantments)
        inputTank.updateCapacity(this)
        outputTank.updateCapacity(this)
    }

    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.consume(1600)

    override fun process(level: ServerLevel, pos: BlockPos) {
        // Find matching recipe
        val input: HTMachineRecipeInput = HTMachineRecipeInput.of(enchantments, inputTank.fluid)
        val holder: RecipeHolder<HTRefineryRecipe> = recipeCache.getFirstRecipe(input, level).getOrThrow()
        val recipe: HTRefineryRecipe = holder.value
        // Try to insert outputs
        recipe.canInsert(itemOutput, outputTank)
        // Insert outputs
        recipe.insertOutputs(itemOutput, outputTank, level, pos)
        // Decrement input
        inputTank.drain(recipe.input.amount(), IFluidHandler.FluidAction.EXECUTE)
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        HTRefineryContainerMenu(containerId, playerInventory, blockPos, itemOutput)

    override fun interactWithFluidStorage(player: Player): Boolean {
        if (outputTank.interactWithFluidStorage(player, HTStorageIO.OUTPUT)) {
            return true
        }
        if (inputTank.interactWithFluidStorage(player, HTStorageIO.GENERIC)) {
            return true
        }
        return false
    }

    override fun getItemHandler(direction: Direction?): IItemHandlerModifiable = HTStorageIO.OUTPUT.wrapItemHandler(itemOutput)

    override fun getFluidHandler(direction: Direction?): IFluidHandler? {
        if (direction == null) {
            return HTReadOnlyFluidHandler(inputTank, outputTank)
        }
        val relativeFront: HTRelativeDirection = HTRelativeDirection.fromDirection(front, direction)
        return when (relativeFront) {
            HTRelativeDirection.RIGHT -> HTStorageIO.INPUT.wrapFluidHandler(inputTank)
            HTRelativeDirection.LEFT -> HTStorageIO.INPUT.wrapFluidHandler(inputTank)
            HTRelativeDirection.FRONT -> HTStorageIO.OUTPUT.wrapFluidHandler(outputTank)
            else -> null
        }
    }
}
