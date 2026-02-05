package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack

class HTMeltingRecipe(val ingredient: HTItemIngredient, val result: HTFluidResult, parameters: SubParameters) :
    HTProcessingRecipe<SingleRecipeInput>(parameters) {
    fun getResultFluid(provider: HolderLookup.Provider): FluidStack = result.getStackOrEmpty(provider)

    override fun matches(input: SingleRecipeInput, level: Level): Boolean = ingredient.test(input.item())

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MELTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MELTING.get()
}
