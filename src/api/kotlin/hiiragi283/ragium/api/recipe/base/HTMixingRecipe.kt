package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.neoforged.neoforge.fluids.FluidStack
import java.util.*

class HTMixingRecipe(itemIngredient: Optional<HTItemIngredient>, fluidIngredient: Optional<HTFluidIngredient>, result: HTFluidResult) :
    HTItemWithFluidToFluidRecipe(
        RagiumRecipeTypes.MIXING.get(),
        itemIngredient,
        fluidIngredient,
        result,
    ) {
    override fun testItem(ingredient: HTItemIngredient, stack: ItemStack): Boolean = ingredient.test(stack)

    override fun testFluid(ingredient: HTFluidIngredient, stack: FluidStack): Boolean = ingredient.test(stack)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MIXING.get()
}
