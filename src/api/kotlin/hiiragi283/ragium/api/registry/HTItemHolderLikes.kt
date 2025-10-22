package hiiragi283.ragium.api.registry

import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import java.util.function.Supplier

fun ResourceKey<Item>.toHolderLike(): HTItemHolderLike = HTItemHolderLike.fromKey(this)

fun Supplier<out Item>.toHolderLike(): HTItemHolderLike = HTItemHolderLike.fromItem(this)

fun ItemLike.toHolderLike(): HTItemHolderLike = HTItemHolderLike.fromItem(this)
