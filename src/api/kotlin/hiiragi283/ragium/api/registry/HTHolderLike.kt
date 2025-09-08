package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.extension.andThen
import hiiragi283.ragium.api.extension.idOrThrow
import net.minecraft.core.Holder
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
        private fun <T : Any> fromHolder(f: () -> Holder<T>): HTHolderLike = f.andThen(Holder<T>::idOrThrow).let(::HTHolderLike)

        @JvmStatic
        fun fromBlock(block: Block): HTHolderLike = block::builtInRegistryHolder.let(::fromHolder)

        @JvmStatic
        fun fromEntity(type: EntityType<*>): HTHolderLike = type::builtInRegistryHolder.let(::fromHolder)

        @JvmStatic
        fun fromFluid(fluid: Fluid): HTHolderLike = fluid::builtInRegistryHolder.let(::fromHolder)

        @JvmStatic
        fun fromItem(item: ItemLike): HTHolderLike = item::asItem.andThen(Item::builtInRegistryHolder).let(::fromHolder)
    }
}
