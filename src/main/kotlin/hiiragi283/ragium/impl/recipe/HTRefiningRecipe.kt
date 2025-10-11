package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.HTFluidTransformRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTItemWithFluidToChancedItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.recipe.result.HTChancedItemResult
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional

class HTRefiningRecipe(
    override val fluidIngredient: HTFluidIngredient,
    override val itemIngredient: Optional<HTItemIngredient>,
    override val itemResult: Optional<HTItemResult>,
    override val fluidResult: Optional<HTFluidResult>,
) : HTFluidTransformRecipe,
    HTItemWithFluidToChancedItemRecipe {
    override fun getRequiredCount(stack: ItemStack): Int = itemIngredient.map { it.getRequiredAmount(stack) }.orElse(0)

    override fun getRequiredAmount(stack: FluidStack): Int = fluidIngredient.getRequiredAmount(stack)

    override fun getResultItems(input: HTItemWithFluidRecipeInput): List<HTChancedItemResult> =
        itemResult.map(::HTChancedItemResult).stream().toList()

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.FLUID_TRANSFORM

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.FLUID_TRANSFORM.get()
}
