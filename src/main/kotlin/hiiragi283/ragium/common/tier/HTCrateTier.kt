package hiiragi283.ragium.common.tier

import hiiragi283.ragium.api.block.type.HTEntityBlockType
import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.data.lang.HTTranslationProvider
import hiiragi283.ragium.api.registry.impl.HTDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.tier.HTBaseTier
import hiiragi283.ragium.api.tier.HTTierProvider
import hiiragi283.ragium.common.block.HTCrateBlock
import hiiragi283.ragium.common.block.entity.storage.HTCrateBlockEntity
import hiiragi283.ragium.common.item.block.HTCrateBlockItem
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumBlockTypes
import hiiragi283.ragium.setup.RagiumBlocks

enum class HTCrateTier(private val enPattern: String, private val jaPattern: String) :
    HTTierProvider,
    HTTranslationProvider {
    SMALL("Small Crate", "小型クレート"),
    MEDIUM("Medium Crate", "中型クレート"),
    LARGE("Large Crate", "大型クレート"),
    HUGE("Huge Crate", "特型クレート大"),
    ;

    val path = "${name.lowercase()}_crate"

    fun getMultiplier(): Int = RagiumConfig.COMMON.crateCapacity[this]!!.asInt

    fun getBlock(): HTDeferredBlock<HTCrateBlock, HTCrateBlockItem> = RagiumBlocks.CRATES[this]!!

    fun getBlockType(): HTEntityBlockType = RagiumBlockTypes.CRATES[this]!!

    fun getBlockEntityType(): HTDeferredBlockEntityType<HTCrateBlockEntity> = RagiumBlockEntityTypes.CRATES[this]!!

    //    HTTierProvider    //

    override fun getBaseTier(): HTBaseTier = when (this) {
        SMALL -> HTBaseTier.BASIC
        MEDIUM -> HTBaseTier.ADVANCED
        LARGE -> HTBaseTier.ELITE
        HUGE -> HTBaseTier.ULTIMATE
    }

    //    HTTranslationProvider    //

    override fun translate(type: HTLanguageType, value: String): String = value.replace(
        "%s",
        when (type) {
            HTLanguageType.EN_US -> enPattern
            HTLanguageType.JA_JP -> jaPattern
        },
    )
}
