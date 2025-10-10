package hiiragi283.ragium.api.registry

import net.minecraft.resources.ResourceLocation

/**
 * `block/`で前置された[HTHolderLike.getId]
 */
val HTHolderLike.blockId: ResourceLocation get() = getId().withPrefix("block/")

/**
 * `item/`で前置された[HTHolderLike.getId]
 */
val HTHolderLike.itemId: ResourceLocation get() = getId().withPrefix("item/")
