package hiiragi283.ragium.impl.recipe

import hiiragi283.core.api.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.impl.recipe.HTSerializableRecipe
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

abstract class HTBasicAssemblingRecipe(
    val result: HTItemResult,
    final override val progressData: HTProgressData,
) : HTDoubleItemToItemRecipe,
    HTProgressRecipe.Simple<RecipeInput>,
    HTSerializableRecipe<RecipeInput> {
    final override fun assemble(firstInput: ItemStack, secondInput: ItemStack): ItemStack = result.getOrEmpty()
}
