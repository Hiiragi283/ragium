package hiiragi283.ragium.config

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.attribute.HTFluidBlockAttribute
import hiiragi283.ragium.api.config.HTIntConfigValue
import hiiragi283.ragium.api.config.definePositiveInt
import hiiragi283.ragium.api.config.translation
import hiiragi283.ragium.common.text.RagiumCommonTranslation
import net.neoforged.neoforge.common.ModConfigSpec

class HTMachineConfig(
    val tankMap: Map<HTFluidBlockAttribute.TankType, HTIntConfigValue>,
    val capacity: HTIntConfigValue,
    val rate: HTIntConfigValue,
) {
    companion object {
        @JvmStatic
        private fun tankCapacity(builder: ModConfigSpec.Builder, type: HTFluidBlockAttribute.TankType, capacity: Int): HTIntConfigValue =
            builder
                .translation(type)
                .definePositiveInt("${type.serializedName}_tank_capacity", capacity)

        @JvmStatic
        private fun energyCapacity(builder: ModConfigSpec.Builder, rate: Int): HTIntConfigValue = builder
            .translation(RagiumCommonTranslation.CONFIG_ENERGY_CAPACITY)
            .definePositiveInt("energy_capacity", rate * 20 * 10 * 10)

        @JvmStatic
        private fun energyRate(builder: ModConfigSpec.Builder, rate: Int): HTIntConfigValue = builder
            .translation(RagiumCommonTranslation.CONFIG_ENERGY_RATE)
            .definePositiveInt("energy_rate", rate)

        @JvmStatic
        fun create(
            builder: ModConfigSpec.Builder,
            name: String,
            vararg pairs: Pair<HTFluidBlockAttribute.TankType, Int>,
            rate: Int = 16,
        ): HTMachineConfig {
            builder.translation("block.${RagiumAPI.MOD_ID}.$name").push(name)
            return HTMachineConfig(
                pairs.associate { (type: HTFluidBlockAttribute.TankType, capacity: Int) ->
                    type to tankCapacity(builder, type, capacity)
                },
                energyCapacity(builder, rate),
                energyRate(builder, rate),
            ).apply { builder.pop() }
        }

        @JvmStatic
        fun createSimple(
            builder: ModConfigSpec.Builder,
            name: String,
            vararg types: HTFluidBlockAttribute.TankType,
            rate: Int = 16,
        ): HTMachineConfig {
            builder.translation("block.${RagiumAPI.MOD_ID}.$name").push(name)
            return HTMachineConfig(
                types.associateWith { type: HTFluidBlockAttribute.TankType -> tankCapacity(builder, type, 8000) },
                energyCapacity(builder, rate),
                energyRate(builder, rate),
            ).apply { builder.pop() }
        }
    }
}
