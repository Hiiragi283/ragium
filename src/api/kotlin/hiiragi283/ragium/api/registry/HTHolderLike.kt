package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.extension.idOrThrow
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid

/**
 * @see [mekanism.common.registration.INamedEntry]
 */
fun interface HTHolderLike {
    fun getId(): ResourceLocation

    fun getPath(): String = getId().path

    @Suppress("DEPRECATION")
    companion object {
        @JvmStatic
        fun fromId(id: ResourceLocation): HTHolderLike = HTHolderLike { id }

        @JvmStatic
        fun fromBlock(block: Block): HTHolderLike = HTHolderLike(block.builtInRegistryHolder()::idOrThrow)

        @JvmStatic
        fun fromEntity(type: EntityType<*>): HTHolderLike = HTHolderLike(type.builtInRegistryHolder()::idOrThrow)

        @JvmStatic
        fun fromFluid(fluid: Fluid): HTHolderLike = HTHolderLike(fluid.builtInRegistryHolder()::idOrThrow)

        @JvmStatic
        fun fromItem(item: ItemLike): HTHolderLike = fromItem(item.asItem())

        @JvmStatic
        fun fromItem(item: Item): HTHolderLike = HTHolderLike(item.builtInRegistryHolder()::idOrThrow)
    }
}
