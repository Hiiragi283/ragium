package hiiragi283.ragium.common.variant

import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.registry.impl.HTDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.variant.HTVariantKey
import hiiragi283.ragium.common.block.HTCrateBlock
import hiiragi283.ragium.common.block.entity.storage.HTCrateBlockEntity
import hiiragi283.ragium.common.item.block.HTCrateBlockItem
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumBlocks

enum class HTCrateVariant(private val enPattern: String, private val jaPattern: String) :
    HTVariantKey.WithBlockAndBE<HTCrateBlock, HTCrateBlockEntity> {
    SMALL("Small Crate", "クレート（小）"),
    MEDIUM("Medium Crate", "クレート（中）"),
    LARGE("Large Crate", "クレート（大）"),
    HUGE("Huge Crate", "クレート（特大）"),
    ;

    val multiplier: Int get() = RagiumConfig.COMMON.crateCapacity[this]!!.asInt

    override val blockHolder: HTDeferredBlock<HTCrateBlock, HTCrateBlockItem> by lazy { RagiumBlocks.CRATES[this]!! }
    override val blockEntityHolder: HTDeferredBlockEntityType<out HTCrateBlockEntity> by lazy { RagiumBlockEntityTypes.CRATES[this]!! }

    override fun translate(type: HTLanguageType, value: String): String = value.replace(
        "%s",
        when (type) {
            HTLanguageType.EN_US -> enPattern
            HTLanguageType.JA_JP -> jaPattern
        },
    )

    override fun variantName(): String = name.lowercase()
}
