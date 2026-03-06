package hiiragi283.ragium.api.recipe

import hiiragi283.core.api.data.recipe.HTIngredientCreator
import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.ragium.api.tag.RagiumTags
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack

interface HTDuplicatingRecipe : HTProcessingRecipe.Serializable<HTItemAndFluidRecipeInput> {
    override fun test(input: HTItemAndFluidRecipeInput): Boolean {
        val stack: ItemStack = input.item
        return testItem(stack) && createFluidIngredient(stack).test(input.fluid)
    }

    fun testItem(stack: ItemStack): Boolean

    fun createFluidIngredient(stack: ItemStack): HTFluidIngredient =
        HTIngredientCreator.create(RagiumTags.Fluids.RAGI_MATTER, getRequiredMatter(stack))

    fun getRequiredMatter(stack: ItemStack): Int

    override fun assemble(input: HTItemAndFluidRecipeInput, registries: HolderLookup.Provider): ItemStack = input.item.copyWithCount(1)
}
