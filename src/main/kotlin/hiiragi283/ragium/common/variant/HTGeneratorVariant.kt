package hiiragi283.ragium.common.variant

import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.registry.impl.HTBasicDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.variant.HTVariantKey
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.tier.HTMachineTier
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumBlocks
import kotlin.lazy

enum class HTGeneratorVariant(val tier: HTMachineTier, private val enUsPattern: String, private val jaJpPattern: String) :
    HTVariantKey.WithBE<HTBlockEntity> {
    // Basic
    THERMAL(HTMachineTier.BASIC, "Thermal", "火力"),

    // Advanced
    COMBUSTION(HTMachineTier.ADVANCED, "Combustion", "燃焼"),
    SOLAR(HTMachineTier.ADVANCED, "Solar", "太陽光"),

    // Elite
    NUCLEAR(HTMachineTier.ELITE, "Nuclear Power", "原子力"),
    ;

    val energyRate: Int get() = RagiumConfig.COMMON.generatorEnergyRate[this]!!.asInt

    override val blockHolder: HTBasicDeferredBlock<HTEntityBlock> by lazy { RagiumBlocks.GENERATORS[this]!! }
    override val blockEntityHolder: HTDeferredBlockEntityType<HTBlockEntity> by lazy { RagiumBlockEntityTypes.GENERATORS[this]!! }

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> "$enUsPattern Generator"
        HTLanguageType.JA_JP -> "${jaJpPattern}発電機"
    }

    override fun getSerializedName(): String = name.lowercase()
}
