package hiiragi283.lib.recipe.input

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

/**
 * 一種類のアイテムと触媒を保持する[RecipeInput]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
@JvmRecord
data class HTItemWithCatalystRecipeInput(val item: ItemStack, val catalyst: ItemStack) : RecipeInput {
    override fun getItem(index: Int): ItemStack = when (index) {
        0 -> item
        else -> error("No item for index: $index")
    }

    override fun size(): Int = 1
}
