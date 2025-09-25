package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.HTChancedItemRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTItemWithFluidToChancedItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.impl.recipe.base.HTChancedItemRecipeBase
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidStack

class HTWashingRecipe(
    val ingredient: HTItemIngredient,
    val fluidIngredient: HTFluidIngredient,
    override val results: List<HTChancedItemRecipe.ChancedResult>,
) : HTChancedItemRecipeBase<HTItemWithFluidRecipeInput>(),
    HTItemWithFluidToChancedItemRecipe {
    override fun isIncompleteIngredient(): Boolean = ingredient.hasNoMatchingStacks() || fluidIngredient.hasNoMatchingStacks()

    override fun getRequiredCount(stack: ItemStack): Int = ingredient.getRequiredAmount(stack)

    override fun getRequiredAmount(stack: FluidStack): Int = fluidIngredient.getRequiredAmount(stack)

    override fun test(input: HTItemWithFluidRecipeInput): Boolean = ingredient.test(input.item) && fluidIngredient.test(input.fluid)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.WASHING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.WASHING.get()
}
