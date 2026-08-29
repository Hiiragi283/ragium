package hiiragi283.ragium.api.config

import hiiragi283.lib.HTConstants
import hiiragi283.lib.config.definePositiveInt
import hiiragi283.lib.config.translation
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.text.RagiumTranslation
import java.util.function.IntSupplier
import net.neoforged.neoforge.common.ModConfigSpec

/**
 * 機械のエネルギーに関するコンフィグを管理するクラスです。
 * @param capacity エネルギーの容量
 * @author rate エネルギーの生産/消費速度
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
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
        ): HTEnergyConfig = create(builder, "${HTConstants.BLOCK}.${RagiumAPI.MOD_ID}.$name", name, usage, capacity)

        @JvmStatic
        fun createItem(
            builder: ModConfigSpec.Builder,
            name: String,
            usage: Int,
            capacity: Int,
        ): HTEnergyConfig = create(builder, "${HTConstants.ITEM}.${RagiumAPI.MOD_ID}.$name", name, usage, capacity)

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
