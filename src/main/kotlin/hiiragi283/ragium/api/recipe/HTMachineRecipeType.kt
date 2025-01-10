package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.machine.HTMachineKey
import net.minecraft.recipe.RecipeType

class HTMachineRecipeType<T : HTMachineRecipe>(val machineKey: HTMachineKey, val factory: HTMachineRecipe.Factory<T>) : RecipeType<T>
