package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.fluid.HTMachineFluidTank
import hiiragi283.ragium.api.recipe.HTMachineRecipeInput
import hiiragi283.ragium.api.recipe.HTMixerRecipe
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import hiiragi283.ragium.common.inventory.HTMixerContainerMenu
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
import net.neoforged.neoforge.items.IItemHandlerModifiable
import net.neoforged.neoforge.items.ItemStackHandler

class HTMixerBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.MIXER, pos, state, RagiumMachineKeys.MIXER) {
    private val itemOutput = ItemStackHandler(1)

    private val firstTank = HTMachineFluidTank(8000, this::setChanged)
    private val secondTank = HTMachineFluidTank(8000, this::setChanged)
    private val outputTank = HTMachineFluidTank(8000, this::setChanged)

    private val recipeCache: HTRecipeCache<HTMachineRecipeInput, HTMixerRecipe> =
        HTRecipeCache(RagiumRecipeTypes.MIXER)

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        tag.put(ITEM_OUTPUT_KEY, itemOutput.serializeNBT(registries))
        tag.put("first_fluid_input", firstTank.writeToNBT(registries, CompoundTag()))
        tag.put("second_fluid_input", secondTank.writeToNBT(registries, CompoundTag()))
        tag.put(FLUID_OUTPUT_KEY, outputTank.writeToNBT(registries, CompoundTag()))
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        itemOutput.deserializeNBT(registries, tag.getCompound(ITEM_OUTPUT_KEY))
        firstTank.readFromNBT(registries, tag.getCompound("first_fluid_input"))
        secondTank.readFromNBT(registries, tag.getCompound("second_fluid_input"))
        outputTank.readFromNBT(registries, tag.getCompound(FLUID_OUTPUT_KEY))
    }

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
        firstTank.fluid.shrink(recipe.firstFluid.amount())
        secondTank.fluid.shrink(recipe.secondFluid.amount())
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        HTMixerContainerMenu(containerId, playerInventory, blockPos, itemOutput)

    override fun interactWithFluidStorage(player: Player): Boolean = false

    override fun getItemHandler(direction: Direction?): IItemHandlerModifiable = HTStorageIO.OUTPUT.wrapItemHandler(itemOutput)
}
