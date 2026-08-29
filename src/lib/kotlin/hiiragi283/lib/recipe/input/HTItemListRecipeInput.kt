package hiiragi283.lib.recipe.input

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

/**
 * 複数のアイテムを保持する[RecipeInput]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmRecord
data class HTItemListRecipeInput(val items: List<ItemStack>) : RecipeInput {
    override fun getItem(index: Int): ItemStack = items[index]

    override fun size(): Int = items.size

    override fun isEmpty(): Boolean = items.isEmpty() || items.all(ItemStack::isEmpty)
}
