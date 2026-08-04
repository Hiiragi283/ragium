package hiiragi283.lib.item

import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.level.ItemLike

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.4
 */
typealias HTSimpleItemLike = HTItemLike<Item>

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.4
 */
interface HTItemLike<out ITEM : Item> : ItemLike {
    override fun asItem(): ITEM

    /**
     * 新しい[ItemStackTemplate]のインスタンスを作成します。
     */
    fun toTemplate(count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): ItemStackTemplate

    /**
     * 新しい[ItemStack]のインスタンスを作成します。
     */
    fun toStack(count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): ItemStack
}
