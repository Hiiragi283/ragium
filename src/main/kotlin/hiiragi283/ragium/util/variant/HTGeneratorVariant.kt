package hiiragi283.ragium.util.variant

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTLanguageType
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.registry.HTVariantKey
import hiiragi283.ragium.common.block.entity.dynamo.HTCombustionGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.dynamo.HTGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.dynamo.HTSolarGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.dynamo.HTThermalGeneratorBlockEntity
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.util.material.RagiumTierType
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.registries.DeferredBlock

enum class HTGeneratorVariant(
    factory: (BlockPos, BlockState) -> HTGeneratorBlockEntity,
    val tier: RagiumTierType,
    private val enUsPattern: String,
    private val jaJpPattern: String,
) : HTVariantKey.WithBE<HTGeneratorBlockEntity> {
    // Basic
    THERMAL(::HTThermalGeneratorBlockEntity, RagiumTierType.BASIC, "Thermal", "火力"),

    // Advanced,
    COMBUSTION(::HTCombustionGeneratorBlockEntity, RagiumTierType.ADVANCED, "Combustion", "燃焼"),
    SOLAR(::HTSolarGeneratorBlockEntity, RagiumTierType.ADVANCED, "Solar", "太陽光"),
    ;

    val energyRate: Int get() = RagiumAPI.getConfig().getGeneratorEnergyRate(serializedName)
    val tankCapacity: Int get() = RagiumAPI.getConfig().getMachineTankCapacity(serializedName)

    override val blockHolder: DeferredBlock<*> get() = RagiumBlocks.GENERATORS[this]!!
    override val blockEntityHolder: HTDeferredBlockEntityType<HTGeneratorBlockEntity> =
        RagiumBlockEntityTypes.registerTick("${serializedName}_generator", factory)

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> "$enUsPattern Generator"
        HTLanguageType.JA_JP -> "${jaJpPattern}発電機"
    }

    override fun getSerializedName(): String = name.lowercase()
}
