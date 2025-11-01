package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.function.IdToFunction
import hiiragi283.ragium.api.function.ItemFactory
import hiiragi283.ragium.api.function.ItemWithContextFactory
import hiiragi283.ragium.api.function.partially1
import hiiragi283.ragium.api.registry.HTDeferredRegister
import hiiragi283.ragium.api.registry.RegistryKey
import hiiragi283.ragium.api.registry.createKey
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import java.util.function.Supplier
import java.util.function.UnaryOperator

/**
 * @see [Items]
 */
class HTDeferredItemRegister(namespace: String) : HTDeferredRegister<Item>(Registries.ITEM, namespace) {
    fun <ITEM : Item> registerItem(
        name: String,
        factory: ItemFactory<ITEM>,
        operator: UnaryOperator<Item.Properties> = UnaryOperator.identity(),
    ): HTDeferredItem<ITEM> = register(name) { _: ResourceLocation -> factory(operator.apply(Item.Properties())) }

    fun registerSimpleItem(name: String, operator: UnaryOperator<Item.Properties> = UnaryOperator.identity()): HTSimpleDeferredItem =
        registerItem(name, ::Item, operator)

    fun <ITEM : Item, C> registerItemWith(
        name: String,
        context: C,
        factory: ItemWithContextFactory<C, ITEM>,
        operator: UnaryOperator<Item.Properties> = UnaryOperator.identity(),
    ): HTDeferredItem<ITEM> = registerItem(name, factory.partially1(context), operator)

    //    HTDeferredRegister    //

    override fun asSequence(): Sequence<HTDeferredItem<*>> = super.asSequence().filterIsInstance<HTDeferredItem<*>>()

    override fun getEntries(): Collection<HTDeferredItem<*>> = super.getEntries().filterIsInstance<HTDeferredItem<*>>()

    override fun <I : Item> register(name: String, func: IdToFunction<out I>): HTDeferredItem<I> =
        super.register(name, func) as HTDeferredItem<I>

    override fun <I : Item> register(name: String, sup: Supplier<out I>): HTDeferredItem<I> = super.register(name, sup) as HTDeferredItem<I>

    override fun <I : Item> createHolder(registryKey: RegistryKey<Item>, key: ResourceLocation): HTDeferredItem<I> =
        HTDeferredItem(registryKey.createKey(key))
}
