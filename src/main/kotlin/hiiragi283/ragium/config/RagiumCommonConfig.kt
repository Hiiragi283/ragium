package hiiragi283.ragium.config

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.config.HTBoolConfigValue
import hiiragi283.ragium.api.config.HTDoubleConfigValue
import hiiragi283.ragium.api.config.HTIntConfigValue
import hiiragi283.ragium.api.config.HTListConfigValue
import hiiragi283.ragium.api.extension.definePositiveDouble
import hiiragi283.ragium.api.extension.definePositiveInt
import hiiragi283.ragium.common.variant.HTDeviceVariant
import hiiragi283.ragium.common.variant.HTGeneratorVariant
import hiiragi283.ragium.common.variant.HTMachineVariant
import net.neoforged.neoforge.common.ModConfigSpec

class RagiumCommonConfig(builder: ModConfigSpec.Builder) {
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

    // Item
    @JvmField
    val expBerriesValue: HTIntConfigValue

    @JvmField
    val teleportKeyCost: HTIntConfigValue

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
            val value: HTIntConfigValue = builder.definePositiveInt("energyRate", variant.tier.generatorRate)
            builder.pop()
            value
        }
        generatorInputTankCapacity = builder.definePositiveInt("tankCapacity", 8000)
        builder.pop()
        // Machine
        builder.push("machine")
        machineEnergyUsage = HTMachineVariant.entries.associateWith { variant: HTMachineVariant ->
            val name: String = variant.serializedName
            builder.push(name)
            // Energy Usage
            val value: HTIntConfigValue = builder.definePositiveInt("energyUsage", variant.tier.processorRate)
            builder.pop()
            value
        }

        builder.push("melter")
        melterTankCapacity = builder.definePositiveInt("tankCapacity", 8000)
        builder.pop()

        builder.push("refinery")
        builder.push("input")
        refineryInputTankCapacity = builder.definePositiveInt("tankCapacity", 8000)
        builder.pop()
        builder.push("output")
        refineryOutputTankCapacity = builder.definePositiveInt("tankCapacity", 8000)
        builder.pop(2)

        builder.pop()
        // Device
        builder.push("device")
        deviceTickRate = HTDeviceVariant.entries.associateWith { variant: HTDeviceVariant ->
            val name: String = variant.serializedName
            builder.push(name)
            val value: HTIntConfigValue = builder.definePositiveInt("tickRate", 20)
            builder.pop()
            value
        }
        builder.push("collector")
        deviceCollectorTankCapacity = builder.definePositiveInt("tankCapacity", 8000)
        deviceCollectorEntityRange = builder.definePositiveDouble("entityRange", 5.0, 1, 16)
        builder.pop()

        builder.push("exp_collector")
        expCollectorMultiplier = builder.definePositiveInt("multiplier", 20)
        builder.pop()

        builder.push("milk_collector")
        milkCollectorMultiplier = builder.definePositiveInt("multiplier", 20)
        builder.pop()

        builder.pop()
        // Drum
        builder.push("drum")
        builder.push("small")
        smallDrumCapacity = builder.definePositiveInt("capacity", 16_000)
        builder.pop()

        builder.push("medium")
        mediumDrumCapacity = builder.definePositiveInt("capacity", 32_000)
        builder.pop()

        builder.push("large")
        largeDrumCapacity = builder.definePositiveInt("capacity", 64_000)
        builder.pop()

        builder.push("huge")
        hugeDrumCapacity = builder.definePositiveInt("capacity", 256_000)
        builder.pop()
        builder.pop()
        // Item
        builder.push("item")
        expBerriesValue = builder.definePositiveInt("expBerriesValue", 8)
        teleportKeyCost = builder.definePositiveInt("teleportKeyCost", 10)
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
}
