package hiiragi283.ragium.common.block.type

import hiiragi283.ragium.api.block.attribute.HTBlockAttribute
import hiiragi283.ragium.api.block.type.HTEntityBlockType
import hiiragi283.ragium.api.collection.AttributeMap
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.common.tier.HTMachineTier
import hiiragi283.ragium.config.RagiumConfig
import java.util.function.BiFunction
import java.util.function.Supplier

/**
 * 機械類向けに特化した[HTEntityBlockType]の拡張クラス
 */
class HTMachineBlockType(blockEntityTypeGetter: Supplier<HTDeferredBlockEntityType<*>>, attributeMap: AttributeMap<HTBlockAttribute>) :
    HTEntityBlockType(blockEntityTypeGetter, attributeMap) {
    companion object {
        @JvmStatic
        fun builder(blockEntityTypeGetter: Supplier<HTDeferredBlockEntityType<*>>): Builder =
            Builder(blockEntityTypeGetter, ::HTMachineBlockType)
    }

    //    Builder    //

    /**
     * [HTMachineBlockType]向けの[HTEntityBlockType.Builder]の拡張クラス
     */
    class Builder(
        blockEntityTypeGetter: Supplier<HTDeferredBlockEntityType<*>>,
        factory: BiFunction<Supplier<HTDeferredBlockEntityType<*>>, AttributeMap<HTBlockAttribute>, HTMachineBlockType>,
    ) : HTEntityBlockType.Builder<HTMachineBlockType, Builder>(blockEntityTypeGetter, factory) {
        /**
         * 発電機として，ティアを追加します。
         */
        fun addGeneratorTier(tier: HTMachineTier): Builder = addTier(tier)
            .addEnergy(
                RagiumConfig.COMMON.energyRate[tier]!!,
                RagiumConfig.COMMON.energyCapacity[tier]!!,
            )

        /**
         * 処理機械として，ティアを追加します。
         */
        fun addConsumerTier(tier: HTMachineTier): Builder = addTier(tier)
            .addEnergy(
                RagiumConfig.COMMON.energyUsage[tier]!!,
                RagiumConfig.COMMON.energyCapacity[tier]!!,
            )
    }
}
