package hiiragi283.ragium.api

import hiiragi283.ragium.api.util.RagiumConst
import net.neoforged.neoforge.common.ModConfigSpec
import org.apache.commons.lang3.tuple.Pair

object RagiumConfig {
    @JvmField
    val COMMON_SPEC: ModConfigSpec

    @JvmField
    val COMMON: Common

    init {
        val commonPair: Pair<Common, ModConfigSpec> = ModConfigSpec.Builder().configure(::Common)
        COMMON_SPEC = commonPair.right
        COMMON = commonPair.left
    }

    //    Common    //

    class Common(builder: ModConfigSpec.Builder) {
        //    Machine    //

        @JvmField
        val machineTankCapacity: ModConfigSpec.IntValue

        @JvmField
        val basicMachineEnergyUsage: ModConfigSpec.IntValue

        @JvmField
        val advancedMachineEnergyUsage: ModConfigSpec.IntValue

        //    Machine - Collector    //

        @JvmField
        val entityCollectorRange: ModConfigSpec.IntValue

        @JvmField
        val expCollectorMultiplier: ModConfigSpec.IntValue

        @JvmField
        val milkDrainMultiplier: ModConfigSpec.IntValue

        //    Machine - Drum    //

        @JvmField
        val smallDrumCapacity: ModConfigSpec.IntValue

        @JvmField
        val mediumDrumCapacity: ModConfigSpec.IntValue

        @JvmField
        val largeDrumCapacity: ModConfigSpec.IntValue

        @JvmField
        val hugeDrumCapacity: ModConfigSpec.IntValue

        //    Machine - Network    //

        @JvmField
        val defaultNetworkCapacity: ModConfigSpec.IntValue

        //    Recipe    //

        @JvmField
        val tagOutputModIds: ModConfigSpec.ConfigValue<List<String>>

        //    World    //

        @JvmField
        val dropTraderCatalog: ModConfigSpec.BooleanValue

        init {
            // Machine
            builder.push("machine")
            machineTankCapacity = builder
                .comment("Capacity for all tanks in machines")
                .worldRestart()
                .definePositiveInt("machineTankCapacity", 8000)
            basicMachineEnergyUsage = builder
                .comment("Energy per tick for basic machines, multiplied by processing interval ticks")
                .definePositiveInt("basicMachineEnergy", 16)
            advancedMachineEnergyUsage = builder
                .comment("Energy per tick for advanced machines, multiplied by processing interval ticks")
                .definePositiveInt("advancedMachineEnergyUsage", 64)
            // Machine - Collector
            builder.push("collector")
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
            // Machine - Drum
            builder.push("drum")
            smallDrumCapacity = builder
                .gameRestart()
                .definePositiveInt("smallDrumCapacity", 32000)
            mediumDrumCapacity = builder
                .gameRestart()
                .definePositiveInt("mediumDrumCapacity", 64000)
            largeDrumCapacity = builder
                .gameRestart()
                .definePositiveInt("largeDrumCapacity", 128000)
            hugeDrumCapacity = builder
                .gameRestart()
                .definePositiveInt("hugeDrumCapacity", 256000)
            builder.pop()
            // Machine - Network
            builder.push("network")
            defaultNetworkCapacity = builder
                .gameRestart()
                .definePositiveInt("defaultNetworkCapacity", 1_000_000)
            builder.pop(2)
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
            dropTraderCatalog = builder.define("dropTraderCatalog", true)
            builder.pop()
            // Done!
            builder.build()
        }
    }

    //    Extension    //

    private fun ModConfigSpec.Builder.definePositiveInt(path: String, defaultValue: Int, min: Int = 1): ModConfigSpec.IntValue =
        defineInRange(path, defaultValue, min, Int.MAX_VALUE)
}
