package hiiragi283.ragium.api.recipe

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

interface HTItemToChancedItemRecipe : HTRecipe<SingleRecipeInput> {
    fun getIngredientCount(input: SingleRecipeInput): Int

    fun getResultItems(input: SingleRecipeInput): List<Pair<ItemStack, Float>>

    fun getPreviewItems(input: SingleRecipeInput): List<ItemStack> = getResultItems(input).map(Pair<ItemStack, Float>::first)
}
