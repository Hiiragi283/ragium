package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTItemWithFluidToFluidRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.neoforged.neoforge.fluids.FluidStack
import java.util.*

class HTMeltingRecipe(itemIngredient: HTItemIngredient, result: HTFluidResult) :
    HTItemWithFluidToFluidRecipe(
        RagiumRecipeTypes.MELTING.get(),
        Optional.of(itemIngredient),
        Optional.empty(),
        result,
    ) {
    constructor(
        itemIngredient: Optional<HTItemIngredient>,
        fluidIngredient: Optional<HTFluidIngredient>,
        result: HTFluidResult,
    ) : this(itemIngredient.orElseThrow(), result)

    override fun testItem(ingredient: HTItemIngredient, stack: ItemStack): Boolean = ingredient.test(stack)

    override fun testFluid(ingredient: HTFluidIngredient, stack: FluidStack): Boolean = true

    override fun isIncomplete(): Boolean = itemIngredient.map(HTItemIngredient::hasNoMatchingStacks).orElse(true)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MELTING.get()
}
