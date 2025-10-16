package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.storage.item.ImmutableItemStack
import hiiragi283.ragium.api.storage.item.isOf
import net.minecraft.world.item.ItemStack

/**
 * 指定した[stack]にアイテムが一致するか判定します。
 */
fun HTItemHolderLike.isOf(stack: ItemStack): Boolean = stack.`is`(asItem())

/**
 * 指定した[stack]にアイテムが一致するか判定します。
 */
fun HTItemHolderLike.isOf(stack: ImmutableItemStack): Boolean = stack.isOf(asItem())
