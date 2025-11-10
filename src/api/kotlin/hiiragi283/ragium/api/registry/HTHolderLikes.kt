package hiiragi283.ragium.api.registry

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid

fun Block.toHolderLike(): HTHolderLike = HTHolderLike.fromBlock(this)

fun EntityType<*>.toHolderLike(): HTHolderLike = HTHolderLike.fromEntity(this)

fun Fluid.toHolderLike(): HTHolderLike = HTHolderLike.fromFluid(this)

/**
 * `block/`で前置された[HTHolderLike.getId]
 */
val HTHolderLike.blockId: ResourceLocation get() = getIdWithPrefix("block/")

/**
 * `item/`で前置された[HTHolderLike.getId]
 */
val HTHolderLike.itemId: ResourceLocation get() = getIdWithPrefix("item/")
