@file:Suppress("DEPRECATION")

package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.world.Containers
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.neoforged.neoforge.items.IItemHandler

//    ItemLike    //

fun ItemLike.asHolder(): Holder.Reference<Item> = asItem().builtInRegistryHolder()

//    Item    //

fun itemProperty(): Item.Properties = Item.Properties()

//    ItemStack    //

/**
 * 指定した[item]とアイテムが一致するか判定します。
 */
fun ItemStack.isOf(item: ItemLike): Boolean = `is`(item.asItem())

/**
 * 残りの耐久値を返します。
 */
val ItemStack.restDamage: Int
    get() = maxDamage - damageValue

/**
 * 現在の個数が[isMaxCount]と一致するか判定します。
 */
val ItemStack.isMaxCount: Boolean
    get() = count == maxStackSize

val ItemStack.machineTier: HTMachineTier
    get() = getOrDefault(RagiumComponentTypes.MACHINE_TIER, HTMachineTier.PRIMITIVE)

//    IItemHandler    //

fun IItemHandler.dropStacks(level: Level, pos: BlockPos) {
    (0 until this.slots)
        .map(this::getStackInSlot)
        .forEach { Containers.dropItemStack(level, pos.x.toDouble(), pos.z.toDouble(), pos.y.toDouble(), it) }
}
