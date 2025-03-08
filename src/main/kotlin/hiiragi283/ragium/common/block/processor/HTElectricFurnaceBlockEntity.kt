package hiiragi283.ragium.common.block.processor

import hiiragi283.ragium.api.heat.HTHeatTier
import hiiragi283.ragium.api.machine.HTMachineEnergyData
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTElectricFurnaceBlockEntity(pos: BlockPos, state: BlockState) :
    HTSimpleMachineBlockEntity(RagiumBlockEntityTypes.ELECTRIC_FURNACE, pos, state, HTMachineType.ELECTRIC_FURNACE) {
    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = when {
        HTHeatTier.getHeatTier(
            level,
            pos.below(),
            Direction.UP,
        ) >= HTHeatTier.MEDIUM -> HTMachineEnergyData.Consume.EMPTY
        else -> HTMachineEnergyData.Consume.DEFAULT
    }

    private var lastRecipe: RecipeHolder<out AbstractCookingRecipe>? = null

    override fun process(level: ServerLevel, pos: BlockPos) {
        val recipeType: RecipeType<out AbstractCookingRecipe> = when (catalystSlot.stack.item) {
            Items.FURNACE -> RecipeType.SMELTING
            Items.SMOKER -> RecipeType.SMOKING
            Items.BLAST_FURNACE -> RecipeType.BLASTING
            else -> throw HTMachineException.Custom(RagiumTranslationKeys.EXCEPTION_UNKNOWN_RECIPE_TYPE)
        }

        val input = SingleRecipeInput(inputSlot.stack)
        val recipe: AbstractCookingRecipe = level.recipeManager
            .getRecipeFor(recipeType, input, level, lastRecipe?.id)
            .map { holder: RecipeHolder<out AbstractCookingRecipe> ->
                this.lastRecipe = holder
                holder
            }.orElseThrow {
                this.lastRecipe = null
                HTMachineException.NoMatchingRecipe()
            }.value
        val output: ItemStack = recipe.assemble(input, level.registryAccess())

        if (!outputSlot.canInsert(output)) throw HTMachineException.GrowItem()
        if (!inputSlot.canExtract(1)) throw HTMachineException.ShrinkItem()

        outputSlot.insert(output, false)
        inputSlot.extract(1, false)
    }
}
