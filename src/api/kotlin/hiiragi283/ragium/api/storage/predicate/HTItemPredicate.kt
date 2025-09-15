package hiiragi283.ragium.api.storage.predicate

import hiiragi283.ragium.api.extension.negate
import hiiragi283.ragium.api.extension.partially1
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import java.util.function.Predicate

fun interface HTItemPredicate : Predicate<ItemStack> {
    companion object {
        @JvmField
        val TRUE = HTItemPredicate { true }

        @JvmField
        val FALSE = HTItemPredicate { false }

        @JvmField
        val EMPTY = HTItemPredicate(ItemStack::isEmpty)

        @JvmField
        val NOT_EMPTY = HTItemPredicate(ItemStack::isEmpty.negate())

        @JvmStatic
        fun byItem(item: ItemLike): HTItemPredicate = HTItemPredicate { stack: ItemStack -> stack.`is`(item.asItem()) }

        @JvmStatic
        fun byItem(other: ItemStack): HTItemPredicate = ItemStack::isSameItem.partially1(other).let(::HTItemPredicate)

        @JvmStatic
        fun byItemAndComponent(other: ItemStack): HTItemPredicate =
            ItemStack::isSameItemSameComponents.partially1(other).let(::HTItemPredicate)

        @JvmStatic
        fun byTag(tagKey: TagKey<Item>): HTItemPredicate = HTItemPredicate { stack: ItemStack -> stack.`is`(tagKey) }

        @JvmStatic
        fun byHolder(holder: Holder<Item>): HTItemPredicate = HTItemPredicate { stack: ItemStack -> stack.`is`(holder) }

        @JvmStatic
        fun byHolderSet(holderSet: HolderSet<Item>): HTItemPredicate = HTItemPredicate { stack: ItemStack -> stack.`is`(holderSet) }
    }
}
