package hiiragi283.ragium.util.variant

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTLanguageType
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.registry.HTVariantKey
import hiiragi283.ragium.common.block.entity.HTDrumBlockEntity
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.registries.DeferredBlock

enum class HTDrumVariant(
    factory: (BlockPos, BlockState) -> HTDrumBlockEntity,
    private val enUsPattern: String,
    private val jaJpPattern: String,
) : HTVariantKey.WithBE<HTDrumBlockEntity> {
    SMALL(HTDrumBlockEntity::Small, "Small Drum", "ドラム（小）"),
    MEDIUM(HTDrumBlockEntity::Medium, "Medium Drum", "ドラム（中）"),
    LARGE(HTDrumBlockEntity::Large, "Large Drum", "ドラム（大）"),
    HUGE(HTDrumBlockEntity::Huge, "Huge Drum", "ドラム（特大）"),
    ;

    val capacity: Int get() = RagiumAPI.getConfig().getDrumCapacity(serializedName)

    override val blockHolder: DeferredBlock<*> get() = RagiumBlocks.DRUMS[this]!!
    override val blockEntityHolder: HTDeferredBlockEntityType<HTDrumBlockEntity> =
        RagiumBlockEntityTypes.REGISTER.registerType("${serializedName}_drum", factory)

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enUsPattern
        HTLanguageType.JA_JP -> jaJpPattern
    }

    override fun getSerializedName(): String = name.lowercase()
}
