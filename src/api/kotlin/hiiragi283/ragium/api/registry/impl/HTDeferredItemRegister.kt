package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.extension.RegistryKey
import hiiragi283.ragium.api.extension.createKey
import hiiragi283.ragium.api.registry.HTDeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import java.util.function.Function
import java.util.function.Supplier

/**
 * @see [Items]
 */
class HTDeferredItemRegister(namespace: String) : HTDeferredRegister<Item>(Registries.ITEM, namespace) {
    fun <ITEM : Item> registerItem(
        name: String,
        factory: (Item.Properties) -> ITEM,
        properties: Item.Properties = Item.Properties(),
    ): HTDeferredItem<ITEM> = register(name) { _: ResourceLocation -> factory(properties) }

    fun registerSimpleItem(name: String, properties: Item.Properties = Item.Properties()): HTDeferredItem<Item> =
        registerItem(name, ::Item, properties)

    //    HTDeferredRegister    //

    override fun asSequence(): Sequence<HTDeferredItem<*>> = super.asSequence().filterIsInstance<HTDeferredItem<*>>()

    override fun getEntries(): Collection<HTDeferredItem<*>> = super.getEntries().filterIsInstance<HTDeferredItem<*>>()

    override fun <I : Item> register(name: String, func: Function<ResourceLocation, out I>): HTDeferredItem<I> =
        super.register(name, func) as HTDeferredItem<I>

    override fun <I : Item> register(name: String, sup: Supplier<out I>): HTDeferredItem<I> = super.register(name, sup) as HTDeferredItem<I>

    override fun <I : Item> createHolder(registryKey: RegistryKey<Item>, key: ResourceLocation): HTDeferredItem<I> =
        HTDeferredItem(registryKey.createKey(key))
}
