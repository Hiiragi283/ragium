package hiiragi283.lib.registry

import hiiragi283.lib.util.Identity
import hiiragi283.lib.util.identity
import net.minecraft.resources.Identifier
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.bus.api.IEventBus

/**
 * [ブロック][Block]と[アイテム][Item]をまとめて登録する[HTDeferredRegister]の補助クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTDeferredBlockAndItemRegister(
    private val blockRegister: HTDeferredBlockRegister,
    private val itemRegister: HTDeferredItemRegister
) {
    constructor(namespace: String) : this(HTDeferredBlockRegister(namespace))

    constructor(blockRegister: HTDeferredBlockRegister) : this(
        blockRegister,
        HTDeferredItemRegister(blockRegister.namespace)
    )

    /**
     * @since 26.1.3
     */
    fun addAlias(from: String, to: String) {
        blockRegister.addAlias(from, to)
        itemRegister.addAlias(from, to)
    }

    /**
     * @since 26.1.3
     */
    fun addAlias(from: Identifier, to: Identifier) {
        blockRegister.addAlias(from, to)
        itemRegister.addAlias(from, to)
    }

    /**
     * 新しいブロックとアイテムをまとめて登録します。
     * @param name ブロックとアイテムのIDのパス
     * @param blockProp ブロックのプロパティ
     * @param itemProp [Item.Properties]を初期化するブロック
     * @return 新しい[HTSimpleDeferredBlockAndItem]のインスタンス
     */
    fun registerSimple(
        name: String,
        blockProp: BlockBehaviour.Properties,
        itemProp: Identity<Item.Properties> = identity()
    ): HTSimpleDeferredBlockAndItem = registerSimple(name, blockProp, ::Block, itemProp)

    /**
     * 新しいブロックとアイテムをまとめて登録します。
     * @param BLOCK ブロックのクラス
     * @param name ブロックとアイテムのIDのパス
     * @param blockProp ブロックのプロパティ
     * @param blockFactory [BlockBehaviour.Properties]からブロックを作るブロック
     * @param itemProp [Item.Properties]を初期化するブロック
     * @return 新しい[HTBasicDeferredBlockAndItem]のインスタンス
     */
    fun <BLOCK : Block> registerSimple(
        name: String,
        blockProp: BlockBehaviour.Properties,
        blockFactory: BlockFactory<BLOCK>,
        itemProp: Identity<Item.Properties> = identity()
    ): HTBasicDeferredBlockAndItem<BLOCK> = register(name, blockProp, blockFactory, ::BlockItem, itemProp)

    /**
     * 新しいブロックとアイテムをまとめて登録します。
     * @param BLOCK ブロックのクラス
     * @param ITEM アイテムのクラス
     * @param name ブロックとアイテムのIDのパス
     * @param blockProp ブロックのプロパティ
     * @param blockFactory [BlockBehaviour.Properties]からブロックを作るブロック
     * @param itemFactory [Item.Properties]と[BLOCK]からアイテムを作るブロック
     * @param itemProp [Item.Properties]を初期化するブロック
     * @return 新しい[HTDeferredBlockAndItem]のインスタンス
     */
    fun <BLOCK : Block, ITEM : Item> register(
        name: String,
        blockProp: BlockBehaviour.Properties,
        blockFactory: BlockFactory<BLOCK>,
        itemFactory: ItemWithContextFactory<BLOCK, ITEM>,
        itemProp: Identity<Item.Properties> = identity()
    ): HTDeferredBlockAndItem<BLOCK, ITEM> {
        val blockHolder: HTDeferredBlock<BLOCK> = blockRegister.registerBlock(name, blockProp, blockFactory)
        val itemHolder: HTDeferredItem<ITEM> = itemRegister.registerItem(
            name,
            { prop: Item.Properties -> itemFactory(blockHolder.get(), prop.useBlockDescriptionPrefix()) },
            itemProp
        )
        return HTDeferredBlockAndItem(blockHolder, itemHolder)
    }

    /**
     * 登録されたブロックの一覧を取得します。
     */
    fun asBlockSequence(): Sequence<HTDeferredBlock<*>> = blockRegister.asSequence()

    /**
     * 登録されたアイテムの一覧を取得します。
     */
    fun asItemSequence(): Sequence<HTDeferredItem<*>> = itemRegister.asSequence()

    /**
     * [IEventBus]に登録します。
     */
    fun register(bus: IEventBus) {
        blockRegister.register(bus)
        itemRegister.register(bus)
    }
}
