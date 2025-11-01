package hiiragi283.ragium.api.stack

import net.minecraft.world.item.ItemStack

fun ItemStack.toImmutable(): ImmutableItemStack? = ImmutableItemStack.of(this)

fun ItemStack.toImmutableOrThrow(): ImmutableItemStack = this.toImmutable() ?: error("ItemStack must not be empty")

fun ImmutableItemStack.maxStackSize(): Int = this.unwrap().maxStackSize

fun ImmutableItemStack.hasCraftingRemainingItem(): Boolean = this.unwrap().hasCraftingRemainingItem()

fun ImmutableItemStack.getCraftingRemainingItem(): ImmutableItemStack? = this.unwrap().craftingRemainingItem.toImmutable()
