package hiiragi283.ragium.api.machine.property

import hiiragi283.ragium.api.recipe.HTMachineRecipeBase
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.Level
import java.util.function.Consumer

/**
 * [HTMachineRecipeBase]を取得するインターフェース
 */
fun interface HTMachineRecipeProxy {
    fun getRecipes(level: Level, consumer: Consumer<RecipeHolder<out HTMachineRecipeBase>>)
}
