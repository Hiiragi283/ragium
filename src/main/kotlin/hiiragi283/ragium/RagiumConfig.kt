package hiiragi283.ragium

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.util.material.RagiumTierType
import hiiragi283.ragium.util.variant.HTDrumVariant
import hiiragi283.ragium.util.variant.HTGeneratorVariant
import hiiragi283.ragium.util.variant.HTMachineVariant
import net.neoforged.fml.ModContainer
import net.neoforged.fml.config.ModConfig
import net.neoforged.neoforge.common.ModConfigSpec
import org.apache.commons.lang3.tuple.Pair

object RagiumConfig : RagiumAPI.Config {
    @JvmStatic
    private val COMMON_SPEC: ModConfigSpec

    @JvmStatic
    private val COMMON: Common

    init {
        val commonPair: Pair<Common, ModConfigSpec> = ModConfigSpec.Builder().configure(::Common)
        COMMON_SPEC = commonPair.right
        COMMON = commonPair.left
    }

    @JvmStatic
    fun register(container: ModContainer) {
        container.registerConfig(ModConfig.Type.COMMON, COMMON_SPEC)
    }

    //    RagiumAPI    //

    // Generator
    override fun getGeneratorEnergyRate(key: String): Int = COMMON.generatorEnergyRate[key]?.get() ?: 0

    // Machine
    override fun getMachineTankCapacity(key: String): Int = COMMON.machineTankCapacity[key]?.get() ?: 8000

    override fun getProcessorEnergyUsage(key: String): Int = COMMON.machineEnergyUsage[key]?.get() ?: 0

    override fun getDefaultNetworkCapacity(): Int = COMMON.defaultNetworkCapacity.get()

    // Device
    override fun getDeviceTankCapacity(): Int = COMMON.deviceTankCapacity.get()

    override fun getEntityCollectorRange(): Int = COMMON.entityCollectorRange.get()

    override fun getExpCollectorMultiplier(): Int = COMMON.expCollectorMultiplier.get()

    override fun getMilkDrainMultiplier(): Int = COMMON.milkDrainMultiplier.get()

    // Drum
    override fun getDrumCapacity(key: String): Int = COMMON.drumCapacity[key]?.get() ?: 8000

    // Recipe
    override fun getTagOutputPriority(): List<String> = COMMON.tagOutputModIds.get()

    // World
    override fun disableMilkCure(): Boolean = COMMON.disableMilkCure.get()

    //    Common    //

    private class Common(builder: ModConfigSpec.Builder) {
        //    Generator    //

        @JvmField
        val generatorEnergyRate: Map<String, ModConfigSpec.IntValue>

        //    Machine    //

        @JvmField
        val machineTankCapacity: MutableMap<String, ModConfigSpec.IntValue> = mutableMapOf()

        @JvmField
        val machineEnergyUsage: Map<String, ModConfigSpec.IntValue>

        //    Device     //

        @JvmField
        val deviceTankCapacity: ModConfigSpec.IntValue

        @JvmField
        val entityCollectorRange: ModConfigSpec.IntValue

        @JvmField
        val expCollectorMultiplier: ModConfigSpec.IntValue

        @JvmField
        val milkDrainMultiplier: ModConfigSpec.IntValue

        //    Drum    //

        @JvmField
        val drumCapacity: Map<String, ModConfigSpec.IntValue>

        //    Machine - Network    //

        @JvmField
        val defaultNetworkCapacity: ModConfigSpec.IntValue

        //    Recipe    //

        @JvmField
        val tagOutputModIds: ModConfigSpec.ConfigValue<List<String>>

        //    World    //

        @JvmField
        val disableMilkCure: ModConfigSpec.BooleanValue

        init {
            // Generator
            builder.push("generator")
            generatorEnergyRate = HTGeneratorVariant.entries.associate { variant: HTGeneratorVariant ->
                val name: String = variant.serializedName
                builder.push(name)
                // Tank Capacity
                machineTankCapacity[name] = builder
                    .comment("Capacity for tanks in this machine")
                    .worldRestart()
                    .definePositiveInt("tankCapacity", 8000)
                // Energy Rate
                val defaultValue: Int = when (variant.tier) {
                    RagiumTierType.BASIC -> 32
                    RagiumTierType.ADVANCED -> 128
                    RagiumTierType.ELITE -> 512
                    RagiumTierType.ULTIMATE -> 2048
                }
                val value: ModConfigSpec.IntValue = builder.definePositiveInt("energyRate", defaultValue)
                builder.pop()
                name to value
            }
            builder.pop()
            // Machine
            builder.push("machine")
            machineEnergyUsage = HTMachineVariant.entries.associate { variant: HTMachineVariant ->
                val name: String = variant.serializedName
                builder.push(name)
                // Tank Capacity
                machineTankCapacity[name] = builder
                    .comment("Capacity for tanks in this machine")
                    .worldRestart()
                    .definePositiveInt("tankCapacity", 8000)
                // Energy Usage
                val defaultValue: Int = when (variant.tier) {
                    RagiumTierType.BASIC -> 16
                    RagiumTierType.ADVANCED -> 64
                    RagiumTierType.ELITE -> 256
                    RagiumTierType.ULTIMATE -> 1024
                }
                val value: ModConfigSpec.IntValue = builder.definePositiveInt("energyUsage", defaultValue)
                builder.pop()
                name to value
            }

            defaultNetworkCapacity = builder
                .gameRestart()
                .definePositiveInt("defaultNetworkCapacity", 1_000_000)
            builder.pop()
            // Device
            builder.push("device")
            deviceTankCapacity = builder
                .comment("Capacity for all tanks in devices")
                .definePositiveInt("deviceTankCapacity", 8000)
            entityCollectorRange = builder
                .comment("Range for entity-targeted collectors")
                .definePositiveInt("collectorRange", 5)
            expCollectorMultiplier = builder
                .comment("Multiplier for converting exp into fluid in Exp Collector")
                .definePositiveInt("expCollectorMultiplier", 20)
            milkDrainMultiplier = builder
                .comment("Multiplier for collecting milk from cows in Milk Drain")
                .definePositiveInt("milkDrainMultiplier", 50)
            builder.pop()
            // Drum
            builder.push("drum")
            drumCapacity = HTDrumVariant.entries.associate { variant: HTDrumVariant ->
                val name: String = variant.serializedName
                builder.push(name)
                val defaultValue: Int = when (variant) {
                    HTDrumVariant.SMALL -> 16_000
                    HTDrumVariant.MEDIUM -> 32_000
                    HTDrumVariant.LARGE -> 64_000
                    HTDrumVariant.HUGE -> 256_000
                }
                val value: ModConfigSpec.IntValue = builder
                    .worldRestart()
                    .definePositiveInt("capacity", defaultValue)
                builder.pop()
                name to value
            }
            builder.pop()
            // Recipe
            builder.push("recipe")
            tagOutputModIds = builder
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
                )
            builder.pop()
            // World
            builder.push("world")
            disableMilkCure = builder.define("disableMilkCure", false)
            builder.pop()
            // Done!
            builder.build()
        }
    }

    //    Extension    //

    private fun ModConfigSpec.Builder.definePositiveInt(path: String, defaultValue: Int, min: Int = 1): ModConfigSpec.IntValue =
        defineInRange(path, defaultValue, min, Int.MAX_VALUE)
}
