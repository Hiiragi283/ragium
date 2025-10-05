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

enum class HTMachineVariant(val tier: HTMachineTier, private val enPattern: String, private val jaPattern: String) :
    HTVariantKey.WithBE<HTBlockEntity> {
    // Basic
    ALLOY_SMELTER(HTMachineTier.ADVANCED, "Alloy Smelter", "合金炉"),
    BLOCK_BREAKER(HTMachineTier.BASIC, "Block Breaker", "採掘機"),
    COMPRESSOR(HTMachineTier.BASIC, "Compressor", "圧縮機"),
    CUTTING_MACHINE(HTMachineTier.BASIC, "Cutting Machine", "裁断機"),
    EXTRACTOR(HTMachineTier.BASIC, "Extractor", "抽出機"),
    PULVERIZER(HTMachineTier.BASIC, "Pulverizer", "粉砕機"),

    // Advanced
    CRUSHER(HTMachineTier.ADVANCED, "Crusher", "破砕機"),
    MELTER(HTMachineTier.ADVANCED, "Melter", "溶融炉"),
    REFINERY(HTMachineTier.ADVANCED, "Refinery", "精製機"),
    WASHER(HTMachineTier.ADVANCED, "Washer", "洗浄機"),

    // Elite
    BREWERY(HTMachineTier.ELITE, "Brewery", "醸造機"),
    MULTI_SMELTER(HTMachineTier.ELITE, "Multi Smelter", "並列製錬炉"),
    PLANTER(HTMachineTier.ELITE, "Planting Chamber", "栽培室"),
    SIMULATOR(HTMachineTier.ELITE, "Simulation Chamber", "シミュレーション室"),
    ;

    val energyUsage: Int get() = RagiumConfig.COMMON.machineEnergyUsage[this]!!.asInt

    override val blockHolder: HTBasicDeferredBlock<HTEntityBlock> by lazy { RagiumBlocks.MACHINES[this]!! }
    override val blockEntityHolder: HTDeferredBlockEntityType<HTBlockEntity> by lazy { RagiumBlockEntityTypes.MACHINES[this]!! }

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enPattern
        HTLanguageType.JA_JP -> jaPattern
    }

    override fun getSerializedName(): String = name.lowercase()
}
