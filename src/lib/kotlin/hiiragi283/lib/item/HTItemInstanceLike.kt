package hiiragi283.lib.item

import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate

/**
 * [ItemStackTemplate]や[ItemStack]に変換可能なオブジェクトを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTItemInstanceLike {
    /**
     * 新しい[ItemStackTemplate]のインスタンスを作成します。
     */
    fun toTemplate(count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): ItemStackTemplate?

    /**
     * 新しい[ItemStack]のインスタンスを作成します。
     */
    fun toStack(count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): ItemStack
}
