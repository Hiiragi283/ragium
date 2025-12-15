package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.world.level.Level
import java.util.function.Predicate

/**
 * 単一のアイテムから完成品を生産するレシピ
 */
interface HTAbstractSingleItemInputRecipe :
    HTAbstractRecipe,
    Predicate<ImmutableItemStack>,
    HTItemIngredient.CountGetter {
    override fun matches(input: HTRecipeInput, level: Level): Boolean = input.testItem(0, this::test)

    override fun test(stack: ImmutableItemStack): Boolean
}
