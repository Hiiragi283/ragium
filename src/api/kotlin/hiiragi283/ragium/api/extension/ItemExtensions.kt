package hiiragi283.ragium.api.extension

import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.MutableDataComponentHolder

//    ItemStack    //

inline fun createItemStack(item: ItemLike, count: Int = 1, builderAction: MutableDataComponentHolder.() -> Unit): ItemStack =
    ItemStack(item, count).apply(builderAction)

fun createPotionStack(potion: Holder<Potion>, count: Int = 1, item: ItemLike = Items.POTION): ItemStack =
    createPotionStack(PotionContents(potion.delegate), count, item)

fun createPotionStack(content: PotionContents, count: Int = 1, item: ItemLike = Items.POTION): ItemStack = createItemStack(item, count) {
    set(DataComponents.POTION_CONTENTS, content)
}

/**
 * 残りの耐久値を返します。
 */
val ItemStack.restDamage: Int get() = maxDamage - damageValue

/**
 * 現在の個数が[isMaxCount]と一致するか判定します。
 */
val ItemStack.isMaxCount: Boolean get() = count == maxStackSize

// val ItemStack.isIgnored: Boolean get() = this.`is`(RagiumModTags.Items.IGNORED_IN_RECIPES)

// val ItemStack.isEmptyOrIgnored: Boolean get() = this.isEmpty || this.isIgnored
