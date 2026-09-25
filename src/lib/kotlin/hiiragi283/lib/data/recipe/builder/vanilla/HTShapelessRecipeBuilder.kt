@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe.builder.vanilla

import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.recipe.ingredient.IngredientBuilder
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.core.NonNullList
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.ShapelessRecipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * 不定形レシピ向けの[HTCraftingRecipeBuilder]の実装クラスです。
 *
 * 参照 : [Minecraft - ShapelessRecipeBuilder][net.minecraft.data.recipes.ShapelessRecipeBuilder]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTShapelessRecipeBuilder : HTCraftingRecipeBuilder<ShapelessRecipe>(HTConstants.SHAPELESS) {
    @PublishedApi internal val ingredients: MutableList<Ingredient> = ObjectArrayList()

    operator fun Ingredient.unaryPlus() {
        ingredients += this
    }

    inline fun ingredient(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +IngredientBuilder.build(builderAction)
    }

    override fun createRecipe(): ShapelessRecipe = ShapelessRecipe(
        commonInfo(true),
        bookInfo(),
        result,
        NonNullList.copyOf(ingredients)
    )
}
