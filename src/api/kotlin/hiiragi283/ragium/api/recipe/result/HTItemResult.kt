package hiiragi283.ragium.api.recipe.result

import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack

/**
 * [ItemStack]向けの[HTRecipeResult]の実装
 */
interface HTItemResult : HTRecipeResult<ItemStack> {
    override fun getOrEmpty(provider: HolderLookup.Provider?): ItemStack = getStackResult(provider).result().orElse(ItemStack.EMPTY)
}
