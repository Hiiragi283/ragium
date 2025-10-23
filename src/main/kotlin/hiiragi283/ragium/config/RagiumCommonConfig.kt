package hiiragi283.ragium.config

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.config.HTBoolConfigValue
import hiiragi283.ragium.api.config.HTDoubleConfigValue
import hiiragi283.ragium.api.config.HTIntConfigValue
import hiiragi283.ragium.api.config.HTListConfigValue
import hiiragi283.ragium.api.config.definePositiveDouble
import hiiragi283.ragium.api.config.definePositiveInt
import hiiragi283.ragium.common.variant.HTCrateVariant
import hiiragi283.ragium.common.variant.HTDeviceVariant
import hiiragi283.ragium.common.variant.HTDrumVariant
import hiiragi283.ragium.common.variant.HTGeneratorVariant
import hiiragi283.ragium.common.variant.HTMachineVariant
import net.neoforged.neoforge.common.ModConfigSpec

class RagiumCommonConfig(builder: ModConfigSpec.Builder) {
    // Generator
    @JvmField
    val generatorEnergyRate: Map<HTGeneratorVariant<*, *>, HTIntConfigValue>

    @JvmField
    val generatorInputTankCapacity: HTIntConfigValue

    // Machine
    @JvmField
    val machineEnergyUsage: Map<HTMachineVariant, HTIntConfigValue>

    @JvmField
    val breweryTankCapacity: HTIntConfigValue

    @JvmField
    val crusherTankCapacity: HTIntConfigValue

    @JvmField
    val melterTankCapacity: HTIntConfigValue

    @JvmField
    val planterTankCapacity: HTIntConfigValue

    @JvmField
    val refineryInputTankCapacity: HTIntConfigValue

    @JvmField
    val refineryOutputTankCapacity: HTIntConfigValue

    @JvmField
    val washerTankCapacity: HTIntConfigValue

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

    // Crate
    @JvmField
    val crateCapacity: Map<HTCrateVariant, HTIntConfigValue>

    // Drum
    @JvmField
    val drumCapacity: Map<HTDrumVariant, HTIntConfigValue>

    // Block
    @JvmField
    val crimsonSoilDamage: HTDoubleConfigValue

    @JvmField
    val spongeCakeFallDamage: HTDoubleConfigValue

    // Item
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
        generatorEnergyRate = HTGeneratorVariant.entries.associateWith { variant: HTGeneratorVariant<*, *> ->
            val name: String = variant.variantName()
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
            val name: String = variant.variantName()
            builder.push(name)
            // Energy Usage
            val value: HTIntConfigValue = builder.definePositiveInt("energyUsage", variant.tier.processorRate)
            builder.pop()
            value
        }

        builder.push(HTMachineVariant.BREWERY.variantName())
        breweryTankCapacity = builder.definePositiveInt("tankCapacity", 8000)
        builder.pop()

        builder.push(HTMachineVariant.CRUSHER.variantName())
        crusherTankCapacity = builder.definePositiveInt("tankCapacity", 8000)
        builder.pop()

        builder.push(HTMachineVariant.MELTER.variantName())
        melterTankCapacity = builder.definePositiveInt("tankCapacity", 8000)
        builder.pop()

        builder.push(HTMachineVariant.PLANTER.variantName())
        planterTankCapacity = builder.definePositiveInt("tankCapacity", 8000)
        builder.pop()

        builder.push(HTMachineVariant.REFINERY.variantName())
        builder.push("input")
        refineryInputTankCapacity = builder.definePositiveInt("tankCapacity", 8000)
        builder.pop()
        builder.push("output")
        refineryOutputTankCapacity = builder.definePositiveInt("tankCapacity", 8000)
        builder.pop(2)

        builder.push(HTMachineVariant.WASHER.variantName())
        washerTankCapacity = builder.definePositiveInt("tankCapacity", 8000)
        builder.pop()

        builder.pop()
        // Device
        builder.push("device")
        deviceTickRate = HTDeviceVariant.entries.associateWith { variant: HTDeviceVariant ->
            val name: String = variant.variantName()
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
        // Crate
        builder.push("crate")
        crateCapacity = HTCrateVariant.entries.associateWith { variant: HTCrateVariant ->
            val name: String = variant.variantName()
            builder.push(name)
            // Capacity
            val value: HTIntConfigValue = builder.definePositiveInt(
                "multiplier",
                when (variant) {
                    HTCrateVariant.SMALL -> 32
                    HTCrateVariant.MEDIUM -> 128
                    HTCrateVariant.LARGE -> 512
                    HTCrateVariant.HUGE -> 2048
                },
            )
            builder.pop()
            value
        }
        builder.pop()
        // Drum
        builder.push("drum")
        drumCapacity = HTDrumVariant.entries.associateWith { variant: HTDrumVariant ->
            val name: String = variant.variantName()
            builder.push(name)
            // Capacity
            val value: HTIntConfigValue = builder.definePositiveInt(
                "capacity",
                when (variant) {
                    HTDrumVariant.SMALL -> 16_000
                    HTDrumVariant.MEDIUM -> 32_000
                    HTDrumVariant.LARGE -> 64_000
                    HTDrumVariant.HUGE -> 256_000
                },
            )
            builder.pop()
            value
        }
        builder.pop()
        // Block
        builder.push("block")
        crimsonSoilDamage = builder.definePositiveDouble("crimsonSoilDamage", 2.0, 0, Int.MAX_VALUE)
        spongeCakeFallDamage = builder.definePositiveDouble("spongeCakeFallDamage", 0.0, 0, Int.MAX_VALUE)
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
