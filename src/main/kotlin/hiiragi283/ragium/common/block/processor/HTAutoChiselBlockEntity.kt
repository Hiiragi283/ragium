package hiiragi283.ragium.common.block.processor

import hiiragi283.ragium.api.machine.HTMachineEnergyData
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.item.crafting.StonecutterRecipe
import net.minecraft.world.level.block.state.BlockState

class HTAutoChiselBlockEntity(pos: BlockPos, state: BlockState) :
    HTSimpleMachineBlockEntity(RagiumBlockEntityTypes.AUTO_CHISEL, pos, state, HTMachineType.AUTO_CHISEL) {
    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData =
        HTMachineEnergyData.Consume.DEFAULT

    override fun process(level: ServerLevel, pos: BlockPos) {
        val input = SingleRecipeInput(inputSlot.stack)
        val recipes: StonecutterRecipe = level.recipeManager
            .getRecipesFor(RecipeType.STONECUTTING, input, level)
            .firstOrNull { holder: RecipeHolder<StonecutterRecipe> ->
                catalystSlot.resource.isOf(holder.value.assemble(input, level.registryAccess()))
            }?.value
            ?: throw HTMachineException.NoMatchingRecipe()
        // Bulk process
        var output: ItemStack = recipes.assemble(input, level.registryAccess())
        val inputCount: Int = 64 / output.count
        output = output.copyWithCount(64)

        if (!outputSlot.canInsert(output)) throw HTMachineException.GrowItem()
        if (!inputSlot.canExtract(inputCount)) throw HTMachineException.ShrinkItem()

        outputSlot.insert(output, false)
        inputSlot.extract(inputCount, false)
    }
}
