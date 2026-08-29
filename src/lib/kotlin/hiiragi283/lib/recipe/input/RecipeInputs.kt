package hiiragi283.lib.recipe.input

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

//    RecipeInput    //

/**
 * 有効なアイテムのインデックスの範囲
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
val RecipeInput.indices: IntRange get() = (0..<this.size())

/**
 * アイテムを取得します。
 * @param index 取得するアイテムのインデックス
 * @return [index]が[範囲][indices]に含まれていない場合`null`
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun RecipeInput.getItemOrNull(index: Int): ItemStack? = if (index in indices) getItem(index) else null

/**
 * アイテムを取得します。
 * @param index 取得するアイテムのインデックス
 * @return [index]が[範囲][indices]に含まれていない場合[ItemStack.EMPTY]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun RecipeInput.getItemOrEmpty(index: Int): ItemStack = this.getItemOrNull(index) ?: ItemStack.EMPTY
