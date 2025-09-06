package hiiragi283.ragium.common.variant

import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.registry.impl.HTBasicDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.variant.HTVariantKey
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTAlloySmelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTBlockBreakerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCompressorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCrusherBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTEngraverBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTExtractorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTMelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTMultiSmelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTPulverizerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTRefineryBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTSimulatorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTSmelterBlockEntity
import hiiragi283.ragium.common.material.HTTierType
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

enum class HTMachineVariant(
    factory: (BlockPos, BlockState) -> HTBlockEntity,
    val tier: HTTierType,
    private val enUsPattern: String,
    private val jaJpPattern: String,
) : HTVariantKey.WithBE<HTBlockEntity> {
    // Basic
    BLOCK_BREAKER(::HTBlockBreakerBlockEntity, HTTierType.BASIC, "Block Breaker", "採掘機"),
    COMPRESSOR(::HTCompressorBlockEntity, HTTierType.BASIC, "Compressor", "圧縮機"),
    ENGRAVER(::HTEngraverBlockEntity, HTTierType.BASIC, "Engraver", "彫刻機"),
    EXTRACTOR(::HTExtractorBlockEntity, HTTierType.BASIC, "Extractor", "抽出機"),
    PULVERIZER(::HTPulverizerBlockEntity, HTTierType.BASIC, "Pulverizer", "粉砕機"),
    SMELTER(::HTSmelterBlockEntity, HTTierType.BASIC, "Smelter", "精錬炉"),

    // Advanced
    ALLOY_SMELTER(::HTAlloySmelterBlockEntity, HTTierType.ADVANCED, "Alloy Smelter", "合金炉"),
    CRUSHER(::HTCrusherBlockEntity, HTTierType.ADVANCED, "Crusher", "破砕機"),
    MELTER(::HTMelterBlockEntity, HTTierType.ADVANCED, "Melter", "溶融炉"),
    REFINERY(::HTRefineryBlockEntity, HTTierType.ADVANCED, "Refinery", "精製機"),

    // Elite
    MULTI_SMELTER(::HTMultiSmelterBlockEntity, HTTierType.ELITE, "Multi Smelter", "並列製錬炉"),
    SIMULATOR(::HTSimulatorBlockEntity, HTTierType.ELITE, "Simulation Chamber", "シミュレーション室"),
    ;

    val energyUsage: Int get() = RagiumConfig.CONFIG.machineEnergyUsage[this]!!.asInt

    override val blockHolder: HTBasicDeferredBlock<HTEntityBlock> get() = RagiumBlocks.MACHINES[this]!!
    override val blockEntityHolder: HTDeferredBlockEntityType<HTBlockEntity> =
        RagiumBlockEntityTypes.registerTick(serializedName, factory)

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enUsPattern
        HTLanguageType.JA_JP -> jaJpPattern
    }

    override fun getSerializedName(): String = name.lowercase()
}
