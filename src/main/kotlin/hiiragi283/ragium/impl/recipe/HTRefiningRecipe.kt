package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.HTChancedItemRecipe
import hiiragi283.ragium.api.recipe.HTFluidTransformRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTItemWithFluidToChancedItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import java.util.Optional

class HTRefiningRecipe(
    override val fluidIngredient: HTFluidIngredient,
    override val itemIngredient: Optional<HTItemIngredient>,
    override val itemResult: Optional<HTItemResult>,
    override val fluidResult: Optional<HTFluidResult>,
) : HTFluidTransformRecipe,
    HTItemWithFluidToChancedItemRecipe {
    override fun getIngredientCount(input: HTItemWithFluidRecipeInput): Int =
        itemIngredient.map { it.getRequiredAmount(input.item) }.orElse(0)

    override fun getIngredientAmount(input: HTItemWithFluidRecipeInput): Int = fluidIngredient.getRequiredAmount(input.fluid)

    override fun getResultItems(input: HTItemWithFluidRecipeInput): List<HTChancedItemRecipe.ChancedResult> =
        itemResult.map { HTChancedItemRecipe.ChancedResult(it, 1f) }.map(::listOf).orElse(listOf())

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.FLUID_TRANSFORM

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.FLUID_TRANSFORM.get()
}
