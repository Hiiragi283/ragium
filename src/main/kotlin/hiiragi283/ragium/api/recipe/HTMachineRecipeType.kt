package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.machine.HTMachineKey
import net.minecraft.recipe.RecipeType

/**
 * [RecipeType]を拡張したクラス
 */
class HTMachineRecipeType<T : HTMachineRecipe>(val machineKey: HTMachineKey, val factory: HTMachineRecipe.Factory<T>) : RecipeType<T>
