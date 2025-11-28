package hiiragi283.ragium.config

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.block.attribute.HTFluidBlockAttribute
import hiiragi283.ragium.api.config.HTBoolConfigValue
import hiiragi283.ragium.api.config.HTDoubleConfigValue
import hiiragi283.ragium.api.config.HTIntConfigValue
import hiiragi283.ragium.api.config.HTListConfigValue
import hiiragi283.ragium.api.config.definePositiveDouble
import hiiragi283.ragium.api.config.definePositiveInt
import hiiragi283.ragium.common.tier.HTCrateTier
import hiiragi283.ragium.common.tier.HTDrumTier
import net.neoforged.neoforge.common.ModConfigSpec

class RagiumCommonConfig(builder: ModConfigSpec.Builder) {
    // Generator
    @JvmField
    val generator: Generator

    // Processor
    @JvmField
    val processor: Processor

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
        builder.push("generator")
        builder.comment("Configurations for Generator Machines")
        generator = Generator(builder)
        builder.pop()

        builder.push("processor")
        builder.comment("Configurations for Processor Machines")
        processor = Processor(builder)
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

    class Generator(builder: ModConfigSpec.Builder) {
        // Basic
        @JvmField
        val thermal: HTMachineConfig = HTMachineConfig.createSimple(builder, "thermal", HTFluidBlockAttribute.TankType.INPUT)

        // Advanced
        @JvmField
        val combustion: HTMachineConfig = HTMachineConfig.createSimple(builder, "combustion", HTFluidBlockAttribute.TankType.INPUT)

        // Elite
        @JvmField
        val solarPanelController: HTMachineConfig = HTMachineConfig.createSimple(builder, "solar_panel_controller")

        // Ultimate
        @JvmField
        val enchantment: HTMachineConfig = HTMachineConfig.createSimple(builder, "enchantment", HTFluidBlockAttribute.TankType.INPUT)

        @JvmField
        val nuclearReactor: HTMachineConfig = HTMachineConfig.createSimple(builder, "nuclear_reactor", HTFluidBlockAttribute.TankType.INPUT)
    }

    class Processor(builder: ModConfigSpec.Builder) {
        // Basic
        @JvmField
        val alloySmelter: HTMachineConfig = HTMachineConfig.createSimple(builder, "alloy_smelter")

        @JvmField
        val blockBreaker: HTMachineConfig = HTMachineConfig.createSimple(builder, "block_breaker")

        @JvmField
        val cuttingMachine: HTMachineConfig = HTMachineConfig.createSimple(builder, "cutting_machine")

        @JvmField
        val compressor: HTMachineConfig = HTMachineConfig.createSimple(builder, "compressor", HTFluidBlockAttribute.TankType.OUTPUT)

        @JvmField
        val extractor: HTMachineConfig = HTMachineConfig.createSimple(builder, "extractor", HTFluidBlockAttribute.TankType.OUTPUT)

        // Advanced
        @JvmField
        val crusher: HTMachineConfig = HTMachineConfig.createSimple(builder, "crusher", HTFluidBlockAttribute.TankType.INPUT)

        @JvmField
        val melter: HTMachineConfig = HTMachineConfig.createSimple(builder, "melter", HTFluidBlockAttribute.TankType.OUTPUT)

        @JvmField
        val mixer: HTMachineConfig = HTMachineConfig.createSimple(
            builder,
            "mixer",
            HTFluidBlockAttribute.TankType.INPUT,
            HTFluidBlockAttribute.TankType.OUTPUT,
        )

        @JvmField
        val refinery: HTMachineConfig = HTMachineConfig.createSimple(
            builder,
            "refinery",
            HTFluidBlockAttribute.TankType.INPUT,
            HTFluidBlockAttribute.TankType.OUTPUT,
        )

        // Elite
        @JvmField
        val advancedMixer: HTMachineConfig = HTMachineConfig.createSimple(
            builder,
            "advanced_mixer",
            HTFluidBlockAttribute.TankType.FIRST_INPUT,
            HTFluidBlockAttribute.TankType.SECOND_INPUT,
            HTFluidBlockAttribute.TankType.OUTPUT,
        )

        @JvmField
        val brewery: HTMachineConfig = HTMachineConfig.createSimple(builder, "brewery", HTFluidBlockAttribute.TankType.INPUT)

        @JvmField
        val multiSmelter: HTMachineConfig = HTMachineConfig.createSimple(builder, "multi_smelter")

        @JvmField
        val planter: HTMachineConfig = HTMachineConfig.createSimple(builder, "planter", HTFluidBlockAttribute.TankType.INPUT)

        @JvmField
        val washer: HTMachineConfig = HTMachineConfig.createSimple(builder, "washer", HTFluidBlockAttribute.TankType.INPUT)

        // Ultimate
        @JvmField
        val enchantCopier: HTMachineConfig = HTMachineConfig.createSimple(builder, "enchant_copier", HTFluidBlockAttribute.TankType.INPUT)

        @JvmField
        val enchanter: HTMachineConfig = HTMachineConfig.createSimple(builder, "enchanter", HTFluidBlockAttribute.TankType.INPUT)

        @JvmField
        val simulator: HTMachineConfig = HTMachineConfig.createSimple(builder, "simulator", HTFluidBlockAttribute.TankType.OUTPUT)
    }
}
