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

sealed interface HTGeneratorVariant<BLOCK : HTEntityBlock, BE : HTGeneratorBlockEntity> : HTVariantKey.WithBlockAndBE<BLOCK, BE> {
    companion object {
        @JvmStatic
        val entries: List<HTGeneratorVariant<*, *>> by lazy {
            buildList {
                addAll(Fuel.entries)
                add(Solar)
                add(Nuclear)
            }
        }
    }

    val energyRate: Int get() = RagiumConfig.COMMON.generatorEnergyRate[this]!!.asInt

    val tier: HTMachineTier

    //    Fuel    //

    enum class Fuel(override val tier: HTMachineTier, private val enUsPattern: String, private val jaJpPattern: String) :
        HTGeneratorVariant<HTHorizontalEntityBlock, HTFuelGeneratorBlockEntity> {
        THERMAL(HTMachineTier.BASIC, "Thermal Generator", "火力発電機"),
        COMBUSTION(HTMachineTier.ADVANCED, "Combustion Generator", "燃焼発電機"),
        ;

        override val blockHolder: HTDeferredBlock<HTHorizontalEntityBlock, *>
            get() = when (this) {
                THERMAL -> RagiumBlocks.THERMAL_GENERATOR
                COMBUSTION -> RagiumBlocks.COMBUSTION_GENERATOR
            }

        override fun translate(type: HTLanguageType, value: String): String = when (type) {
            HTLanguageType.EN_US -> enUsPattern
            HTLanguageType.JA_JP -> jaJpPattern
        }

        override fun variantName(): String = "${name.lowercase()}_generator"

        override val blockEntityHolder: HTDeferredBlockEntityType<out HTFuelGeneratorBlockEntity>
            get() = when (this) {
                THERMAL -> RagiumBlockEntityTypes.THERMAL
                COMBUSTION -> RagiumBlockEntityTypes.COMBUSTION
            }
    }

    //    Advanced    //

    data object Solar : HTGeneratorVariant<HTEntityBlock, HTSolarGeneratorBlockEntity> {
        override val blockHolder: HTDeferredBlock<HTEntityBlock, *>
            get() = RagiumBlocks.SOLAR_PANEL_CONTROLLER

        override fun translate(type: HTLanguageType, value: String): String = when (type) {
            HTLanguageType.EN_US -> "Solar Panel Controller"
            HTLanguageType.JA_JP -> "太陽光パネルコントローラー"
        }

        override fun variantName(): String = "solar_generator"

        override val blockEntityHolder: HTDeferredBlockEntityType<out HTSolarGeneratorBlockEntity>
            get() = RagiumBlockEntityTypes.SOLAR

        override val tier: HTMachineTier = HTMachineTier.ADVANCED
    }

    //    Elite    //

    data object Nuclear : HTGeneratorVariant<HTEntityBlock, HTNuclearReactorBlockEntity> {
        override val blockHolder: HTDeferredBlock<HTEntityBlock, *>
            get() = RagiumBlocks.NUCLEAR_REACTOR

        override fun translate(type: HTLanguageType, value: String): String = when (type) {
            HTLanguageType.EN_US -> "Nuclear Reactor"
            HTLanguageType.JA_JP -> "原子炉"
        }

        override fun variantName(): String = "nuclear_reactor"

        override val blockEntityHolder: HTDeferredBlockEntityType<out HTNuclearReactorBlockEntity>
            get() = RagiumBlockEntityTypes.NUCLEAR

        override val tier: HTMachineTier = HTMachineTier.ELITE
    }
}
