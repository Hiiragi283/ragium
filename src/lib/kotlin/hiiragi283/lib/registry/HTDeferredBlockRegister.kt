package hiiragi283.lib.registry

import java.util.function.Function
import java.util.function.Supplier
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour

typealias BlockFactory<BLOCK> = (BlockBehaviour.Properties) -> BLOCK

typealias BlockWithContextFactory<C, BLOCK> = (C, BlockBehaviour.Properties) -> BLOCK

/**
 * [ブロック][Block]向けの[HTDeferredRegister]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTDeferredBlockRegister(namespace: String) : HTDeferredRegister<Block>(Registries.BLOCK, namespace) {
    /**
     * 新しいブロックを登録します。
     * @param BLOCK ブロックのクラス
     * @param name ブロックのIDのパス
     * @param blockProp ブロックのプロパティ
     * @param factory [BlockBehaviour.Properties]からブロックを作るブロック
     * @return 新しい[HTDeferredBlock]のインスタンス
     */
    fun <BLOCK : Block> registerBlock(name: String, blockProp: BlockBehaviour.Properties, factory: BlockFactory<BLOCK>): HTDeferredBlock<BLOCK> = this.register(name) { id: Identifier -> blockProp.setId(createKey(id)).let(factory) }

    /**
     * 新しいブロックを登録します。
     * @param BLOCK ブロックのクラス
     * @param name ブロックのIDのパス
     * @param blockProp ブロックのプロパティ
     * @param factory [BlockBehaviour.Properties]からブロックを作るブロック
     * @return 新しい[HTDeferredBlock]のインスタンス
     */
    fun <BLOCK : Block> registerBlock(name: String, blockProp: Supplier<BlockBehaviour.Properties>, factory: BlockFactory<BLOCK>): HTDeferredBlock<BLOCK> = this.register(name) { id: Identifier -> blockProp.get().setId(createKey(id)).let(factory) }

    /**
     * 新しいブロックを登録します。
     * @param name ブロックのIDのパス
     * @param blockProp ブロックのプロパティ
     * @return 新しい[HTSimpleDeferredBlock]のインスタンス
     */
    fun registerSimpleBlock(name: String, blockProp: BlockBehaviour.Properties): HTSimpleDeferredBlock = this.registerBlock(name, blockProp, ::Block)

    /**
     * 新しいブロックを登録します。
     * @param name ブロックのIDのパス
     * @param blockProp ブロックのプロパティ
     * @return 新しい[HTSimpleDeferredBlock]のインスタンス
     */
    fun registerSimpleBlock(name: String, blockProp: Supplier<BlockBehaviour.Properties>): HTSimpleDeferredBlock = this.registerBlock(name, blockProp, ::Block)

    //    HTDeferredRegister    //

    override fun <I : Block> createHolder(registryKey: RegistryKey<Block>, key: Identifier): HTDeferredBlock<I> = HTDeferredBlock(key)

    override fun <I : Block> register(name: String, sup: Supplier<out I>): HTDeferredBlock<I> = super.register(name, sup) as HTDeferredBlock<I>

    override fun <I : Block> register(name: String, func: Function<Identifier, out I>): HTDeferredBlock<I> = super.register(name, func) as HTDeferredBlock<I>

    override fun asSequence(): Sequence<HTDeferredBlock<*>> = super.asSequence().filterIsInstance<HTDeferredBlock<*>>()
}
