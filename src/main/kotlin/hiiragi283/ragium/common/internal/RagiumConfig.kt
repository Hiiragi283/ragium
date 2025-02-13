package hiiragi283.ragium.common.internal

import hiiragi283.ragium.api.material.HTMaterialKey
import net.neoforged.neoforge.common.ModConfigSpec

@Suppress("DEPRECATION")
internal object RagiumConfig {
    @JvmField
    val SPEC: ModConfigSpec

    //    Durability    //

    @JvmField
    val FORGE_HAMMER_DURABILITY: ModConfigSpec.IntValue

    @JvmField
    val SOAP_DURABILITY: ModConfigSpec.IntValue

    //    Machine    //

    @JvmField
    val MACHINE_TANK_CAPACITY: ModConfigSpec.IntValue

    @JvmField
    val MACHINE_SOUND: ModConfigSpec.DoubleValue

    @JvmField
    val STIRLING_WATER_MODIFIER: ModConfigSpec.IntValue

    @JvmStatic
    fun getStirlingWater(burnTime: Int): Int {
        val value: Int = STIRLING_WATER_MODIFIER.get()
        return when {
            value > 0 -> burnTime * value
            value < 0 -> burnTime / value
            else -> 0
        }
    }

    @JvmField
    val STIRLING_ASH_MODIFIER: ModConfigSpec.IntValue

    @JvmStatic
    fun getStirlingAsh(burnTime: Int): Int {
        val value: Int = STIRLING_ASH_MODIFIER.get()
        return when {
            value > 0 -> burnTime * value
            value < 0 -> burnTime / value
            else -> 0
        }
    }

    @JvmField
    val STIRLING_ENERGY_MODIFIER: ModConfigSpec.IntValue

    @JvmStatic
    fun getStirlingEnergy(burnTime: Int): Int {
        val value: Int = STIRLING_ASH_MODIFIER.get()
        return when {
            value > 0 -> burnTime * value
            value < 0 -> burnTime / value
            else -> 0
        }
    }

    //    Material    //

    @JvmField
    val GRINDER_RAW_COUNT: ModConfigSpec.ConfigValue<List<String>>

    @JvmStatic
    fun getGrinderRawCountMap(): Map<HTMaterialKey, Int> = GRINDER_RAW_COUNT
        .get()
        .map { it.split('=', limit = 2) }
        .associate { HTMaterialKey.of(it[0]) to it[1].toInt() }

    //    Misc    //

    @JvmField
    val DYNAMITE_POWER: ModConfigSpec.DoubleValue

    init {
        val builder = ModConfigSpec.Builder()

        // Durability
        builder.push("durability")
        FORGE_HAMMER_DURABILITY = builder
            .gameRestart()
            .defineInRange("forgeHammer", 63, 1, Int.MAX_VALUE)

        SOAP_DURABILITY = builder
            .gameRestart()
            .defineInRange("soap", 63, 1, Int.MAX_VALUE)
        builder.pop()

        // Machine
        builder.push("machine")
        MACHINE_TANK_CAPACITY = builder.defineInRange("tankCapacity", 8000, 1000, 64000)
        MACHINE_SOUND = builder.defineInRange("soundVolume", 0.2, 0.0, 1.0)

        builder
            .comment("Positive value is regarded as multiplier", "Negative value is regarded as division")
            .push("stirling")
        STIRLING_ASH_MODIFIER =
            builder.worldRestart().defineInRange("ashModifier", -200, Int.MIN_VALUE, Int.MAX_VALUE)
        STIRLING_ENERGY_MODIFIER =
            builder.worldRestart().defineInRange("energyModifier", 10, Int.MIN_VALUE, Int.MAX_VALUE)
        STIRLING_WATER_MODIFIER =
            builder.worldRestart().defineInRange("waterModifier", -10, Int.MIN_VALUE, Int.MAX_VALUE)
        builder.pop(2)

        // Material
        builder.push("material")
        GRINDER_RAW_COUNT = builder
            .worldRestart()
            .defineList(
                "grinderRawCountMap",
                listOf(
                    "lapis=4",
                    "redstone=2",
                ),
            ) { true }
        builder.pop()

        // Misc
        builder.push("misc")
        DYNAMITE_POWER = builder.defineInRange("dynamitePower", 2.0, 0.0, 16.0)
        builder.pop()

        SPEC = builder.build()
    }
}
