package hiiragi283.ragium.config

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.config.HTBoolConfigValue
import hiiragi283.ragium.api.config.HTDoubleConfigValue
import hiiragi283.ragium.api.config.HTIntConfigValue
import hiiragi283.ragium.api.config.HTListConfigValue
import hiiragi283.ragium.api.config.definePositiveDouble
import hiiragi283.ragium.api.config.definePositiveInt
import hiiragi283.ragium.common.tier.HTCrateTier
import hiiragi283.ragium.common.tier.HTDrumTier
import hiiragi283.ragium.common.tier.HTMachineTier
import net.neoforged.neoforge.common.ModConfigSpec

class RagiumCommonConfig(builder: ModConfigSpec.Builder) {
    // Machine
    @JvmField
    val energyCapacity: Map<HTMachineTier, HTIntConfigValue>

    @JvmField
    val energyRate: Map<HTMachineTier, HTIntConfigValue>

    @JvmField
    val energyUsage: Map<HTMachineTier, HTIntConfigValue>

    // Generator
    @JvmField
    val generatorInputTankCapacity: HTIntConfigValue

    // Processor
    @JvmField
    val breweryTankCapacity: HTIntConfigValue

    @JvmField
    val crusherTankCapacity: HTIntConfigValue

    @JvmField
    val extractorTankCapacity: HTIntConfigValue

    @JvmField
    val melterTankCapacity: HTIntConfigValue

    @JvmField
    val mixerFirstInputTankCapacity: HTIntConfigValue

    @JvmField
    val mixerSecondInputTankCapacity: HTIntConfigValue

    @JvmField
    val mixerOutputTankCapacity: HTIntConfigValue

    @JvmField
    val planterTankCapacity: HTIntConfigValue

    @JvmField
    val refineryInputTankCapacity: HTIntConfigValue

    @JvmField
    val refineryOutputTankCapacity: HTIntConfigValue

    @JvmField
    val simulatorTankCapacity: HTIntConfigValue

    @JvmField
    val washerTankCapacity: HTIntConfigValue

    // Device
    @JvmField
    val deviceCollectorTankCapacity: HTIntConfigValue

    @JvmField
    val deviceCollectorEntityRange: HTDoubleConfigValue

    @JvmField
    val milkCollectorMultiplier: HTIntConfigValue

    // Crate
    @JvmField
    val crateCapacity: Map<HTCrateTier, HTIntConfigValue>

    // Drum
    @JvmField
    val drumCapacity: Map<HTDrumTier, HTIntConfigValue>

    @JvmField
    val expConversionRatio: HTIntConfigValue

    // Block
    @JvmField
    val crimsonSoilDamage: HTDoubleConfigValue

    @JvmField
    val spongeCakeFallDamage: HTDoubleConfigValue

    // Item
    @JvmField
    val basicMagnetRange: HTDoubleConfigValue

    @JvmField
    val advancedMagnetRange: HTDoubleConfigValue

    @JvmField
    val expBerriesValue: HTIntConfigValue

    @JvmField
    val teleportKeyCost: HTIntConfigValue

    // Recipe
    @JvmField
    val tagOutputPriority: HTListConfigValue<String>

    // Tooltip
    @JvmField
    val showFoodEffect: HTBoolConfigValue

    // World
    @JvmField
    val disableMilkCure: HTBoolConfigValue

    init {
        // Generator
        builder.push("generator")
        generatorInputTankCapacity = builder.definePositiveInt("tankCapacity", 8000)
        builder.pop()
        // Machine
        builder.push("machine")
        energyCapacity = HTMachineTier.entries.associateWith { tier: HTMachineTier ->
            val name: String = tier.name.lowercase()
            builder.push(name)
            // Energy Capacity
            val value: HTIntConfigValue = builder.definePositiveInt("energyCapacity", tier.batteryCapacity)
            builder.pop()
            value
        }
        energyRate = HTMachineTier.entries.associateWith { tier: HTMachineTier ->
            val name: String = tier.name.lowercase()
            builder.push(name)
            // Energy Rate
            val value: HTIntConfigValue = builder.definePositiveInt("energyRate", tier.generatorRate)
            builder.pop()
            value
        }
        energyUsage = HTMachineTier.entries.associateWith { tier: HTMachineTier ->
            val name: String = tier.name.lowercase()
            builder.push(name)
            // Energy Rate
            val value: HTIntConfigValue = builder.definePositiveInt("energyUsage", tier.processorRate)
            builder.pop()
            value
        }

        builder.push("brewery")
        breweryTankCapacity = builder.definePositiveInt("tankCapacity", 8000)
        builder.pop()

        builder.push("crusher")
        crusherTankCapacity = builder.definePositiveInt("tankCapacity", 8000)
        builder.pop()

        builder.push("extractor")
        extractorTankCapacity = builder.definePositiveInt("tankCapacity", 8000)
        builder.pop()

        builder.push("melter")
        melterTankCapacity = builder.definePositiveInt("tankCapacity", 8000)
        builder.pop()

        builder.push("mixer")
        builder.push("input")
        mixerFirstInputTankCapacity = builder.definePositiveInt("firstTankCapacity", 8000)
        mixerSecondInputTankCapacity = builder.definePositiveInt("secondTankCapacity", 8000)
        builder.pop()
        builder.push("output")
        mixerOutputTankCapacity = builder.definePositiveInt("tankCapacity", 8000)
        builder.pop(2)

        builder.push("planter")
        planterTankCapacity = builder.definePositiveInt("tankCapacity", 8000)
        builder.pop()

        builder.push("refinery")
        builder.push("input")
        refineryInputTankCapacity = builder.definePositiveInt("tankCapacity", 8000)
        builder.pop()
        builder.push("output")
        refineryOutputTankCapacity = builder.definePositiveInt("tankCapacity", 8000)
        builder.pop(2)

        builder.push("simulator")
        simulatorTankCapacity = builder.definePositiveInt("tankCapacity", 8000)
        builder.pop()

        builder.push("washer")
        washerTankCapacity = builder.definePositiveInt("tankCapacity", 8000)
        builder.pop()

        builder.pop()
        // Device
        builder.push("device")
        builder.push("collector")
        deviceCollectorTankCapacity = builder.definePositiveInt("tankCapacity", 8000)
        deviceCollectorEntityRange = builder.definePositiveDouble("entityRange", 5.0, 1, 16)
        builder.pop()

        builder.push("milk_collector")
        milkCollectorMultiplier = builder.definePositiveInt("multiplier", 20)
        builder.pop()

        builder.pop()
        // Crate
        builder.push("crate")
        crateCapacity = HTCrateTier.entries.associateWith { tier: HTCrateTier ->
            val name: String = tier.name.lowercase()
            builder.push(name)
            // Capacity
            val value: HTIntConfigValue = builder.definePositiveInt(
                "multiplier",
                when (tier) {
                    HTCrateTier.SMALL -> 32
                    HTCrateTier.MEDIUM -> 128
                    HTCrateTier.LARGE -> 512
                    HTCrateTier.HUGE -> 2048
                },
            )
            builder.pop()
            value
        }
        builder.pop()
        // Drum
        builder.push("drum")
        drumCapacity = HTDrumTier.entries.associateWith { variant: HTDrumTier ->
            val name: String = variant.name.lowercase()
            builder.push(name)
            // Capacity
            val value: HTIntConfigValue = builder.definePositiveInt(
                "capacity",
                when (variant) {
                    HTDrumTier.SMALL -> 16_000
                    HTDrumTier.MEDIUM -> 32_000
                    HTDrumTier.LARGE -> 64_000
                    HTDrumTier.HUGE -> 256_000
                    HTDrumTier.CREATIVE -> 1_000
                },
            )
            builder.pop()
            value
        }
        expConversionRatio = builder.definePositiveInt("expConversionRatio", 20)
        builder.pop()
        // Block
        builder.push("block")
        crimsonSoilDamage = builder.definePositiveDouble("crimsonSoilDamage", 2.0, 0, Int.MAX_VALUE)
        spongeCakeFallDamage = builder.definePositiveDouble("spongeCakeFallDamage", 0.0, 0, Int.MAX_VALUE)
        builder.pop()
        // Item
        builder.push("item")
        basicMagnetRange = builder.definePositiveDouble("basicMagnetRange", 5.0, 0, Int.MAX_VALUE)
        advancedMagnetRange = builder.definePositiveDouble("advancedMagnetRange", 8.0, 0, Int.MAX_VALUE)
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
                        RagiumConst.MEKANISM,
                        RagiumConst.IMMERSIVE,
                    ),
                    { "" },
                    { obj: Any -> obj is String },
                ).let(::HTListConfigValue)
        builder.pop()
        // Tooltip
        builder.push("world")
        showFoodEffect = HTBoolConfigValue(builder.define("showFoodEffect", true))
        builder.pop()
        // World
        builder.push("world")
        disableMilkCure = HTBoolConfigValue(builder.define("disableMilkCure", false))
        builder.pop()
    }
}
