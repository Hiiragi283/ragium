package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.recipe.fluid.HTRefiningRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.RecipeSerializer
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

class HTBasicRefiningRecipe(val ingredient: HTFluidIngredient, val itemResult: Optional<HTItemResult>, val fluidResult: HTFluidResult) :
    HTRefiningRecipe {
    override fun test(stack: ImmutableFluidStack): Boolean = ingredient.test(stack)

    override fun assembleFluid(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableFluidStack? =
        fluidResult.getStackOrNull(provider)

    override fun assembleItem(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? =
        itemResult.getOrNull()?.getStackOrNull(provider)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.REFINING

    override fun getRequiredAmount(): Int = ingredient.getRequiredAmount()
}
