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
    parameters: SubParameters,
) : HTProcessingRecipe<INPUT>(parameters) {
    abstract class ItemToItem(ingredient: HTItemIngredient, result: HTItemResult, parameters: SubParameters) :
        HTSingleProcessingRecipe<HTItemIngredient, HTItemResult, SingleRecipeInput>(ingredient, result, parameters) {
        override fun matches(input: SingleRecipeInput, level: Level): Boolean = ingredient.test(input.item())

        override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)
    }

    abstract class ItemToFluid(ingredient: HTItemIngredient, result: HTFluidResult, parameters: SubParameters) :
        HTSingleProcessingRecipe<HTItemIngredient, HTFluidResult, SingleRecipeInput>(ingredient, result, parameters),
        HTFluidRecipe.Simple {
        override fun matches(input: SingleRecipeInput, level: Level): Boolean = ingredient.test(input.item())

        override fun getResultItem(registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY
    }

    abstract class FluidToItem(ingredient: HTFluidIngredient, result: HTItemResult, parameters: SubParameters) :
        HTSingleProcessingRecipe<HTFluidIngredient, HTItemResult, HTSingleFluidRecipeInput>(ingredient, result, parameters) {
        override fun matches(input: HTSingleFluidRecipeInput, level: Level): Boolean = ingredient.test(input.fluid)

        override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)
    }

    abstract class FluidToFluid(ingredient: HTFluidIngredient, result: HTFluidResult, parameters: SubParameters) :
        HTSingleProcessingRecipe<HTFluidIngredient, HTFluidResult, HTSingleFluidRecipeInput>(ingredient, result, parameters),
        HTFluidRecipe.Simple {
        override fun matches(input: HTSingleFluidRecipeInput, level: Level): Boolean = ingredient.test(input.fluid)

        override fun getResultItem(registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY
    }
}
