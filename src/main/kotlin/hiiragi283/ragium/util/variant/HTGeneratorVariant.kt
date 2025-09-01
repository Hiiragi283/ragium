package hiiragi283.ragium.util.variant

import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.api.data.HTLanguageType
import hiiragi283.ragium.api.registry.HTBasicDeferredBlockHolder
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.registry.HTVariantKey
import hiiragi283.ragium.api.util.material.HTTierType
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTCombustionGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTSolarGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTThermalGeneratorBlockEntity
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

enum class HTGeneratorVariant(
    factory: (BlockPos, BlockState) -> HTBlockEntity,
    val tier: HTTierType,
    private val enUsPattern: String,
    private val jaJpPattern: String,
) : HTVariantKey.WithBE<HTBlockEntity> {
    // Basic
    THERMAL(::HTThermalGeneratorBlockEntity, HTTierType.BASIC, "Thermal", "火力"),

    // Advanced
    COMBUSTION(::HTCombustionGeneratorBlockEntity, HTTierType.ADVANCED, "Combustion", "燃焼"),
    SOLAR(::HTSolarGeneratorBlockEntity, HTTierType.ADVANCED, "Solar", "太陽光"),
    ;

    val energyRate: Int get() = RagiumConfig.CONFIG.generatorEnergyRate[this]!!.asInt

    override val blockHolder: HTBasicDeferredBlockHolder<HTEntityBlock> get() = RagiumBlocks.GENERATORS[this]!!
    override val blockEntityHolder: HTDeferredBlockEntityType<HTBlockEntity> =
        RagiumBlockEntityTypes.registerTick("${serializedName}_generator", factory)

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> "$enUsPattern Generator"
        HTLanguageType.JA_JP -> "${jaJpPattern}発電機"
    }

    override fun getSerializedName(): String = name.lowercase()
}
