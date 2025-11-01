package hiiragi283.ragium.api.block.type

import hiiragi283.ragium.api.block.attribute.HTEnergyBlockAttribute
import hiiragi283.ragium.api.block.attribute.HTMenuBlockAttribute
import hiiragi283.ragium.api.block.attribute.HTTierBlockAttribute
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.registry.impl.HTDeferredMenuType
import hiiragi283.ragium.api.tier.HTTierProvider
import net.minecraft.world.level.block.entity.BlockEntity
import java.util.function.BiFunction
import java.util.function.IntSupplier
import java.util.function.Supplier

/**
 * [BlockEntity]を保持するブロック向けの[HTBlockType]の拡張クラス
 * @param blockEntityTypeGetter [HTDeferredBlockEntityType]を渡す[Supplier]
 * @see mekanism.common.content.blocktype.BlockTypeTile
 */
open class HTEntityBlockType(private val blockEntityTypeGetter: Supplier<HTDeferredBlockEntityType<*>>, attributeMap: BlockAttributeMap) :
    HTBlockType(attributeMap) {
    companion object {
        @JvmStatic
        fun builder(blockEntityTypeGetter: Supplier<HTDeferredBlockEntityType<*>>): Builder.Impl<HTEntityBlockType> =
            Builder.Impl(blockEntityTypeGetter, ::HTEntityBlockType)
    }

    fun getBlockEntityType(): HTDeferredBlockEntityType<*> = blockEntityTypeGetter.get()

    //    Builder    //

    /**
     * [HTEntityBlockType]向けの[HTBlockType.Builder]の拡張クラス
     * @param TYPE [HTEntityBlockType]を継承したタイプのクラス
     * @param BUILDER [Builder]を継承したビルダーのクラス
     * @param blockEntityTypeGetter [HTDeferredBlockEntityType]を渡す[Supplier]
     * @param factory [blockEntityTypeGetter]と[BlockAttributeMap]から[TYPE]に変換するブロック
     */
    abstract class Builder<TYPE : HTEntityBlockType, BUILDER : Builder<TYPE, BUILDER>>(
        private val blockEntityTypeGetter: Supplier<HTDeferredBlockEntityType<*>>,
        factory: BiFunction<Supplier<HTDeferredBlockEntityType<*>>, BlockAttributeMap, TYPE>,
    ) : HTBlockType.Builder<TYPE, BUILDER>({ map: BlockAttributeMap -> factory.apply(blockEntityTypeGetter, map) }) {
        /**
         * GUIを追加します。
         * @param C [HTDeferredMenuType.WithContext]におけるコンテキストのクラス
         */
        fun <C> addMenu(type: Supplier<HTDeferredMenuType.WithContext<*, C>>): BUILDER = add(HTMenuBlockAttribute(type))

        /**
         * エネルギーストレージの基礎情報を追加します。
         * @param capacity ストレージの容量
         */
        fun addEnergy(capacity: IntSupplier): BUILDER = add(HTEnergyBlockAttribute(capacity))

        /**
         * エネルギーストレージの基礎情報を追加します。
         * @param capacity ストレージの容量
         * @param usage 処理当たりの使用量
         */
        fun addEnergy(capacity: IntSupplier, usage: IntSupplier): BUILDER = add(HTEnergyBlockAttribute(capacity, usage))

        /**
         * ティアを追加します。
         * @param TIER [HTTierProvider]を実装したクラス
         * @param tier ブロックのティア
         */
        fun <TIER : HTTierProvider> addTier(tier: TIER): BUILDER = add(HTTierBlockAttribute(tier))

        //    Impl    //

        /**
         * [Builder]の簡易的な実装
         */
        class Impl<TYPE : HTEntityBlockType>(
            blockEntityTypeGetter: Supplier<HTDeferredBlockEntityType<*>>,
            factory: BiFunction<Supplier<HTDeferredBlockEntityType<*>>, BlockAttributeMap, TYPE>,
        ) : Builder<TYPE, Impl<TYPE>>(blockEntityTypeGetter, factory)
    }
}
