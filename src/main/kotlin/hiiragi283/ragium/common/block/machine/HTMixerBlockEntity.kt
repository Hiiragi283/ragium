package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.capability.HTHandlerSerializer
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.energy.HTMachineEnergyData
import hiiragi283.ragium.api.fluid.HTMachineFluidTank
import hiiragi283.ragium.api.fluid.HTReadOnlyFluidHandler
import hiiragi283.ragium.api.item.HTMachineItemHandler
import hiiragi283.ragium.api.recipe.HTMachineRecipeInput
import hiiragi283.ragium.api.recipe.HTMixerRecipe
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.util.HTRelativeDirection
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import hiiragi283.ragium.common.inventory.HTMixerContainerMenu
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

class HTMixerBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.MIXER, pos, state, RagiumMachineKeys.MIXER) {
    private val itemOutput = HTMachineItemHandler(1, this::setChanged)
    private val firstTank = HTMachineFluidTank(this::setChanged)
    private val secondTank = HTMachineFluidTank(this::setChanged)
    private val outputTank = HTMachineFluidTank(this::setChanged)

    override val handlerSerializer: HTHandlerSerializer = HTHandlerSerializer.of(
        listOf(
            itemOutput.createSlot(0),
        ),
        listOf(firstTank, secondTank, outputTank),
    )

    private val recipeCache: HTRecipeCache<HTMachineRecipeInput, HTMixerRecipe> =
        HTRecipeCache(RagiumRecipeTypes.MIXER)

    override fun updateEnchantments(newEnchantments: ItemEnchantments) {
        super.updateEnchantments(newEnchantments)
        firstTank.updateCapacity(this)
        secondTank.updateCapacity(this)
        outputTank.updateCapacity(this)
    }

    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.consume(1600)

    override fun process(level: ServerLevel, pos: BlockPos) {
        // Find matching recipe
        val input: HTMachineRecipeInput = HTMachineRecipeInput.of(
            pos,
            listOf(),
            listOf(firstTank.fluid, secondTank.fluid),
        )
        val holder: RecipeHolder<HTMixerRecipe> = recipeCache.getFirstRecipe(input, level).getOrThrow()
        val recipe: HTMixerRecipe = holder.value
        // Try to insert outputs
        recipe.canInsert(itemOutput, outputTank)
        // Insert outputs
        recipe.insertOutputs(itemOutput, outputTank, level, pos)
        // Decrement input
        firstTank.drain(recipe.firstFluid.amount(), IFluidHandler.FluidAction.EXECUTE)
        secondTank.drain(recipe.secondFluid.amount(), IFluidHandler.FluidAction.EXECUTE)
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        HTMixerContainerMenu(containerId, playerInventory, blockPos, itemOutput)

    override fun interactWithFluidStorage(player: Player): Boolean {
        if (outputTank.interactWithFluidStorage(player, HTStorageIO.OUTPUT)) {
            return true
        }
        if (firstTank.interactWithFluidStorage(player, HTStorageIO.GENERIC)) {
            return true
        }
        if (secondTank.interactWithFluidStorage(player, HTStorageIO.GENERIC)) {
            return true
        }
        return false
    }

    override fun getItemHandler(direction: Direction?): IItemHandlerModifiable = HTStorageIO.OUTPUT.wrapItemHandler(itemOutput)

    override fun getFluidHandler(direction: Direction?): IFluidHandler? {
        if (direction == null) {
            return HTReadOnlyFluidHandler(firstTank, secondTank, outputTank)
        }
        val relativeFront: HTRelativeDirection = HTRelativeDirection.fromDirection(front, direction)
        return when (relativeFront) {
            HTRelativeDirection.RIGHT -> HTStorageIO.INPUT.wrapFluidHandler(firstTank)
            HTRelativeDirection.LEFT -> HTStorageIO.INPUT.wrapFluidHandler(secondTank)
            HTRelativeDirection.FRONT -> HTStorageIO.OUTPUT.wrapFluidHandler(outputTank)
            else -> null
        }
    }
}
