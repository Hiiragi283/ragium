package hiiragi283.ragium.api.storage.item

import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.RecipeType

fun ImmutableItemStack.isOf(item: Item): Boolean = this.stack.`is`(item)

fun ImmutableItemStack.isOf(tagKey: TagKey<Item>): Boolean = this.stack.`is`(tagKey)

fun ImmutableItemStack.isOf(holder: Holder<Item>): Boolean = this.stack.`is`(holder)

fun ImmutableItemStack.isOf(holderSet: HolderSet<Item>): Boolean = this.stack.`is`(holderSet)

fun ImmutableItemStack.maxStackSize(): Int = stack.maxStackSize

fun ImmutableItemStack.hasCraftingRemainingItem(): Boolean = stack.hasCraftingRemainingItem()

fun ImmutableItemStack.getCraftingRemainingItem(): ImmutableItemStack = ImmutableItemStack.of(stack.craftingRemainingItem)

fun ImmutableItemStack.getBurnTime(recipeType: RecipeType<*>?): Int = this.stack.getBurnTime(recipeType)
