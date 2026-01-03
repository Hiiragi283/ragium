package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.toResourcePair
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.crafting.DataComponentIngredient
import net.neoforged.neoforge.fluids.FluidStack
import org.apache.commons.lang3.math.Fraction

class HTPlantingRecipe(
    val seed: HTItemResourceType,
    val soil: HTItemIngredient,
    val crop: HTItemResult,
    time: Int,
    exp: Fraction,
) : HTProcessingRecipe<HTPlantingRecipe.Input>(time, exp) {
    companion object {
        const val FLUID_AMOUNT = 50
    }

    val seedIngredient: HTItemIngredient = when {
        seed.components.isEmpty -> Ingredient.of(seed.type())
        else -> DataComponentIngredient.of(false, seed.toStack())
    }.let(::HTItemIngredient)
    val seedResult = HTItemResult(Ior.Left(seed), 1)

    fun getResultSeed(provider: HolderLookup.Provider): ItemStack = seedResult.getStackOrEmpty(provider)

    override fun matches(input: Input, level: Level): Boolean {
        val bool1: Boolean = seedIngredient.testOnlyType(input.seed)
        val bool2: Boolean = soil.testOnlyType(input.soil)
        val (resource: HTFluidResourceType, amount: Int) = input.fluid.toResourcePair() ?: return false
        val bool3: Boolean = RagiumDataMapTypes.getFluidFertilizer(resource) != null && amount >= FLUID_AMOUNT
        return bool1 && bool2 && bool3
    }

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = crop.getStackOrEmpty(registries)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.PLANTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.PLANTING.get()

    @JvmRecord
    data class Input(val seed: ItemStack, val soil: ItemStack, val fluid: FluidStack) : HTFluidRecipeInput {
        override fun getFluid(index: Int): FluidStack = fluid

        override fun getFluidSize(): Int = 1

        override fun getItem(index: Int): ItemStack = when (index) {
            0 -> seed
            else -> soil
        }

        override fun size(): Int = 2
    }
}
