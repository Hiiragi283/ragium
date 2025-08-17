package hiiragi283.ragium.util.variant

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTLanguageType
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.registry.HTVariantKey
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTAlloySmelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTBlockBreakerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCompressorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTCrusherBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTEngraverBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTExtractorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTInfuserBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTMelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTMixerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTPulverizerBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTRefineryBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTSmelterBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTSolidifierBlockEntity
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.util.material.RagiumTierType
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.registries.DeferredBlock

enum class HTMachineVariant(
    factory: (BlockPos, BlockState) -> HTMachineBlockEntity,
    val tier: RagiumTierType,
    private val enUsPattern: String,
    private val jaJpPattern: String,
) : HTVariantKey.WithBE<HTMachineBlockEntity> {
    // Basic
    BLOCK_BREAKER(::HTBlockBreakerBlockEntity, RagiumTierType.BASIC, "Block Breaker", "採掘機"),
    COMPRESSOR(::HTCompressorBlockEntity, RagiumTierType.BASIC, "Compressor", "圧縮機"),
    ENGRAVER(::HTEngraverBlockEntity, RagiumTierType.BASIC, "Engraver", "彫刻機"),
    EXTRACTOR(::HTExtractorBlockEntity, RagiumTierType.BASIC, "Extractor", "抽出機"),
    PULVERIZER(::HTPulverizerBlockEntity, RagiumTierType.BASIC, "Pulverizer", "粉砕機"),
    SMELTER(::HTSmelterBlockEntity, RagiumTierType.BASIC, "Smelter", "精錬炉"),

    // Advanced
    ALLOY_SMELTER(::HTAlloySmelterBlockEntity, RagiumTierType.ADVANCED, "Alloy Smelter", "合金炉"),
    CRUSHER(::HTCrusherBlockEntity, RagiumTierType.ADVANCED, "Crusher", "破砕機"),
    INFUSER(::HTInfuserBlockEntity, RagiumTierType.ADVANCED, "Infuser", "注入機"),
    MELTER(::HTMelterBlockEntity, RagiumTierType.ADVANCED, "Melter", "溶融炉"),
    MIXER(::HTMixerBlockEntity, RagiumTierType.ADVANCED, "Mixer", "混合機"),
    REFINERY(::HTRefineryBlockEntity, RagiumTierType.ADVANCED, "Refinery", "精製機"),
    SOLIDIFIER(::HTSolidifierBlockEntity, RagiumTierType.ADVANCED, "Solidifier", "成型機"),
    ;

    val energyUsage: Int get() = RagiumAPI.getConfig().getProcessorEnergyUsage(serializedName)
    val tankCapacity: Int get() = RagiumAPI.getConfig().getMachineTankCapacity(serializedName)

    override val blockHolder: DeferredBlock<*> get() = RagiumBlocks.MACHINES[this]!!
    override val blockEntityHolder: HTDeferredBlockEntityType<HTMachineBlockEntity> =
        RagiumBlockEntityTypes.registerTick(serializedName, factory)

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enUsPattern
        HTLanguageType.JA_JP -> jaJpPattern
    }

    override fun getSerializedName(): String = name.lowercase()
}
