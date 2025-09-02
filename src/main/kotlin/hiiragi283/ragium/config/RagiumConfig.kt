package hiiragi283.ragium.config

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.api.util.material.HTTierType
import hiiragi283.ragium.util.variant.HTDeviceVariant
import hiiragi283.ragium.util.variant.HTGeneratorVariant
import hiiragi283.ragium.util.variant.HTMachineVariant
import net.neoforged.neoforge.common.ModConfigSpec
import org.apache.commons.lang3.tuple.Pair

class RagiumConfig(builder: ModConfigSpec.Builder) {
    companion object {
        @JvmField
        val CONFIG_SPEC: ModConfigSpec

        @JvmField
        val CONFIG: RagiumConfig

        init {
            val configPair: Pair<RagiumConfig, ModConfigSpec> = ModConfigSpec.Builder().configure(::RagiumConfig)
            CONFIG_SPEC = configPair.right
            CONFIG = configPair.left
        }
    }

    // Generator
    @JvmField
    val generatorEnergyRate: Map<HTGeneratorVariant, HTIntConfigValue>

    @JvmField
    val generatorInputTankCapacity: HTIntConfigValue

    // Machine
    @JvmField
    val machineEnergyUsage: Map<HTMachineVariant, HTIntConfigValue>

    @JvmField
    val melterTankCapacity: HTIntConfigValue

    @JvmField
    val refineryInputTankCapacity: HTIntConfigValue

    @JvmField
    val refineryOutputTankCapacity: HTIntConfigValue

    // Device
    @JvmField
    val deviceTickRate: Map<HTDeviceVariant, HTIntConfigValue>

    @JvmField
    val deviceCollectorTankCapacity: HTIntConfigValue

    @JvmField
    val deviceCollectorEntityRange: HTDoubleConfigValue

    @JvmField
    val expCollectorMultiplier: HTIntConfigValue

    @JvmField
    val milkCollectorMultiplier: HTIntConfigValue

    // Drum
    @JvmField
    val smallDrumCapacity: HTIntConfigValue

    @JvmField
    val mediumDrumCapacity: HTIntConfigValue

    @JvmField
    val largeDrumCapacity: HTIntConfigValue

    @JvmField
    val hugeDrumCapacity: HTIntConfigValue

    // Recipe
    @JvmField
    val tagOutputPriority: HTListConfigValue<String>

    // World
    @JvmField
    val disableMilkCure: HTBoolConfigValue

    init {
        // Generator
        builder.push("generator")
        generatorEnergyRate = HTGeneratorVariant.entries.associateWith { variant: HTGeneratorVariant ->
            val name: String = variant.serializedName
            builder.push(name)
            // Energy Rate
            val defaultValue: Int = when (variant.tier) {
                HTTierType.BASIC -> 32
                HTTierType.ADVANCED -> 128
                HTTierType.ELITE -> 512
                else -> 2048
            }
            val value = HTIntConfigValue(builder.definePositiveInt("energyRate", defaultValue))
            builder.pop()
            value
        }
        generatorInputTankCapacity = HTIntConfigValue(builder.definePositiveInt("tankCapacity", 8000))
        builder.pop()
        // Machine
        builder.push("machine")
        machineEnergyUsage = HTMachineVariant.entries.associateWith { variant: HTMachineVariant ->
            val name: String = variant.serializedName
            builder.push(name)
            // Energy Usage
            val defaultValue: Int = when (variant.tier) {
                HTTierType.BASIC -> 16
                HTTierType.ADVANCED -> 64
                HTTierType.ELITE -> 256
                else -> 1024
            }
            val value = HTIntConfigValue(builder.definePositiveInt("energyUsage", defaultValue))
            builder.pop()
            value
        }

        builder.push("melter")
        melterTankCapacity = HTIntConfigValue(builder.definePositiveInt("tankCapacity", 8000))
        builder.pop()

        builder.push("refinery")
        builder.push("input")
        refineryInputTankCapacity = HTIntConfigValue(builder.definePositiveInt("tankCapacity", 8000))
        builder.pop()
        builder.push("output")
        refineryOutputTankCapacity = HTIntConfigValue(builder.definePositiveInt("tankCapacity", 8000))
        builder.pop(2)

        builder.pop()
        // Device
        builder.push("device")
        deviceTickRate = HTDeviceVariant.entries.associateWith { variant: HTDeviceVariant ->
            val name: String = variant.serializedName
            builder.push(name)
            val value = HTIntConfigValue(builder.definePositiveInt("tickRate", 20))
            builder.pop()
            value
        }
        builder.push("collector")
        deviceCollectorTankCapacity = HTIntConfigValue(builder.definePositiveInt("tankCapacity", 8000))
        deviceCollectorEntityRange = HTDoubleConfigValue(builder.definePositiveDouble("entityRange", 5.0, 1.0, 16.0))
        builder.pop()

        builder.push("exp_collector")
        expCollectorMultiplier = HTIntConfigValue(builder.definePositiveInt("multiplier", 20))
        builder.pop()

        builder.push("milk_collector")
        milkCollectorMultiplier = HTIntConfigValue(builder.definePositiveInt("multiplier", 20))
        builder.pop()

        builder.pop()
        // Drum
        builder.push("drum")
        builder.push("small")
        smallDrumCapacity = HTIntConfigValue(builder.definePositiveInt("capacity", 16_000))
        builder.pop()

        builder.push("medium")
        mediumDrumCapacity = HTIntConfigValue(builder.definePositiveInt("capacity", 32_000))
        builder.pop()

        builder.push("large")
        largeDrumCapacity = HTIntConfigValue(builder.definePositiveInt("capacity", 64_000))
        builder.pop()

        builder.push("huge")
        hugeDrumCapacity = HTIntConfigValue(builder.definePositiveInt("capacity", 256_000))
        builder.pop()
        builder.pop()
        // Recipe
        builder.push("recipe")
        tagOutputPriority =
            builder
                .worldRestart()
                .defineList(
                    "tagOutputModIds",
                    listOf(
                        RagiumAPI.MOD_ID,
                        RagiumConst.MINECRAFT,
                        "alltheores",
                    ),
                    { "" },
                    { obj: Any -> obj is String },
                ).let(::HTListConfigValue)
        builder.pop()
        // World
        builder.push("world")
        disableMilkCure = HTBoolConfigValue(builder.define("disableMilkCure", false))
        builder.pop()
    }

    //    Extension    //

    private fun ModConfigSpec.Builder.definePositiveInt(path: String, defaultValue: Int, min: Int = 1): ModConfigSpec.IntValue =
        defineInRange(path, defaultValue, min, Int.MAX_VALUE)

    private fun ModConfigSpec.Builder.definePositiveDouble(
        path: String,
        defaultValue: Double,
        min: Double,
        max: Double,
    ): ModConfigSpec.DoubleValue = defineInRange(path, defaultValue, min, max)
}
