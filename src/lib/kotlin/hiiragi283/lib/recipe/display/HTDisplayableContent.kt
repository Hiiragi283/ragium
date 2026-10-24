package hiiragi283.lib.recipe.display

import net.minecraft.world.item.crafting.display.SlotDisplay

/**
 * [SlotDisplay]に基づいてプレビューを提供可能なオブジェクトを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
interface HTDisplayableContent {
    /**
     * このオブジェクトのプレビューを返します。
     */
    fun display(): SlotDisplay
}
