package hiiragi283.ragium.api.registry

import net.minecraft.world.level.ItemLike

fun ItemLike.toHolderLike(): HTItemHolderLike = HTItemHolderLike.fromItem(this)
