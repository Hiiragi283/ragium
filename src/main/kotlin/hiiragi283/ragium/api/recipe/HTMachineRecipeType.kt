package hiiragi283.ragium.api.recipe

import net.minecraft.recipe.RecipeType

/**
 * [RecipeType]を拡張したクラス
 */
class HTMachineRecipeType<T : HTMachineRecipe>(val factory: HTMachineRecipe.Factory<T>) : RecipeType<T>
