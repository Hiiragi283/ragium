package hiiragi283.ragium.common.variant

import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.api.block.HTHorizontalEntityBlock
import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.registry.impl.HTDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.variant.HTVariantKey
import hiiragi283.ragium.common.block.entity.generator.HTFuelGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTNuclearReactorBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTSolarGeneratorBlockEntity
import hiiragi283.ragium.common.tier.HTMachineTier
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumBlocks

sealed class HTGeneratorVariant<BLOCK : HTEntityBlock, BE : HTGeneratorBlockEntity>(
    val tier: HTMachineTier,
    private val enUsPattern: String,
    private val jaJpPattern: String,
) : HTVariantKey.WithBlock<BLOCK>,
    HTVariantKey.WithBE<BE> {
    companion object {
        @JvmField
        val entries: List<HTGeneratorVariant<*, *>> = listOf(Thermal, Combustion, Solar, Nuclear)
    }

    val energyRate: Int get() = RagiumConfig.COMMON.generatorEnergyRate[this]!!.asInt

    final override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enUsPattern
        HTLanguageType.JA_JP -> jaJpPattern
    }

    //    Basic    //

    data object Thermal : HTGeneratorVariant<HTHorizontalEntityBlock, HTFuelGeneratorBlockEntity>(
        HTMachineTier.BASIC,
        "Thermal Generator",
        "火力発電機",
    ) {
        override val blockHolder: HTDeferredBlock<HTHorizontalEntityBlock, *>
            get() = RagiumBlocks.THERMAL_GENERATOR

        override fun variantName(): String = "thermal_generator"

        override val blockEntityHolder: HTDeferredBlockEntityType<out HTFuelGeneratorBlockEntity>
            get() = RagiumBlockEntityTypes.THERMAL
    }

    //    Advanced    //

    data object Combustion : HTGeneratorVariant<HTHorizontalEntityBlock, HTFuelGeneratorBlockEntity>(
        HTMachineTier.ADVANCED,
        "Combustion Generator",
        "燃焼発電機",
    ) {
        override val blockHolder: HTDeferredBlock<HTHorizontalEntityBlock, *>
            get() = RagiumBlocks.COMBUSTION_GENERATOR

        override fun variantName(): String = "combustion_generator"

        override val blockEntityHolder: HTDeferredBlockEntityType<out HTFuelGeneratorBlockEntity>
            get() = RagiumBlockEntityTypes.COMBUSTION
    }

    data object Solar : HTGeneratorVariant<HTEntityBlock, HTSolarGeneratorBlockEntity>(
        HTMachineTier.ADVANCED,
        "Solar Panel Controller",
        "太陽光パネルコントローラー",
    ) {
        override val blockHolder: HTDeferredBlock<HTEntityBlock, *>
            get() = RagiumBlocks.SOLAR_PANEL_CONTROLLER

        override fun variantName(): String = "solar_generator"

        override val blockEntityHolder: HTDeferredBlockEntityType<out HTSolarGeneratorBlockEntity>
            get() = RagiumBlockEntityTypes.SOLAR
    }

    //    Elite    //

    data object Nuclear : HTGeneratorVariant<HTEntityBlock, HTNuclearReactorBlockEntity>(
        HTMachineTier.ELITE,
        "Nuclear Reactor",
        "原子炉",
    ) {
        override val blockHolder: HTDeferredBlock<HTEntityBlock, *>
            get() = RagiumBlocks.NUCLEAR_REACTOR

        override fun variantName(): String = "nuclear_reactor"

        override val blockEntityHolder: HTDeferredBlockEntityType<out HTNuclearReactorBlockEntity> get() =
            RagiumBlockEntityTypes.NUCLEAR
    }
}
