package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.capability.HTHandlerSerializer
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.extension.canInsert
import hiiragi283.ragium.api.extension.insertOrDrop
import hiiragi283.ragium.api.fluid.HTMachineFluidTank
import hiiragi283.ragium.api.item.HTMachineItemHandler
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeInput
import hiiragi283.ragium.api.recipe.base.HTMultiItemRecipe
import hiiragi283.ragium.api.recipe.base.HTRecipeGetter
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper
import java.util.function.Supplier

abstract class HTMultiItemMachineBlockEntity(
    type: Supplier<out BlockEntityType<*>>,
    pos: BlockPos,
    state: BlockState,
    machineType: HTMachineType,
) : HTMachineBlockEntity(type, pos, state, machineType) {
    private val itemInput: HTMachineItemHandler = RagiumAPI.getInstance().createItemHandler(3, this::setChanged)
    private val fluidInput: HTMachineFluidTank = RagiumAPI.getInstance().createTank(this::setChanged)
    private val itemOutput: HTMachineItemHandler = RagiumAPI.getInstance().createItemHandler(this::setChanged)

    final override val handlerSerializer: HTHandlerSerializer = HTHandlerSerializer.of(
        listOf(
            itemInput.createSlot(0),
            itemInput.createSlot(1),
            itemInput.createSlot(2),
            itemOutput.createSlot(0),
        ),
        listOf(
            fluidInput,
        ),
    )

    abstract val recipeGetter: HTRecipeGetter<HTMachineRecipeInput, out HTMultiItemRecipe>

    override fun process(level: ServerLevel, pos: BlockPos) {
        val input: HTMachineRecipeInput = HTMachineRecipeInput.of(
            enchantments,
            listOf(
                itemInput.getStackInSlot(0),
                itemInput.getStackInSlot(1),
                itemInput.getStackInSlot(2),
            ),
            listOf(
                fluidInput.fluid,
            ),
        )
        val recipe: HTMultiItemRecipe = recipeGetter.getFirstRecipe(input, level).getOrThrow()
        if (!itemInput.canConsumeAll()) throw HTMachineException.ConsumeInput(false)
        val output: ItemStack = recipe.itemOutput.get()
        if (!itemOutput.canInsert(output)) throw HTMachineException.MergeResult(false)
        itemOutput.insertOrDrop(level, pos.above(), output)
        itemInput.consumeItem(0, recipe.itemInputs[0].count, false)
        itemInput.consumeItem(1, recipe.itemInputs.getOrNull(1)?.count ?: 0, false)
        itemInput.consumeItem(2, recipe.itemInputs.getOrNull(2)?.count ?: 0, false)
        recipe.fluidInput.ifPresent { ingredient: SizedFluidIngredient ->
            fluidInput.drain(ingredient.amount(), IFluidHandler.FluidAction.EXECUTE)
        }
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        RagiumAPI.getInstance().createMultiItemMenu(containerId, playerInventory, blockPos, itemInput, itemOutput)

    final override fun interactWithFluidStorage(player: Player): Boolean = fluidInput.interactWithFluidStorage(player, HTStorageIO.INPUT)

    final override fun getItemHandler(direction: Direction?): CombinedInvWrapper = CombinedInvWrapper(
        HTStorageIO.INPUT.wrapItemHandler(itemInput),
        HTStorageIO.OUTPUT.wrapItemHandler(itemOutput),
    )

    override fun getFluidHandler(direction: Direction?): IFluidHandler = HTStorageIO.INPUT.wrapFluidHandler(fluidInput)
}
