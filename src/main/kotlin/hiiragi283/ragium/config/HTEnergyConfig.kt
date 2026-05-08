package hiiragi283.ragium.config

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.config.definePositiveInt
import hiiragi283.core.api.config.translation
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.text.RagiumTranslation
import net.neoforged.neoforge.common.ModConfigSpec
import java.util.function.IntSupplier

@JvmRecord
data class HTEnergyConfig(private val capacity: IntSupplier, private val rate: IntSupplier) {
    fun getCapacity(): Int = capacity.asInt

    fun getUsage(): Int = rate.asInt

    companion object {
        @JvmStatic
        private fun energyCapacity(builder: ModConfigSpec.Builder, value: Int): ModConfigSpec.IntValue = builder
            .translation(RagiumTranslation.CONFIG_ENERGY_CAPACITY)
            .definePositiveInt("energy_capacity", value)

        @JvmStatic
        private fun energyRate(builder: ModConfigSpec.Builder, value: Int): ModConfigSpec.IntValue = builder
            .translation(RagiumTranslation.CONFIG_ENERGY_RATE)
            .definePositiveInt("energy_rate", value)

        @JvmStatic
        fun createMachine(builder: ModConfigSpec.Builder, name: String, rate: Int = 16): HTEnergyConfig = createBlock(builder, name, rate, rate * 20 * 10 * 10)

        @JvmStatic
        fun createBlock(
            builder: ModConfigSpec.Builder,
            name: String,
            usage: Int,
            capacity: Int,
        ): HTEnergyConfig = create(builder, "${HTConst.BLOCK}.${RagiumAPI.MOD_ID}.$name", name, usage, capacity)

        @JvmStatic
        fun createItem(
            builder: ModConfigSpec.Builder,
            name: String,
            usage: Int,
            capacity: Int,
        ): HTEnergyConfig = create(builder, "${HTConst.ITEM}.${RagiumAPI.MOD_ID}.$name", name, usage, capacity)

        @JvmStatic
        fun create(
            builder: ModConfigSpec.Builder,
            translationKey: String,
            name: String,
            usage: Int,
            capacity: Int,
        ): HTEnergyConfig {
            builder.translation(translationKey).push(name)
            val config = HTEnergyConfig(energyCapacity(builder, capacity), energyRate(builder, usage))
            builder.pop()
            return config
        }
    }
}
