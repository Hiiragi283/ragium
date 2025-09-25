package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import net.minecraft.world.item.crafting.SingleRecipeInput

/**
 * 単一の材料を受け付けるレシピの拡張インターフェース
 */
interface HTSingleInputRecipe :
    HTRecipe<SingleRecipeInput>,
    HTItemIngredient.CountGetter
