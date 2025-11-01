package hiiragi283.ragium.common.tier

import hiiragi283.ragium.api.block.type.HTEntityBlockType
import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.data.lang.HTTranslationProvider
import hiiragi283.ragium.api.registry.impl.HTDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.registry.impl.HTDeferredEntityType
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredItem
import hiiragi283.ragium.api.tier.HTBaseTier
import hiiragi283.ragium.api.tier.HTTierProvider
import hiiragi283.ragium.common.block.entity.storage.HTDrumBlockEntity
import hiiragi283.ragium.common.block.storage.HTDrumBlock
import hiiragi283.ragium.common.entity.vehicle.HTDrumMinecart
import hiiragi283.ragium.common.entity.vehicle.HTMinecart
import hiiragi283.ragium.common.item.block.HTDrumBlockItem
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumBlockTypes
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumEntityTypes
import hiiragi283.ragium.setup.RagiumItems

enum class HTDrumTier(private val enPattern: String, private val jaPattern: String) :
    HTTierProvider,
    HTTranslationProvider {
    SMALL("Small Drum", "小型ドラム"),
    MEDIUM("Medium Drum", "中型ドラム"),
    LARGE("Large Drum", "大型ドラム"),
    HUGE("Huge Drum", "特大型ドラム"),
    CREATIVE("Creative Drum", "クリエイティブ用ドラム"),
    ;

    val path = "${name.lowercase()}_drum"
    val entityPath = "${name.lowercase()}_drum_minecart"
    val capacity: Int get() = RagiumConfig.COMMON.drumCapacity[this]!!.asInt

    fun getDefaultCapacity(): Int = RagiumConfig.COMMON.drumCapacity[this]!!.asInt

    fun getBlock(): HTDeferredBlock<HTDrumBlock, HTDrumBlockItem> = RagiumBlocks.DRUMS[this]!!

    fun getBlockType(): HTEntityBlockType = RagiumBlockTypes.DRUMS[this]!!

    fun getBlockEntityType(): HTDeferredBlockEntityType<HTDrumBlockEntity> = RagiumBlockEntityTypes.DRUMS[this]!!

    fun getEntityType(): HTDeferredEntityType<HTDrumMinecart> = RagiumEntityTypes.DRUMS[this]!!

    fun getMinecartFactory(): HTMinecart.Factory = when (this) {
        SMALL -> HTMinecart.Factory(HTDrumMinecart::Small)
        MEDIUM -> HTMinecart.Factory(HTDrumMinecart::Medium)
        LARGE -> HTMinecart.Factory(HTDrumMinecart::Large)
        HUGE -> HTMinecart.Factory(HTDrumMinecart::Huge)
        CREATIVE -> HTMinecart.Factory(HTDrumMinecart::Creative)
    }

    fun getMinecartItem(): HTSimpleDeferredItem = RagiumItems.DRUM_MINECARTS[this]!!

    //    HTTierProvider    //

    override fun getBaseTier(): HTBaseTier = when (this) {
        SMALL -> HTBaseTier.BASIC
        MEDIUM -> HTBaseTier.ADVANCED
        LARGE -> HTBaseTier.ELITE
        HUGE -> HTBaseTier.ULTIMATE
        CREATIVE -> HTBaseTier.CREATIVE
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
