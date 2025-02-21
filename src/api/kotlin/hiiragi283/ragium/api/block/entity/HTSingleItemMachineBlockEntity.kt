package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.capability.HTHandlerSerializer
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.extension.canInsert
import hiiragi283.ragium.api.extension.insertOrDrop
import hiiragi283.ragium.api.item.HTMachineItemHandler
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeInput
import hiiragi283.ragium.api.recipe.base.HTRecipeGetter
import hiiragi283.ragium.api.recipe.base.HTSingleItemRecipe
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper
import java.util.function.Supplier

abstract class HTSingleItemMachineBlockEntity(
    type: Supplier<out BlockEntityType<*>>,
    pos: BlockPos,
    state: BlockState,
    machineType: HTMachineType,
) : HTMachineBlockEntity(type, pos, state, machineType) {
    private val itemInput: HTMachineItemHandler = RagiumAPI.getInstance().createItemHandler(this::setChanged)
    private val itemCatalyst: HTMachineItemHandler = RagiumAPI.getInstance().createItemHandler(this::setChanged)
    private val itemOutput: HTMachineItemHandler = RagiumAPI.getInstance().createItemHandler(this::setChanged)

    final override val handlerSerializer: HTHandlerSerializer = HTHandlerSerializer.ofItem(
        listOf(
            itemInput.createSlot(0),
            itemCatalyst.createSlot(0),
            itemOutput.createSlot(0),
        ),
    )

    abstract val recipeGetter: HTRecipeGetter<HTMachineRecipeInput, out HTSingleItemRecipe>

    final override fun process(level: ServerLevel, pos: BlockPos) {
        // Find matching recipe
        val input: HTMachineRecipeInput = HTMachineRecipeInput.of(
            enchantments,
            itemInput.getStackInSlot(0),
            itemCatalyst.getStackInSlot(0),
        )
        val recipe: HTSingleItemRecipe = recipeGetter.getFirstRecipe(input, level).getOrThrow()
        val output: ItemStack = recipe.itemOutput.get()
        // Try to insert outputs
        if (!itemOutput.canInsert(output)) throw HTMachineException.MergeResult(false)
        // Insert outputs
        itemOutput.insertOrDrop(level, pos.above(), output)
        // Decrement input
        itemInput.getStackInSlot(0).shrink(recipe.input.count)
    }

    final override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu =
        RagiumAPI.getInstance().createSingleItemMenu(
            containerId,
            playerInventory,
            blockPos,
            itemInput,
            itemCatalyst,
            itemOutput,
        )

    final override fun interactWithFluidStorage(player: Player): Boolean = false

    final override fun getItemHandler(direction: Direction?): CombinedInvWrapper = CombinedInvWrapper(
        HTStorageIO.INPUT.wrapItemHandler(itemInput),
        HTStorageIO.INTERNAL.wrapItemHandler(itemCatalyst),
        HTStorageIO.OUTPUT.wrapItemHandler(itemOutput),
    )
}
