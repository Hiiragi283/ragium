package hiiragi283.ragium.config

import hiiragi283.core.api.config.definePositiveInt
import hiiragi283.core.api.config.translation
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.text.RagiumTranslation
import net.neoforged.neoforge.common.ModConfigSpec
import java.util.function.IntSupplier

class HTMachineConfig(
    val tankMap: Map<RagiumFluidConfigType, IntSupplier>,
    private val capacity: IntSupplier,
    private val rate: IntSupplier,
) {
    fun getCapacity(): Int = capacity.asInt

    fun getUsage(): Int = rate.asInt

    companion object {
        @JvmStatic
        private fun tankCapacity(builder: ModConfigSpec.Builder, type: RagiumFluidConfigType, capacity: Int): ModConfigSpec.IntValue =
            builder
                .translation(type)
                .definePositiveInt("${type.serializedName}_tank_capacity", capacity)

        @JvmStatic
        private fun energyCapacity(builder: ModConfigSpec.Builder, rate: Int): ModConfigSpec.IntValue = builder
            .translation(RagiumTranslation.CONFIG_ENERGY_CAPACITY)
            .definePositiveInt("energy_capacity", rate * 20 * 10 * 10)

        @JvmStatic
        private fun energyRate(builder: ModConfigSpec.Builder, rate: Int): ModConfigSpec.IntValue = builder
            .translation(RagiumTranslation.CONFIG_ENERGY_RATE)
            .definePositiveInt("energy_rate", rate)

        @JvmStatic
        fun create(
            builder: ModConfigSpec.Builder,
            name: String,
            vararg pairs: Pair<RagiumFluidConfigType, Int>,
            rate: Int = 16,
        ): HTMachineConfig {
            builder.translation("block.${RagiumAPI.MOD_ID}.$name").push(name)
            return HTMachineConfig(
                pairs.associate { (type: RagiumFluidConfigType, capacity: Int) ->
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
            vararg types: RagiumFluidConfigType,
            rate: Int = 16,
        ): HTMachineConfig {
            builder.translation("block.${RagiumAPI.MOD_ID}.$name").push(name)
            return HTMachineConfig(
                types.associateWith { type: RagiumFluidConfigType -> tankCapacity(builder, type, 8000) },
                energyCapacity(builder, rate),
                energyRate(builder, rate),
            ).apply { builder.pop() }
        }
    }
}
