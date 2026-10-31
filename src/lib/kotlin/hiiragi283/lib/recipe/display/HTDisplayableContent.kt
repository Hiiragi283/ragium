package hiiragi283.lib.recipe.display

import hiiragi283.lib.util.toOptional
import net.minecraft.world.item.crafting.display.SlotDisplay
import java.util.Optional

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

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
fun HTDisplayableContent?.displayOrEmpty(): Optional<SlotDisplay> = this?.display().toOptional()

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
fun Optional<out HTDisplayableContent>.display(): Optional<SlotDisplay> = this.map(HTDisplayableContent::display)
