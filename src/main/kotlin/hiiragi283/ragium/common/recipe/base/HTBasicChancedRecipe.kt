package hiiragi283.ragium.common.recipe.base

import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.recipe.result.getStackOrNull
import hiiragi283.core.api.util.wrapOptional
import hiiragi283.ragium.api.recipe.HTChancedRecipe
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import java.util.Optional

abstract class HTBasicChancedRecipe<INPUT : RecipeInput>(
    val result: HTItemResult,
    val extraResult: Optional<HTChancedItemResult>,
    final override val time: Int,
) : HTChancedRecipe.Serializable<INPUT> {
    final override fun assembleExtraItem(input: INPUT, registries: HolderLookup.Provider, chance: Float): ItemStack =
        extraResult.flatMap { it.getStackOrNull(registries, chance).wrapOptional() }.orElse(ItemStack.EMPTY)

    final override fun assemble(input: INPUT, registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)
}
