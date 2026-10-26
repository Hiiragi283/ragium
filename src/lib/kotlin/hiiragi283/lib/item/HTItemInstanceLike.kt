package hiiragi283.lib.item

import com.mojang.serialization.DataResult
import hiiragi283.lib.util.getOrElse
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.neoforged.neoforge.transfer.item.ItemResource

/**
 * [ItemStackTemplate]や[ItemStack]に変換可能なオブジェクトを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTItemInstanceLike {
    /**
     * 新しい[ItemStackTemplate]のインスタンスを作成します。
     */
    fun asTemplate(count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): DataResult<ItemStackTemplate>

    /**
     * 新しい[ItemStack]のインスタンスを作成します。
     */
    fun asStack(count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): DataResult<ItemStack> =
        asTemplate(count, patch).map(ItemStackTemplate::create)

    /**
     * 新しい[ItemStack]のインスタンスを作成します。
     * @return [ItemStack]のインスタンスを作成できなかった場合は[ItemStack.EMPTY]
     */
    fun asStackOrEmpty(count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): ItemStack =
        asStack(count, patch).getOrElse { ItemStack.EMPTY }

    /**
     * @since 26.1.12
     */
    fun asResource(patch: DataComponentPatch = DataComponentPatch.EMPTY): DataResult<ItemResource>

    /**
     * @since 26.1.12
     */
    fun asResourceOrEmpty(patch: DataComponentPatch = DataComponentPatch.EMPTY): ItemResource = asResource(patch).getOrElse { ItemResource.EMPTY }
}
