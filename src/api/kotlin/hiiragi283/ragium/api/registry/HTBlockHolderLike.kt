package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.util.HTMaterialType
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.extensions.IHolderExtension
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.datamaps.DataMapType
import java.util.function.Supplier

interface HTBlockHolderLike :
    Supplier<Block>,
    ItemLike,
    IHolderExtension<Block> {
    val holder: DeferredBlock<*>

    val id: ResourceLocation get() = holder.id
    val blockId: ResourceLocation get() = id.withPrefix("block/")

    override fun get(): Block = holder.get()

    override fun asItem(): Item = get().asItem()

    override fun getDelegate(): Holder<Block> = holder.delegate

    override fun unwrapLookup(): HolderLookup.RegistryLookup<Block>? = holder.unwrapLookup()

    override fun getKey(): ResourceKey<Block>? = holder.key

    override fun <T : Any> getData(type: DataMapType<Block, T>): T? = holder.getData(type)

    interface Typed<V : HTVariantKey> : HTBlockHolderLike {
        val variant: V
    }

    interface Materialized :
        HTBlockHolderLike,
        HTMaterialType.Provider
}
