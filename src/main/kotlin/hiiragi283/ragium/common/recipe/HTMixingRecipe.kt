package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTComplexResult
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.ragium.common.recipe.base.HTComplexResultRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import org.apache.commons.lang3.math.Fraction

class HTMixingRecipe(
    val itemIngredients: List<HTItemIngredient>,
    val fluidIngredients: List<HTFluidIngredient>,
    result: HTComplexResult,
    time: Int,
    exp: Fraction,
) : HTComplexResultRecipe<HTMixingRecipe.Input>(result, time, exp) {
    override fun matches(input: Input, level: Level): Boolean {
        if (itemIngredients.isEmpty() && fluidIngredients.isEmpty()) return false
        val bool1: Boolean = itemIngredients.getOrNull(0)?.test(input.getItem(0)) ?: true
        val bool2: Boolean = itemIngredients.getOrNull(1)?.test(input.getItem(1)) ?: true
        val bool3: Boolean = fluidIngredients.getOrNull(0)?.test(input.getFluid(0)) ?: true
        val bool4: Boolean = fluidIngredients.getOrNull(1)?.test(input.getFluid(1)) ?: true
        return bool1 && bool2 && bool3 && bool4
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MIXING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MIXING.get()

    @JvmRecord
    data class Input(val items: Pair<ItemStack, ItemStack>, val fluids: Pair<FluidStack, FluidStack>) : HTFluidRecipeInput {
        override fun getFluid(index: Int): FluidStack = when (index) {
            0 -> fluids.first
            1 -> fluids.second
            else -> FluidStack.EMPTY
        }

        override fun getFluidSize(): Int = 2

        private fun validateItem(stack: ItemStack): ItemStack = when {
            stack.`is`(HiiragiCoreTags.Items.IGNORED_IN_RECIPE_INPUTS) -> ItemStack.EMPTY
            else -> stack
        }

        override fun getItem(index: Int): ItemStack = when (index) {
            0 -> items.first
            1 -> items.second
            else -> ItemStack.EMPTY
        }.let(::validateItem)

        override fun size(): Int = 2
    }
}
