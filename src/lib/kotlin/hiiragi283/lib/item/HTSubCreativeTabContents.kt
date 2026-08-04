package hiiragi283.lib.item

import net.minecraft.core.Holder
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

/**
 * クリエイティブタブに複数の[ItemStack]を追加するためのインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun interface HTSubCreativeTabContents {
    /**
     * 複数の[ItemStack]を追加します。
     * @param baseItem 対象のアイテム
     * @param parameters 登録時のコンテキスト
     * @param output [ItemStack]の登録先
     */
    fun addItems(baseItem: Holder<Item>, parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output)

    /**
     * デフォルトの[ItemStack]を追加するか判定します。
     */
    fun shouldAddDefault(): Boolean = true
}
