package hiiragi283.ragium.api.recipe

import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.ragium.api.tag.RagiumTags
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

interface HTDuplicatingRecipe : HTProcessingRecipe.Serializable<HTItemAndFluidRecipeInput> {
    override fun test(input: HTItemAndFluidRecipeInput): Boolean = testItem(input.item) && testFluid(input)

    fun testItem(stack: ItemStack): Boolean

    fun testFluid(input: HTItemAndFluidRecipeInput): Boolean {
        val stack: FluidStack = input.fluid
        return stack.`is`(RagiumTags.Fluids.RAGI_MATTER) && stack.amount >= getRequiredMatter(input.item)
    }

    fun getRequiredMatter(stack: ItemStack): Int

    override fun assemble(input: HTItemAndFluidRecipeInput, registries: HolderLookup.Provider): ItemStack = input.item.copyWithCount(1)
}
