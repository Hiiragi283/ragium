package hiiragi283.ragium.common.recipe.vanilla

import hiiragi283.ragium.api.recipe.HTRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTShapelessInputsRecipe
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.CraftingRecipe
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.Level

class HTAutoCraftingRecipe(override val ingredients: List<HTItemIngredient>, private val factory: HTVanillaResultFactory) :
    HTShapelessInputsRecipe,
    HTRecipe.Fake {
    companion object {
        /**
         * @see net.minecraft.world.level.block.entity.CrafterBlockEntity
         */
        @JvmStatic
        fun fromCrafting(recipe: CraftingRecipe): HTAutoCraftingRecipe = HTAutoCraftingRecipe(
            buildMap {
                for (ingredient: Ingredient in recipe.ingredients) {
                    this[ingredient] = this.getOrDefault(ingredient, 0) + 1
                }
            }.map { (ingredient: Ingredient, count: Int) -> HTItemIngredient(ingredient, count) },
        ) { _: HTRecipeInput, provider: HolderLookup.Provider -> recipe.getResultItem(provider) }
    }

    override fun assembleItem(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? =
        factory.assemble(input, provider).toImmutable()

    override fun matches(input: HTRecipeInput, level: Level): Boolean = HTRecipeInput.hasMatchingSlots(ingredients, input.items)
}
