package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.data.recipe.HTIngredientCreator
import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.tag.HiiragiCoreTags
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack

class RagiumDuplicatingRecipe(val ingredient: HTItemIngredient, val requiredMatter: Int) : HTProcessingRecipe<HTItemAndFluidRecipeInput> {
    val defaultFluidIngredient: HTFluidIngredient by lazy {
        HTIngredientCreator.create(HiiragiCoreTags.Fluids.ELDRITCH, requiredMatter)
    }

    override fun test(input: HTItemAndFluidRecipeInput): Boolean {
        val bool1: Boolean = ingredient.testOnlyType(input.item)
        val bool2: Boolean = createFluidIngredient(input).test(input.fluid)
        return bool1 && bool2
    }

    private fun createFluidIngredient(input: HTItemAndFluidRecipeInput): HTFluidIngredient {
        val itemStack: ItemStack = input.item
        val extraAmount = 0
        // RagiumRegistries.DUPLICATION_MODIFIER
        // .filter { it.test(itemStack) }
        // .sumOf { it.calculateExtraAmount(itemStack) }
        return HTIngredientCreator.create(HiiragiCoreTags.Fluids.ELDRITCH, requiredMatter + extraAmount)
    }

    override fun assemble(input: HTItemAndFluidRecipeInput, registries: HolderLookup.Provider): ItemStack = input.item.copyWithCount(1)

    override val time: Int = 20 * 30
}
