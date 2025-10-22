package hiiragi283.ragium.common.variant

import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.registry.impl.HTDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.registry.impl.HTDeferredEntityType
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredItem
import hiiragi283.ragium.api.variant.HTVariantKey
import hiiragi283.ragium.common.block.HTDrumBlock
import hiiragi283.ragium.common.block.entity.HTDrumBlockEntity
import hiiragi283.ragium.common.entity.vehicle.HTDrumMinecart
import hiiragi283.ragium.common.item.block.HTDrumItem
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumEntityTypes
import hiiragi283.ragium.setup.RagiumItems

enum class HTDrumVariant(private val enPattern: String, private val jaPattern: String) :
    HTVariantKey.WithBlock<HTDrumBlock>,
    HTVariantKey.WithBE<HTDrumBlockEntity>,
    HTVariantKey.WithEntity<HTDrumMinecart> {
    SMALL("Small Drum", "ドラム（小）"),
    MEDIUM("Medium Drum", "ドラム（中）"),
    LARGE("Large Drum", "ドラム（大）"),
    HUGE("Huge Drum", "ドラム（特大）"),
    ;

    override val blockHolder: HTDeferredBlock<HTDrumBlock, HTDrumItem> by lazy { RagiumBlocks.DRUMS[this]!! }
    override val blockEntityHolder: HTDeferredBlockEntityType<HTDrumBlockEntity> by lazy { RagiumBlockEntityTypes.DRUMS[this]!! }
    override val entityHolder: HTDeferredEntityType<HTDrumMinecart> by lazy { RagiumEntityTypes.DRUMS[this]!! }
    val minecartItem: HTSimpleDeferredItem by lazy { RagiumItems.DRUM_MINECARTS[this]!! }

    override fun translate(type: HTLanguageType, value: String): String = value.replace(
        "%s",
        when (type) {
            HTLanguageType.EN_US -> enPattern
            HTLanguageType.JA_JP -> jaPattern
        },
    )

    override fun variantName(): String = name.lowercase()
}
