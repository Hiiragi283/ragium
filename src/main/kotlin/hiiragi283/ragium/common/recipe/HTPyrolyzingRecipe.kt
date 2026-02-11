package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

class HTPyrolyzingRecipe(
    val itemIngredient: HTItemIngredient,
    val fluidIngredient: HTFluidIngredient?,
    val itemResult: HTItemResult,
    val fluidResult: HTFluidResult,
    parameters: SubParameters,
) : HTProcessingRecipe<HTItemAndFluidRecipeInput>(parameters) {
    constructor(
        itemIngredient: HTItemIngredient,
        fluidIngredient: Optional<HTFluidIngredient>,
        itemResult: HTItemResult,
        fluidResult: HTFluidResult,
        parameters: SubParameters,
    ) : this(itemIngredient, fluidIngredient.getOrNull(), itemResult, fluidResult, parameters)

    fun getResultFluid(provider: HolderLookup.Provider): FluidStack = fluidResult.getStackOrEmpty(provider)

    override fun matches(input: HTItemAndFluidRecipeInput, level: Level): Boolean {
        val bool1: Boolean = itemIngredient.test(input.item)
        val bool2: Boolean = fluidIngredient?.test(input.fluid) ?: true
        return bool1 && bool2
    }

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = itemResult.getStackOrEmpty(registries)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.PYROLYZING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.PYROLYZING.get()
}
