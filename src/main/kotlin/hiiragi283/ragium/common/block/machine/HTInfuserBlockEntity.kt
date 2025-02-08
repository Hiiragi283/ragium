package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.capability.HTHandlerSerializer
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.energy.HTMachineEnergyData
import hiiragi283.ragium.api.fluid.HTMachineFluidTank
import hiiragi283.ragium.api.item.HTMachineItemHandler
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.recipe.HTInfuserRecipe
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeInput
import hiiragi283.ragium.api.util.HTRelativeDirection
import hiiragi283.ragium.common.fluid.HTReadOnlyFluidHandler
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.inventory.HTInfuserContainerMenu
import hiiragi283.ragium.common.recipe.HTRecipeConverters
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper

class HTInfuserBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.INFUSER, pos, state, RagiumMachineKeys.INFUSER) {
    private val itemInput = HTMachineItemHandler(1, this::setChanged)
    private val inputTank: HTMachineFluidTank = RagiumAPI.getInstance().createTank(this::setChanged)
    private val itemOutput = HTMachineItemHandler(1, this::setChanged)
    private val outputTank: HTMachineFluidTank = RagiumAPI.getInstance().createTank(this::setChanged)

    override val handlerSerializer: HTHandlerSerializer = HTHandlerSerializer.of(
        listOf(
            itemInput.createSlot(0),
            itemOutput.createSlot(0),
        ),
        listOf(inputTank, outputTank),
    )

    override fun updateEnchantments(newEnchantments: ItemEnchantments) {
        super.updateEnchantments(newEnchantments)
        inputTank.updateCapacity(this)
        outputTank.updateCapacity(this)
    }

    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.consume(1600)

    override fun process(level: ServerLevel, pos: BlockPos) {
        // Find matching recipe
        val foundRecipes: MutableList<HTInfuserRecipe> = mutableListOf()
        HTRecipeConverters.infuser(
            level.recipeManager,
            RagiumAPI.getInstance().getMaterialRegistry(),
            foundRecipes::add,
        )
        if (foundRecipes.isEmpty()) throw HTMachineException.NoMatchingRecipe(false)
        val input: HTMachineRecipeInput = HTMachineRecipeInput.of(
            pos,
            itemInput.getStackInSlot(0),
            inputTank.fluid,
        )
        var foundRecipe: HTInfuserRecipe? = null
        for (recipe: HTInfuserRecipe in foundRecipes) {
            if (recipe.matches(input, level)) {
                foundRecipe = recipe
                break
            }
        }
        if (foundRecipe == null) throw HTMachineException.NoMatchingRecipe(false)
        // Try to insert outputs
        foundRecipe.canInsert(itemOutput, outputTank)
        // Insert outputs
        foundRecipe.insertOutputs(itemOutput, outputTank, level, pos)
        // Decrement input
        itemInput.getStackInSlot(0).shrink(foundRecipe.itemInput.count())
        inputTank.drain(foundRecipe.fluidInput.amount(), IFluidHandler.FluidAction.EXECUTE)
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        HTInfuserContainerMenu(containerId, playerInventory, blockPos, CombinedInvWrapper(itemInput, itemOutput))

    override fun interactWithFluidStorage(player: Player): Boolean {
        if (outputTank.interactWithFluidStorage(player, HTStorageIO.OUTPUT)) {
            return true
        }
        if (inputTank.interactWithFluidStorage(player, HTStorageIO.GENERIC)) {
            return true
        }
        return false
    }

    override fun getItemHandler(direction: Direction?): CombinedInvWrapper =
        CombinedInvWrapper(HTStorageIO.INPUT.wrapItemHandler(itemInput), HTStorageIO.OUTPUT.wrapItemHandler(itemOutput))

    override fun getFluidHandler(direction: Direction?): IFluidHandler? {
        if (direction == null) {
            return HTReadOnlyFluidHandler(inputTank, outputTank)
        }
        val relativeFront: HTRelativeDirection = HTRelativeDirection.fromDirection(front, direction)
        return when (relativeFront) {
            HTRelativeDirection.RIGHT -> HTStorageIO.INPUT.wrapFluidHandler(inputTank)
            HTRelativeDirection.LEFT -> HTStorageIO.OUTPUT.wrapFluidHandler(outputTank)
            else -> null
        }
    }
}
