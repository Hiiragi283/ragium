package hiiragi283.lib.recipe.base

import hiiragi283.lib.recipe.input.getItemOrEmpty
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

/**
 * 2種類のアイテムから1種類のアイテムを作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTDoubleItemToItemRecipe : HTRecipeFactories.DoubleInput<RecipeInput, ItemInstance, ItemInstance, ItemStack> {
    override fun matches(input: RecipeInput): Boolean = test(input.getItemOrEmpty(0), input.getItemOrEmpty(1))

    override fun produce(input: RecipeInput): ItemStack = apply(input.getItemOrEmpty(0), input.getItemOrEmpty(1))

    override fun getProgressData(input: RecipeInput): HTProgressData = getProgressData(input.getItemOrEmpty(0), input.getItemOrEmpty(1))
}
