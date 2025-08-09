package hiiragi283.ragium.api.registry

import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.extensions.IHolderExtension
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.datamaps.DataMapType
import java.util.function.Supplier

interface HTItemHolderLike :
    Supplier<Item>,
    ItemLike,
    IHolderExtension<Item> {
    val holder: DeferredItem<*>

    val id: ResourceLocation get() = holder.id
    val itemId: ResourceLocation get() = id.withPrefix("item/")

    fun toStack(count: Int = 1): ItemStack = ItemStack(get(), count)

    override fun get(): Item = holder.get()

    override fun asItem(): Item = get().asItem()

    override fun getDelegate(): Holder<Item> = holder.delegate

    override fun unwrapLookup(): HolderLookup.RegistryLookup<Item>? = holder.unwrapLookup()

    override fun getKey(): ResourceKey<Item>? = holder.key

    override fun <T : Any> getData(type: DataMapType<Item, T>): T? = holder.getData(type)

    interface Typed<V : HTVariantKey> : HTItemHolderLike {
        val variant: V
    }
}
