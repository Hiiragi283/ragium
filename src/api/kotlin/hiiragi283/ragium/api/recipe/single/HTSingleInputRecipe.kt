package hiiragi283.ragium.api.recipe.single

import hiiragi283.ragium.api.recipe.HTRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import net.minecraft.world.item.crafting.SingleRecipeInput

/**
 * 単一のアイテムから単一のアイテムを生成するレシピ
 */
interface HTSingleInputRecipe :
    HTRecipe<SingleRecipeInput>,
    HTItemIngredient.CountGetter
