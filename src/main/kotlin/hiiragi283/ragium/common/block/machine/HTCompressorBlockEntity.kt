package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.capability.HTHandlerSerializer
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.extension.canInsert
import hiiragi283.ragium.api.extension.insertOrDrop
import hiiragi283.ragium.api.item.HTMachineItemHandler
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.recipe.HTCompressorRecipe
import hiiragi283.ragium.api.recipe.HTMachineRecipeInput
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.inventory.HTCompressorContainerMenu
import hiiragi283.ragium.common.recipe.HTRecipeConverters
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper

class HTCompressorBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.COMPRESSOR, pos, state, RagiumMachineKeys.COMPRESSOR) {
    private val itemInput = HTMachineItemHandler(1, this::setChanged)
    private val itemOutput = HTMachineItemHandler(1, this::setChanged)

    override val handlerSerializer: HTHandlerSerializer = HTHandlerSerializer.ofItem(
        listOf(
            itemInput.createSlot(0),
            itemOutput.createSlot(0),
        ),
    )

    override fun process(level: ServerLevel, pos: BlockPos) {
        // Find matching recipe
        val foundRecipes: MutableList<HTCompressorRecipe> = mutableListOf()
        HTRecipeConverters.compressor(level.recipeManager, RagiumAPI.materialRegistry, foundRecipes::add)
        if (foundRecipes.isEmpty()) throw HTMachineException.NoMatchingRecipe(false)
        val input: HTMachineRecipeInput = HTMachineRecipeInput.of(pos, itemInput.getStackInSlot(0))
        var foundRecipe: HTCompressorRecipe? = foundRecipes.firstOrNull { it.matches(input, level) }
        if (foundRecipe == null) throw HTMachineException.NoMatchingRecipe(false)
        val output: ItemStack = foundRecipe.getItemOutput()
        // Try to insert outputs
        if (!itemOutput.canInsert(output)) throw HTMachineException.MergeResult(false)
        // Insert outputs
        itemOutput.insertOrDrop(level, pos, output)
        // Decrement input
        itemInput.getStackInSlot(0).shrink(foundRecipe.input.count())
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        HTCompressorContainerMenu(containerId, playerInventory, blockPos, CombinedInvWrapper(itemInput, itemOutput))

    override fun interactWithFluidStorage(player: Player): Boolean = false

    override fun getItemHandler(direction: Direction?): CombinedInvWrapper =
        CombinedInvWrapper(HTStorageIO.INPUT.wrapItemHandler(itemInput), HTStorageIO.OUTPUT.wrapItemHandler(itemOutput))
}
