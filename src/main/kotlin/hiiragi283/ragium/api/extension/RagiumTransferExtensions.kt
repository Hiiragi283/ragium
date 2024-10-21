package hiiragi283.ragium.api.extension

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.item.ItemStack

//    Transaction    //

inline fun <R> useTransaction(action: (Transaction) -> R): R = Transaction.openOuter().use(action)

//    ResourceAmount    //

val ResourceAmount<ItemVariant>.itemStack: ItemStack
    get() = resource.toStack(amount.toInt())

val ResourceAmount<FluidVariant>.bucketStack: ItemStack
    get() = ItemStack(resource.fluid.bucketItem, amount.toInt())
