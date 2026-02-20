package hiiragi283.ragium.common.recipe.base

import hiiragi283.core.api.recipe.HTFluidRecipe
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.recipe.result.HTRecipeResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level

abstract class HTSingleProcessingRecipe<INGREDIENT : HTIngredient<*, *>, RESULT : HTRecipeResult<*>, INPUT : RecipeInput>(
    val ingredient: INGREDIENT,
    val result: RESULT,
    final override val time: Int,
) : HTProcessingRecipe<INPUT> {
    abstract class ItemToItem(ingredient: HTItemIngredient, result: HTItemResult, time: Int) :
        HTSingleProcessingRecipe<HTItemIngredient, HTItemResult, SingleRecipeInput>(ingredient, result, time) {
        override fun matches(input: SingleRecipeInput, level: Level): Boolean = ingredient.test(input.item())

        override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)
    }

    abstract class ItemToFluid(ingredient: HTItemIngredient, result: HTFluidResult, time: Int) :
        HTSingleProcessingRecipe<HTItemIngredient, HTFluidResult, SingleRecipeInput>(ingredient, result, time),
        HTFluidRecipe.Simple {
        override fun matches(input: SingleRecipeInput, level: Level): Boolean = ingredient.test(input.item())

        override fun getResultItem(registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY
    }

    abstract class FluidToItem(ingredient: HTFluidIngredient, result: HTItemResult, time: Int) :
        HTSingleProcessingRecipe<HTFluidIngredient, HTItemResult, HTSingleFluidRecipeInput>(ingredient, result, time) {
        override fun matches(input: HTSingleFluidRecipeInput, level: Level): Boolean = ingredient.test(input.fluid)

        override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)
    }

    abstract class FluidToFluid(ingredient: HTFluidIngredient, result: HTFluidResult, time: Int) :
        HTSingleProcessingRecipe<HTFluidIngredient, HTFluidResult, HTSingleFluidRecipeInput>(ingredient, result, time),
        HTFluidRecipe.Simple {
        override fun matches(input: HTSingleFluidRecipeInput, level: Level): Boolean = ingredient.test(input.fluid)

        override fun getResultItem(registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY
    }
}
