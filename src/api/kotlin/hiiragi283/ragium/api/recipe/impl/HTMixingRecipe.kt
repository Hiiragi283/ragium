package hiiragi283.ragium.api.recipe.impl

import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTItemWithFluidToFluidRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidStack
import java.util.*

class HTMixingRecipe(itemIngredient: Optional<HTItemIngredient>, fluidIngredient: Optional<HTFluidIngredient>, result: HTFluidResult) :
    HTItemWithFluidToFluidRecipe(
        itemIngredient,
        fluidIngredient,
        result,
    ) {
    override fun testItem(ingredient: HTItemIngredient, stack: ItemStack): Boolean = ingredient.test(stack)

    override fun testFluid(ingredient: HTFluidIngredient, stack: FluidStack): Boolean = ingredient.test(stack)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MIXING.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MIXING.get()
}
