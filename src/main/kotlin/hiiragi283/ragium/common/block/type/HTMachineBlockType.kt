package hiiragi283.ragium.common.block.type

import hiiragi283.ragium.api.block.type.BlockAttributeMap
import hiiragi283.ragium.api.block.type.HTEntityBlockType
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.common.tier.HTMachineTier
import hiiragi283.ragium.config.RagiumConfig
import java.util.function.BiFunction
import java.util.function.Supplier

class HTMachineBlockType(blockEntityTypeGetter: Supplier<HTDeferredBlockEntityType<*>>, attributeMap: BlockAttributeMap) :
    HTEntityBlockType(blockEntityTypeGetter, attributeMap) {
    companion object {
        @JvmStatic
        fun builder(blockEntityTypeGetter: Supplier<HTDeferredBlockEntityType<*>>): Builder =
            Builder(blockEntityTypeGetter, ::HTMachineBlockType)
    }

    //    Builder    //

    class Builder(
        blockEntityTypeGetter: Supplier<HTDeferredBlockEntityType<*>>,
        factory: BiFunction<Supplier<HTDeferredBlockEntityType<*>>, BlockAttributeMap, HTMachineBlockType>,
    ) : HTEntityBlockType.Builder<HTMachineBlockType, Builder>(blockEntityTypeGetter, factory) {
        fun addGeneratorTier(tier: HTMachineTier): Builder = addTier(tier)
            .addEnergy(
                RagiumConfig.COMMON.energyCapacity[tier]!!,
                RagiumConfig.COMMON.energyRate[tier]!!,
            )

        fun addConsumerTier(tier: HTMachineTier): Builder = addTier(tier)
            .addEnergy(
                RagiumConfig.COMMON.energyCapacity[tier]!!,
                RagiumConfig.COMMON.energyUsage[tier]!!,
            )
    }
}
