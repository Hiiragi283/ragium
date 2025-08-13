package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTItemWithFluidToObjRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import java.util.*

abstract class HTItemWithFluidToItemRecipe(
    override val itemIngredient: Optional<HTItemIngredient>,
    override val fluidIngredient: Optional<HTFluidIngredient>,
    override val result: HTItemResult,
) : HTItemWithFluidToObjRecipe<HTItemResult> {
    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.getOrEmpty()
}
