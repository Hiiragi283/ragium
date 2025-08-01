package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.neoforged.neoforge.fluids.FluidStack
import java.util.*

class HTInfusingRecipe(itemIngredient: Optional<HTItemIngredient>, fluidIngredient: Optional<HTFluidIngredient>, result: HTItemResult) :
    HTItemWithFluidToItemRecipe(
        RagiumRecipeTypes.INFUSING.get(),
        itemIngredient,
        fluidIngredient,
        result,
    ) {
    override fun testItem(ingredient: HTItemIngredient, stack: ItemStack): Boolean = ingredient.test(stack)

    override fun testFluid(ingredient: HTFluidIngredient, stack: FluidStack): Boolean = ingredient.test(stack)

    override fun isIncomplete(): Boolean {
        val bool1: Boolean = itemIngredient.map(HTItemIngredient::hasNoMatchingStacks).orElse(true)
        val bool2: Boolean = fluidIngredient.map(HTFluidIngredient::hasNoMatchingStacks).orElse(true)
        val bool3: Boolean = itemIngredient.isEmpty && fluidIngredient.isEmpty
        return bool1 || bool2 || bool3
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.INFUSING.get()
}
