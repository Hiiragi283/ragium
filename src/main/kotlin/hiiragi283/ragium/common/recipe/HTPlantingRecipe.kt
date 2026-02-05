package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTViewRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.item.toResource
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class HTPlantingRecipe(
    val seed: HTItemHolderLike<*>,
    val soil: HTItemIngredient,
    val crop: HTItemResult,
    parameters: SubParameters,
) : HTProcessingRecipe<HTViewRecipeInput>(parameters) {
    companion object {
        const val FLUID_AMOUNT = 50
    }

    val seedIngredient: HTItemIngredient = Ingredient.of(seed).let(::HTItemIngredient)
    val seedResult = HTItemResult(Ior.Left(seed.toResource()!!), 1)

    fun getResultSeed(provider: HolderLookup.Provider): ItemStack = seedResult.getStackOrEmpty(provider)

    override fun matches(input: HTViewRecipeInput, level: Level): Boolean {
        val bool1: Boolean = seedIngredient.testOnlyType(input.getItemAt(0))
        val bool2: Boolean = soil.testOnlyType(input.getItemAt(1))
        val (resource: HTFluidResourceType, amount: Int) = input.getFluidAt(0)
        val bool3: Boolean = RagiumDataMapTypes.getFluidFertilizer(resource) != null && amount >= FLUID_AMOUNT
        return bool1 && bool2 && bool3
    }

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = crop.getStackOrEmpty(registries)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.PLANTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.PLANTING.get()
}
