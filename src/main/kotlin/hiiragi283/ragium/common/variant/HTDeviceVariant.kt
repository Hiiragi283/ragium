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
import java.util.function.IntSupplier

enum class HTDeviceVariant(
    val tier: HTMachineTier,
    private val enUsPattern: String,
    private val jaJpPattern: String,
    private val customName: String? = null,
) : HTVariantKey.WithBE<HTBlockEntity> {
    // Basic
    ITEM_BUFFER(HTMachineTier.BASIC, "Item Buffer", "アイテムバッファ"),
    MILK_COLLECTOR(HTMachineTier.BASIC, "Milk Collector", "搾乳機"),
    WATER_COLLECTOR(HTMachineTier.BASIC, "Water Collector", "水収集機"),

    // Advanced
    ENI(HTMachineTier.ADVANCED, "E.N.I.", "E.N.I.", "energy_network_interface"),
    EXP_COLLECTOR(HTMachineTier.ADVANCED, "Exp Collector", "経験値収集機"),
    LAVA_COLLECTOR(HTMachineTier.ADVANCED, "Lava Collector", "溶岩収集機"),

    // Elite
    DIM_ANCHOR(HTMachineTier.ELITE, "Dimensional Anchor", "次元アンカー", "dimensional_anchor"),
    TELEPAD(HTMachineTier.ELITE, "Telepad", "テレパッド"),
    MOB_CAPTURER(HTMachineTier.ELITE, "Mob Capturer", "モブ捕獲機"),

    // Creative
    CEU(HTMachineTier.CREATIVE, "C.E.U", "C.E.U", "creative_energy_unit"),
    ;

    val tickRate: IntSupplier get() = RagiumConfig.COMMON.deviceTickRate[this]!!

    override val blockHolder: HTBasicDeferredBlock<HTEntityBlock> by lazy { RagiumBlocks.DEVICES[this]!! }
    override val blockEntityHolder: HTDeferredBlockEntityType<HTBlockEntity> by lazy { RagiumBlockEntityTypes.DEVICES[this]!! }

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enUsPattern
        HTLanguageType.JA_JP -> jaJpPattern
    }

    override fun variantName(): String = customName ?: name.lowercase()
}
