package hiiragi283.ragium.common.block.processor

import hiiragi283.ragium.api.heat.HTHeatTier
import hiiragi283.ragium.api.machine.HTMachineEnergyData
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.block.state.BlockState

class HTMultiSmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTSimpleMachineBlockEntity(RagiumBlockEntityTypes.MULTI_SMELTER, pos, state, HTMachineType.MULTI_SMELTER) {
    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = when {
        HTHeatTier.getHeatTier(level, pos.below(), Direction.UP) >= HTHeatTier.HIGH -> HTMachineEnergyData.Consume.EMPTY
        else -> HTMachineEnergyData.Consume.PRECISION
    }

    private val recipeCache: RecipeManager.CachedCheck<SingleRecipeInput, SmeltingRecipe> =
        RecipeManager.createCheck(RecipeType.SMELTING)

    override fun process(level: ServerLevel, pos: BlockPos) {
        val input = SingleRecipeInput(inputSlot.stack)
        val recipe: AbstractCookingRecipe = recipeCache
            .getRecipeFor(input, level)
            .orElseThrow(HTMachineException::NoMatchingRecipe)
            .value
        // Bulk process
        var output: ItemStack = recipe.assemble(input, level.registryAccess())
        val inputCount: Int = inputSlot.amount
        output = output.copyWithCount(output.count * inputCount)

        if (!outputSlot.canInsert(output)) throw HTMachineException.GrowItem()
        if (!inputSlot.canExtract(inputCount)) throw HTMachineException.ShrinkItem()

        outputSlot.insert(output, false)
        inputSlot.extract(inputCount, false)
    }
}
