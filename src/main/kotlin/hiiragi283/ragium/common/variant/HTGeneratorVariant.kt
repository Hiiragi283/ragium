package hiiragi283.ragium.common.variant

import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.registry.impl.HTBasicDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.variant.HTVariantKey
import hiiragi283.ragium.common.block.entity.generator.HTGeneratorBlockEntity
import hiiragi283.ragium.common.tier.HTMachineTier
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumBlocks
import kotlin.lazy

enum class HTGeneratorVariant(
    val tier: HTMachineTier,
    private val enUsPattern: String,
    private val jaJpPattern: String,
    private val customName: String? = null,
) : HTVariantKey.WithBlock<HTEntityBlock>,
    HTVariantKey.WithBE<HTGeneratorBlockEntity> {
    // Basic
    THERMAL(HTMachineTier.BASIC, "Thermal Generator", "火力発電機", "thermal_generator"),

    // Advanced
    COMBUSTION(HTMachineTier.ADVANCED, "Combustion Generator", "燃焼発電機", "combustion_generator"),
    SOLAR(HTMachineTier.ADVANCED, "Solar Generator", "太陽光発電機", "solar_generator"),

    // Elite
    NUCLEAR_REACTOR(HTMachineTier.ELITE, "Nuclear Reactor", "原子炉"),
    ;

    val energyRate: Int get() = RagiumConfig.COMMON.generatorEnergyRate[this]!!.asInt

    override val blockHolder: HTBasicDeferredBlock<HTEntityBlock> by lazy { RagiumBlocks.GENERATORS[this]!! }
    override val blockEntityHolder: HTDeferredBlockEntityType<HTGeneratorBlockEntity> by lazy { RagiumBlockEntityTypes.GENERATORS[this]!! }

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enUsPattern
        HTLanguageType.JA_JP -> jaJpPattern
    }

    override fun variantName(): String = customName ?: name.lowercase()
}
