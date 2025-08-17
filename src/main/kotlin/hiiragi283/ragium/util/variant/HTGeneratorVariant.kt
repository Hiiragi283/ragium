package hiiragi283.ragium.util.variant

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTLanguageType
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.registry.HTVariantKey
import hiiragi283.ragium.common.block.entity.dynamo.HTCombustionGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.dynamo.HTGeneratorBlockEntity
import hiiragi283.ragium.common.block.entity.dynamo.HTThermalGeneratorBlockEntity
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

enum class HTGeneratorVariant(
    factory: (BlockPos, BlockState) -> HTGeneratorBlockEntity,
    val energyRate: () -> Int,
    private val enUsPattern: String,
    private val jaJpPattern: String,
) : HTVariantKey.WithBE<HTGeneratorBlockEntity> {
    // Basic
    THERMAL(
        ::HTThermalGeneratorBlockEntity,
        RagiumAPI.getConfig()::getBasicMachineEnergyUsage,
        "Thermal",
        "火力",
    ),

    // Advanced,
    COMBUSTION(
        ::HTCombustionGeneratorBlockEntity,
        RagiumAPI.getConfig()::getAdvancedMachineEnergyUsage,
        "Combustion",
        "燃焼",
    ),
    ;

    override val blockEntityHolder: HTDeferredBlockEntityType<HTGeneratorBlockEntity> =
        RagiumBlockEntityTypes.registerTick("${serializedName}_generator", factory)

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> "$enUsPattern Generator"
        HTLanguageType.JA_JP -> "${jaJpPattern}発電機"
    }

    override fun getSerializedName(): String = name.lowercase()
}
