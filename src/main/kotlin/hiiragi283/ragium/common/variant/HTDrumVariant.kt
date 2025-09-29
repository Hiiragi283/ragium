package hiiragi283.ragium.common.variant

import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.registry.impl.HTDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.variant.HTVariantKey
import hiiragi283.ragium.common.block.HTDrumBlock
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.block.entity.HTDrumBlockEntity
import hiiragi283.ragium.common.item.block.HTDrumItem
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

enum class HTDrumVariant(factory: (BlockPos, BlockState) -> HTBlockEntity, private val enPattern: String, private val jaPattern: String) :
    HTVariantKey.WithBE<HTBlockEntity> {
    SMALL(HTDrumBlockEntity::Small, "Small Drum", "ドラム（小）"),
    MEDIUM(HTDrumBlockEntity::Medium, "Medium Drum", "ドラム（中）"),
    LARGE(HTDrumBlockEntity::Large, "Large Drum", "ドラム（大）"),
    HUGE(HTDrumBlockEntity::Huge, "Huge Drum", "ドラム（特大）"),
    ;

    override val blockHolder: HTDeferredBlock<HTDrumBlock, HTDrumItem> get() = RagiumBlocks.DRUMS[this]!!
    override val blockEntityHolder: HTDeferredBlockEntityType<HTBlockEntity> =
        RagiumBlockEntityTypes.registerTick("${serializedName}_drum", factory)

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enPattern
        HTLanguageType.JA_JP -> jaPattern
    }

    override fun getSerializedName(): String = name.lowercase()
}
