package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.item.HTBlockItem
import hiiragi283.ragium.api.registry.HTDeferredRegister
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import java.util.function.Function
import java.util.function.Supplier

/**
 * @see [Items]
 */
class HTDeferredItemRegister(namespace: String) : HTDeferredRegister<Item>(Registries.ITEM, namespace) {
    fun <BLOCK : Block> registerBlockItem(
        name: String,
        block: Supplier<BLOCK>,
        properties: Item.Properties = Item.Properties(),
    ): HTDeferredItem<HTBlockItem<BLOCK>> = register(name) { _: ResourceLocation -> HTBlockItem(block.get(), properties) }

    fun <BLOCK : Block> registerBlockItem(
        block: Holder<BLOCK>,
        properties: Item.Properties = Item.Properties(),
    ): HTDeferredItem<HTBlockItem<BLOCK>> = registerBlockItem(block.idOrThrow.path, block::value, properties)

    fun <ITEM : Item> registerItem(
        name: String,
        factory: (Item.Properties) -> ITEM,
        properties: Item.Properties = Item.Properties(),
    ): HTDeferredItem<ITEM> = register(name) { _: ResourceLocation -> factory(properties) }

    fun registerSimpleItem(name: String, properties: Item.Properties = Item.Properties()): HTDeferredItem<Item> =
        registerItem(name, ::Item, properties)

    //    HTDeferredRegister    //

    override fun getEntries(): Collection<HTDeferredItem<*>> = super.getEntries().filterIsInstance<HTDeferredItem<*>>()

    override fun <I : Item> register(name: String, func: Function<ResourceLocation, out I>): HTDeferredItem<I> =
        super.register(name, func) as HTDeferredItem<I>

    override fun <I : Item> register(name: String, sup: Supplier<out I>): HTDeferredItem<I> = super.register(name, sup) as HTDeferredItem<I>

    override fun <I : Item> createHolder(registryKey: ResourceKey<out Registry<Item>>, key: ResourceLocation): HTDeferredItem<I> =
        HTDeferredItem(ResourceKey.create(registryKey, key))
}
