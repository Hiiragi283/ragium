package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTFluidWithCatalystToObjRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import java.util.*

abstract class HTFluidWithCatalystToItemRecipe(
    override val ingredient: HTFluidIngredient,
    override val catalyst: Optional<HTItemIngredient>,
    override val result: HTItemResult,
) : HTFluidWithCatalystToObjRecipe<HTItemResult> {
    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.getOrEmpty(registries)
}
