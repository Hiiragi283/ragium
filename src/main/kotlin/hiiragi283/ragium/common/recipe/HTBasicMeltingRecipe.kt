package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.fluid.HTMeltingRecipe
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.RecipeSerializer

class HTBasicMeltingRecipe(val ingredient: HTItemIngredient, val result: HTFluidResult) : HTMeltingRecipe {
    override fun assembleFluid(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableFluidStack? =
        result.getStackOrNull(provider)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MELTING

    override fun test(stack: ImmutableItemStack): Boolean = ingredient.test(stack)

    override fun getRequiredCount(): Int = ingredient.getRequiredAmount()
}
