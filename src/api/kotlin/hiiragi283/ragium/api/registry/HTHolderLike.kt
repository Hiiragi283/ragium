package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.extension.andThen
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid

/**
 * IDを取得可能できるインターフェース
 * @see [mekanism.common.registration.INamedEntry]
 */
fun interface HTHolderLike {
    fun getId(): ResourceLocation

    fun getPath(): String = getId().path

    @Suppress("DEPRECATION")
    companion object {
        private fun fromHolder(f: () -> Holder<*>): HTHolderLike = f.andThen(Holder<*>::idOrThrow).let(::HTHolderLike)

        /**
         * 指定した[Holder]を[HTHolderLike]に変換します。
         */
        @JvmStatic
        fun fromHolder(holder: Holder<*>): HTHolderLike = HTHolderLike(holder::idOrThrow)

        /**
         * 指定した[Block]を[HTHolderLike]に変換します。
         */
        @JvmStatic
        fun fromBlock(block: Block): HTHolderLike = block::builtInRegistryHolder.let(::fromHolder)

        /**
         * 指定した[EntityType]を[HTHolderLike]に変換します。
         */
        @JvmStatic
        fun fromEntity(type: EntityType<*>): HTHolderLike = type::builtInRegistryHolder.let(::fromHolder)

        /**
         * 指定した[Fluid]を[HTHolderLike]に変換します。
         */
        @JvmStatic
        fun fromFluid(fluid: Fluid): HTHolderLike = fluid::builtInRegistryHolder.let(::fromHolder)

        /**
         * 指定した[ItemLike]を[HTHolderLike]に変換します。
         */
        @JvmStatic
        fun fromItem(item: ItemLike): HTHolderLike = item::asItem.andThen(Item::builtInRegistryHolder).let(::fromHolder)
    }
}
