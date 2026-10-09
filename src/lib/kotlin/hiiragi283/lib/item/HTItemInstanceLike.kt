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
    fun asTemplate(count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): Result<ItemStackTemplate>

    /**
     * 新しい[ItemStack]のインスタンスを作成します。
     */
    fun asStack(count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): Result<ItemStack> =
        asTemplate(count, patch).map(ItemStackTemplate::create)

    /**
     * 新しい[ItemStack]のインスタンスを作成します。
     * @return [ItemStack]のインスタンスを作成できなかった場合は[ItemStack.EMPTY]
     */
    fun asStackOrEmpty(count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): ItemStack =
        asStack(count, patch).getOrElse { ItemStack.EMPTY }
}
