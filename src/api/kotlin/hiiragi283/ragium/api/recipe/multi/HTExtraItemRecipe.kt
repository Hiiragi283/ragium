package hiiragi283.ragium.api.recipe.multi

import hiiragi283.ragium.api.recipe.HTRecipe
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.RecipeInput

/**
 * 主産物と副産物を生産するレシピ
 */
interface HTExtraItemRecipe<INPUT : RecipeInput> : HTRecipe<INPUT> {
    fun assembleExtraItem(input: INPUT, provider: HolderLookup.Provider): ImmutableItemStack?
}
