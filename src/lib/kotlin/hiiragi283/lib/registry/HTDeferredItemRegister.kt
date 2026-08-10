package hiiragi283.lib.registry

import hiiragi283.lib.util.Identity
import hiiragi283.lib.util.identity
import java.util.function.Function
import java.util.function.Supplier
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item

typealias ItemWithContextFactory<C, ITEM> = (C, Item.Properties) -> ITEM

/**
 * [アイテム][Item]向けの[HTDeferredRegister]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTDeferredItemRegister(namespace: String) : HTDeferredRegister<Item>(Registries.ITEM, namespace) {
    /**
     * 新しいアイテムを登録します。
     * @param ITEM アイテムのクラス
     * @param name アイテムのIDのパス
     * @param factory [Item.Properties]からアイテムを作るブロック
     * @param operator [Item.Properties]を初期化するブロック
     * @return 新しい[HTDeferredItem]のインスタンス
     */
    fun <ITEM : Item> registerItem(name: String, factory: (Item.Properties) -> ITEM, operator: Identity<Item.Properties> = identity()): HTDeferredItem<ITEM> = this.register(name) { id: Identifier -> Item.Properties().setId(createKey(id)).let(operator).let(factory) }

    /**
     * 新しいアイテムを登録します。
     * @param name アイテムのIDのパス
     * @param operator [Item.Properties]を初期化するブロック
     * @return 新しい[HTSimpleDeferredItem]のインスタンス
     */
    fun registerSimpleItem(name: String, operator: Identity<Item.Properties> = identity()): HTSimpleDeferredItem = this.registerItem(name, ::Item, operator)

    /**
     * 新しいアイテムを登録します。
     * @param ITEM アイテムのクラス
     * @param C コンテキストのクラス
     * @param name アイテムのIDのパス
     * @param factory [Item.Properties]とコンテキストからアイテムを作るブロック
     * @param operator [Item.Properties]を初期化するブロック
     * @return 新しい[HTDeferredItem]のインスタンス
     */
    fun <ITEM : Item, C> registerItemWith(
        name: String,
        context: C,
        factory: ItemWithContextFactory<C, ITEM>,
        operator: Identity<Item.Properties> = identity(),
    ): HTDeferredItem<ITEM> = registerItem(name, { factory(context, it) }, operator)

    //    HTDeferredRegister    //

    override fun <I : Item> createHolder(registryKey: RegistryKey<Item>, key: Identifier): HTDeferredItem<I> = HTDeferredItem(key)

    override fun <I : Item> register(name: String, sup: Supplier<out I>): HTDeferredItem<I> = super.register(name, sup) as HTDeferredItem<I>

    override fun <I : Item> register(name: String, func: Function<Identifier, out I>): HTDeferredItem<I> = super.register(name, func) as HTDeferredItem<I>

    override fun asSequence(): Sequence<HTDeferredItem<*>> = super.asSequence().filterIsInstance<HTDeferredItem<*>>()
}
