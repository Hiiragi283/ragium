package hiiragi283.ragium.api.storage.predicate

import hiiragi283.ragium.api.extension.partially1
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import java.util.function.Predicate

object HTItemPredicates {
    @JvmField
    val TRUE: Predicate<ItemStack> = Predicate { true }

    @JvmField
    val FALSE: Predicate<ItemStack> = TRUE.negate()

    @JvmField
    val EMPTY: Predicate<ItemStack> = Predicate(ItemStack::isEmpty)

    @JvmField
    val NOT_EMPTY: Predicate<ItemStack> = EMPTY.negate()

    @JvmStatic
    fun byItem(item: ItemLike): Predicate<ItemStack> = Predicate { stack: ItemStack -> stack.`is`(item.asItem()) }

    @JvmStatic
    fun byItem(other: ItemStack): Predicate<ItemStack> = Predicate(ItemStack::isSameItem.partially1(other))

    @JvmStatic
    fun byItemAndComponent(other: ItemStack): Predicate<ItemStack> = Predicate(ItemStack::isSameItemSameComponents.partially1(other))

    @JvmStatic
    fun byTag(tagKey: TagKey<Item>): Predicate<ItemStack> = Predicate { stack: ItemStack -> stack.`is`(tagKey) }

    @JvmStatic
    fun byHolder(holder: Holder<Item>): Predicate<ItemStack> = Predicate { stack: ItemStack -> stack.`is`(holder) }

    @JvmStatic
    fun byHolderSet(holderSet: HolderSet<Item>): Predicate<ItemStack> = Predicate { stack: ItemStack -> stack.`is`(holderSet) }
}
