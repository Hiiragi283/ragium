package hiiragi283.ragium.common.variant

import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.registry.impl.HTBasicDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.variant.HTVariantKey
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.material.HTTierType
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumBlocks

enum class HTMachineVariant(val tier: HTTierType, private val enUsPattern: String, private val jaJpPattern: String) :
    HTVariantKey.WithBE<HTBlockEntity> {
    // Basic
    ALLOY_SMELTER(HTTierType.ADVANCED, "Alloy Smelter", "合金炉"),
    BLOCK_BREAKER(HTTierType.BASIC, "Block Breaker", "採掘機"),
    COMPRESSOR(HTTierType.BASIC, "Compressor", "圧縮機"),
    ENGRAVER(HTTierType.BASIC, "Engraver", "彫刻機"),
    EXTRACTOR(HTTierType.BASIC, "Extractor", "抽出機"),
    PULVERIZER(HTTierType.BASIC, "Pulverizer", "粉砕機"),

    // Advanced
    CRUSHER(HTTierType.ADVANCED, "Crusher", "破砕機"),
    MELTER(HTTierType.ADVANCED, "Melter", "溶融炉"),
    REFINERY(HTTierType.ADVANCED, "Refinery", "精製機"),

    // Elite
    MULTI_SMELTER(HTTierType.ELITE, "Multi Smelter", "並列製錬炉"),
    SIMULATOR(HTTierType.ELITE, "Simulation Chamber", "シミュレーション室"),
    ;

    val energyUsage: Int get() = RagiumConfig.CONFIG.machineEnergyUsage[this]!!.asInt

    override val blockHolder: HTBasicDeferredBlock<HTEntityBlock> get() = RagiumBlocks.MACHINES[this]!!
    override val blockEntityHolder: HTDeferredBlockEntityType<HTBlockEntity> get() = RagiumBlockEntityTypes.MACHINES[this]!!

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enUsPattern
        HTLanguageType.JA_JP -> jaJpPattern
    }

    override fun getSerializedName(): String = name.lowercase()
}
