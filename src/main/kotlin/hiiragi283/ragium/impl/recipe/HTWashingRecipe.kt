package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.HTChancedItemRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTChancedItemRecipeBase
import hiiragi283.ragium.api.recipe.base.HTItemWithFluidToChancedItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTWashingRecipe(
    val ingredient: HTItemIngredient,
    val fluidIngredient: HTFluidIngredient,
    override val results: List<HTChancedItemRecipe.ChancedResult>,
) : HTChancedItemRecipeBase<HTItemWithFluidRecipeInput>(),
    HTItemWithFluidToChancedItemRecipe {
    override fun isIncompleteIngredient(): Boolean = ingredient.hasNoMatchingStacks() || fluidIngredient.hasNoMatchingStacks()

    override fun getIngredientCount(input: HTItemWithFluidRecipeInput): Int = ingredient.getRequiredAmount(input.item)

    override fun getIngredientAmount(input: HTItemWithFluidRecipeInput): Int = fluidIngredient.getRequiredAmount(input.fluid)

    override fun test(input: HTItemWithFluidRecipeInput): Boolean = ingredient.test(input.item) && fluidIngredient.test(input.fluid)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.WASHING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.WASHING.get()
}
