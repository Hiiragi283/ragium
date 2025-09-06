package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTItemToObjRecipe
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

interface HTItemToItemRecipe : HTItemToObjRecipe<HTItemResult> {
    override fun assemble(input: SingleRecipeInput, registries: HolderLookup.Provider): ItemStack = getItemResult(input, registries, result)
}
