package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.input.HTChemicalRecipeInput
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import org.apache.commons.lang3.math.Fraction
import java.util.Optional

class HTMixingRecipe(
    val itemIngredient: Optional<HTItemIngredient>,
    val fluidIngredients: List<HTFluidIngredient>,
    val result: HTFluidResult,
    time: Int,
    exp: Fraction,
) : HTProcessingRecipe<HTChemicalRecipeInput>(time, exp) {
    fun getResultFluid(provider: HolderLookup.Provider): FluidStack = result.getStackOrEmpty(provider)

    override fun matches(input: HTChemicalRecipeInput, level: Level): Boolean {
        if (fluidIngredients.isEmpty()) return false
        val item: ItemStack = input.getItem(0)
        val bool1: Boolean = itemIngredient.map { it.test(item) }.orElseGet(item::isEmpty)
        val bool2: Boolean = fluidIngredients.getOrNull(0)?.test(input.getFluid(0)) ?: true
        val bool3: Boolean = fluidIngredients.getOrNull(1)?.test(input.getFluid(1)) ?: true
        return bool1 && bool2 && bool3
    }

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MIXING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MIXING.get()
}
