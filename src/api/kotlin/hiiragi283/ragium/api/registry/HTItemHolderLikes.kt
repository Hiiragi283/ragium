package hiiragi283.ragium.api.registry

import net.minecraft.core.Holder
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike

fun ItemLike.toHolderLike(): HTItemHolderLike = HTItemHolderLike.fromItem(this)

@Suppress("DEPRECATION")
fun ItemLike.builtInRegistryHolder(): Holder.Reference<Item> = this.asItem().builtInRegistryHolder()
