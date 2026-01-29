package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTComplexResult
import hiiragi283.ragium.api.recipe.input.HTChemicalRecipeInput
import hiiragi283.ragium.common.recipe.base.HTComplexResultRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import org.apache.commons.lang3.math.Fraction
import java.util.*

class HTMixingRecipe(
    val itemIngredient: Optional<HTItemIngredient>,
    val fluidIngredients: List<HTFluidIngredient>,
    result: HTComplexResult,
    time: Int,
    exp: Fraction,
) : HTComplexResultRecipe<HTChemicalRecipeInput>(result, time, exp) {
    override fun matches(input: HTChemicalRecipeInput, level: Level): Boolean {
        if (fluidIngredients.isEmpty()) return false
        val item: ItemStack = input.getItem(0)
        val bool1: Boolean = itemIngredient.map { it.test(item) }.orElseGet(item::isEmpty)
        val bool2: Boolean = fluidIngredients.getOrNull(0)?.test(input.getFluid(0)) ?: true
        val bool3: Boolean = fluidIngredients.getOrNull(1)?.test(input.getFluid(1)) ?: true
        return bool1 && bool2 && bool3
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MIXING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MIXING.get()
}
