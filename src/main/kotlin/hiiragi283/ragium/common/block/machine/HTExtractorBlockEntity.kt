package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.fluid.HTMachineFluidTank
import hiiragi283.ragium.api.recipe.HTExtractorRecipe
import hiiragi283.ragium.api.recipe.HTMachineRecipeInput
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import hiiragi283.ragium.common.inventory.HTExtractorContainerMenu
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.ItemStackHandler
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper

class HTExtractorBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.EXTRACTOR, pos, state, RagiumMachineKeys.EXTRACTOR) {
    private val itemInput = ItemStackHandler(1)
    private val itemOutput = ItemStackHandler(1)
    private val outputTank = HTMachineFluidTank(8000, this::setChanged)

    private val recipeCache: HTRecipeCache<HTMachineRecipeInput, HTExtractorRecipe> =
        HTRecipeCache(RagiumRecipeTypes.EXTRACTOR)

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        tag.put(ITEM_INPUT_KEY, itemInput.serializeNBT(registries))
        tag.put(ITEM_OUTPUT_KEY, itemOutput.serializeNBT(registries))
        tag.put(FLUID_OUTPUT_KEY, outputTank.writeToNBT(registries, CompoundTag()))
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        itemInput.deserializeNBT(registries, tag.getCompound(ITEM_INPUT_KEY))
        itemOutput.deserializeNBT(registries, tag.getCompound(ITEM_OUTPUT_KEY))
        outputTank.readFromNBT(registries, tag.getCompound(FLUID_OUTPUT_KEY))
    }

    override fun process(level: ServerLevel, pos: BlockPos) {
        // Find matching recipe
        val input: HTMachineRecipeInput = HTMachineRecipeInput.of(pos, itemInput.getStackInSlot(0))
        val holder: RecipeHolder<HTExtractorRecipe> = recipeCache.getFirstRecipe(input, level).getOrThrow()
        val recipe: HTExtractorRecipe = holder.value
        // Try to insert outputs
        recipe.canInsert(itemOutput, outputTank)
        // Insert outputs
        recipe.insertOutputs(itemOutput, outputTank, level, pos)
        // Decrement input
        itemInput.getStackInSlot(0).shrink(recipe.input.count())
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        HTExtractorContainerMenu(containerId, playerInventory, blockPos, CombinedInvWrapper(itemInput, itemOutput))

    override fun interactWithFluidStorage(player: Player): Boolean = fillPlayerContainer(player, outputTank, true).isSuccess

    override fun getItemHandler(direction: Direction?): CombinedInvWrapper =
        CombinedInvWrapper(HTStorageIO.INPUT.wrapItemHandler(itemInput), HTStorageIO.OUTPUT.wrapItemHandler(itemOutput))

    override fun getFluidHandler(direction: Direction?): IFluidHandler = HTStorageIO.OUTPUT.wrapFluidHandler(outputTank)
}
