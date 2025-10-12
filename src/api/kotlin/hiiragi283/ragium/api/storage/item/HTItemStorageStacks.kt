package hiiragi283.ragium.api.storage.item

import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.RecipeType

fun HTItemStorageStack.isOf(item: Item): Boolean = this.stack.`is`(item)

fun HTItemStorageStack.isOf(tagKey: TagKey<Item>): Boolean = this.stack.`is`(tagKey)

fun HTItemStorageStack.isOf(holder: Holder<Item>): Boolean = this.stack.`is`(holder)

fun HTItemStorageStack.isOf(holderSet: HolderSet<Item>): Boolean = this.stack.`is`(holderSet)

fun HTItemStorageStack.maxStackSize(): Int = stack.maxStackSize

fun HTItemStorageStack.hasCraftingRemainingItem(): Boolean = stack.hasCraftingRemainingItem()

fun HTItemStorageStack.getCraftingRemainingItem(): HTItemStorageStack = HTItemStorageStack.of(stack.craftingRemainingItem)

fun HTItemStorageStack.getBurnTime(recipeType: RecipeType<*>?): Int = this.stack.getBurnTime(recipeType)
