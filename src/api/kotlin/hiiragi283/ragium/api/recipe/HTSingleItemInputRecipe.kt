package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import net.minecraft.world.level.Level

/**
 * 単一のアイテムから完成品を生産するレシピ
 */
interface HTSingleItemInputRecipe :
    HTRecipe,
    HTAbstractSingleItemInputRecipe {
    override fun matches(input: HTRecipeInput, level: Level): Boolean = super.matches(input, level)
}
