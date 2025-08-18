package hiiragi283.ragium.util.variant

import hiiragi283.ragium.api.data.HTLanguageType
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.registry.HTVariantKey
import hiiragi283.ragium.common.block.entity.device.HTDeviceBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTDimensionalAnchorBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTEnergyNetworkAccessBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTExpCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTItemBufferBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTLavaCollectorBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTMilkDrainBlockEntity
import hiiragi283.ragium.common.block.entity.device.HTWaterCollectorBlockEntity
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.util.material.RagiumTierType
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.registries.DeferredBlock

enum class HTDeviceVariant(
    factory: (BlockPos, BlockState) -> HTDeviceBlockEntity,
    val tier: RagiumTierType,
    private val enUsPattern: String,
    private val jaJpPattern: String,
    private val customName: String? = null,
) : HTVariantKey.WithBE<HTDeviceBlockEntity> {
    // Basic
    ITEM_BUFFER(::HTItemBufferBlockEntity, RagiumTierType.BASIC, "Item Buffer", "アイテムバッファ"),
    MILK_COLLECTOR(::HTMilkDrainBlockEntity, RagiumTierType.BASIC, "Milk Collector", "搾乳機"),
    WATER_COLLECTOR(::HTWaterCollectorBlockEntity, RagiumTierType.BASIC, "Water Collector", "水収集機"),

    // Advanced
    ENI(HTEnergyNetworkAccessBlockEntity::Simple, RagiumTierType.ADVANCED, "E.N.I.", "E.N.I.", "energy_network_interface"),
    EXP_COLLECTOR(::HTExpCollectorBlockEntity, RagiumTierType.ADVANCED, "Exp Collector", "経験値収集機"),
    LAVA_COLLECTOR(::HTLavaCollectorBlockEntity, RagiumTierType.ADVANCED, "Lava Collector", "溶岩収集機"),
    DIM_ANCHOR(::HTDimensionalAnchorBlockEntity, RagiumTierType.ADVANCED, "Dimensional Anchor", "次元アンカー", "dimensional_anchor"),

    // Creative
    CEU(HTEnergyNetworkAccessBlockEntity::Creative, RagiumTierType.ULTIMATE, "C.E.U", "C.E.U", "creative_energy_unit"),
    ;

    override val blockHolder: DeferredBlock<*> get() = RagiumBlocks.DEVICES[this]!!
    override val blockEntityHolder: HTDeferredBlockEntityType<HTDeviceBlockEntity> =
        RagiumBlockEntityTypes.registerTick(serializedName, factory)

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enUsPattern
        HTLanguageType.JA_JP -> jaJpPattern
    }

    override fun getSerializedName(): String = customName ?: name.lowercase()
}
