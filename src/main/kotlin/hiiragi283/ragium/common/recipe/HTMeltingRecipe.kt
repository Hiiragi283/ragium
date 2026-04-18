package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.base.HTFluidRecipe
import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.fluids.FluidStack

class HTMeltingRecipe(val ingredient: HTItemIngredient, val result: HTFluidResult, override val time: Int) :
    HTProcessingRecipe.Serializable<SingleRecipeInput>,
    HTFluidRecipe<SingleRecipeInput> {
    override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())

    @Deprecated("Not used", level = DeprecationLevel.ERROR)
    override fun assemble(input: SingleRecipeInput, preview: Boolean): ItemStack = ItemStack.EMPTY

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MELTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MELTING.get()

    override fun assembleFluid(input: SingleRecipeInput): FluidStack = result.getOrEmpty()
}
