package hiiragi283.ragium.common.block.processor

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.capability.HTHandlerSerializer
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.capability.energy.HTMachineEnergyData
import hiiragi283.ragium.api.capability.fluid.HTMachineFluidTank
import hiiragi283.ragium.api.capability.item.HTMachineItemHandler
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTMixerRecipe
import hiiragi283.ragium.api.recipe.HTRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTItemIngredient
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeInput
import hiiragi283.ragium.api.util.HTRelativeDirection
import hiiragi283.ragium.common.capability.fluid.HTReadOnlyFluidHandler
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.inventory.HTMixerContainerMenu
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandlerModifiable

class HTMixerBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.MIXER, pos, state, HTMachineType.MIXER) {
    private val itemInput: HTMachineItemHandler = RagiumAPI.getInstance().createItemHandler(this::setChanged)
    private val itemOutput: HTMachineItemHandler = RagiumAPI.getInstance().createItemHandler(this::setChanged)
    private val firstTank: HTMachineFluidTank = RagiumAPI.getInstance().createTank(this::setChanged)
    private val secondTank: HTMachineFluidTank = RagiumAPI.getInstance().createTank(this::setChanged)
    private val outputTank: HTMachineFluidTank = RagiumAPI.getInstance().createTank(this::setChanged)

    override val handlerSerializer: HTHandlerSerializer = HTHandlerSerializer.of(
        listOf(
            itemInput.createSlot(0),
            itemOutput.createSlot(0),
        ),
        listOf(firstTank, secondTank, outputTank),
    )

    override fun updateEnchantments(newEnchantments: ItemEnchantments) {
        super.updateEnchantments(newEnchantments)
        firstTank.updateCapacity(this)
        secondTank.updateCapacity(this)
        outputTank.updateCapacity(this)
    }

    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.Consume.CHEMICAL

    override fun process(level: ServerLevel, pos: BlockPos) {
        // Find matching recipe
        val input: HTMachineRecipeInput = HTMachineRecipeInput
            .Builder()
            .addItem(itemInput, 0)
            .addFluid(firstTank)
            .addFluid(secondTank)
            .build()
        val recipe: HTMixerRecipe = HTRecipeTypes.MIXER.getFirstRecipe(input, level).getOrThrow()
        // Try to insert outputs
        recipe.canInsert(enchantments, itemOutput, outputTank)
        // Insert outputs
        recipe.insertOutputs(level, pos, enchantments, itemOutput, outputTank)
        // Decrement input
        itemInput.getStackInSlot(0).shrink(recipe.itemInput.map(HTItemIngredient::count).orElse(0))
        firstTank.drain(recipe.firstFluid.amount(), IFluidHandler.FluidAction.EXECUTE)
        secondTank.drain(recipe.secondFluid.amount(), IFluidHandler.FluidAction.EXECUTE)
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        HTMixerContainerMenu(containerId, playerInventory, blockPos, itemInput, itemOutput)

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
