package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.ragium.common.recipe.base.HTChemicalResult
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTDistillingRecipe(val ingredient: HTFluidIngredient, val results: HTChemicalResult, override val time: Int) :
    HTProcessingRecipe.Serializable<HTSingleFluidRecipeInput> {
    companion object {
        const val MAX_FLUID_OUTPUT = 2
        const val MAX_ITEM_OUTPUT = 1
    }

    override fun assemble(input: HTSingleFluidRecipeInput, registries: HolderLookup.Provider): ItemStack = results
        .getLeft()
        ?.firstOrNull()
        ?.getStackResult(registries)
        ?.value() ?: ItemStack.EMPTY

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.DISTILLING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.DISTILLING.get()

    override fun test(input: HTSingleFluidRecipeInput): Boolean = ingredient.test(input.fluid)
}
