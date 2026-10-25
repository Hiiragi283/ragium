package hiiragi283.ragium.config

import hiiragi283.lib.HTConstants
import hiiragi283.lib.config.definePositiveInt
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConstants
import net.neoforged.neoforge.common.ModConfigSpec

data object RagiumConfig {
    @JvmField
    val COMMON_SPEC: ModConfigSpec

    @JvmField
    val SERVER_SPEC: ModConfigSpec

    @JvmField
    val COMMON: Common

    @JvmField
    val SERVER: Server

    init {
        val (common: Common, commonSpec: ModConfigSpec) = ModConfigSpec.Builder().configure(::Common)
        COMMON_SPEC = commonSpec
        COMMON = common
        val (server: Server, serverSpec: ModConfigSpec) = ModConfigSpec.Builder().configure(::Server)
        SERVER_SPEC = serverSpec
        SERVER = server
    }

    private inline fun <T> pushAndPop(builder: ModConfigSpec.Builder, path: String, factory: ModConfigSpec.Builder.() -> T): T {
        builder.push(path)
        return factory(builder).also { builder.pop() }
    }

    class Common(builder: ModConfigSpec.Builder)

    class Server(builder: ModConfigSpec.Builder) {
        @JvmField
        val tagOutputPriority: ModConfigSpec.ConfigValue<List<String>> =
            builder
                .worldRestart()
                .defineList(
                    "tagOutputModIds",
                    listOf(
                        RagiumAPI.MOD_ID,
                        HTConstants.MINECRAFT,
                        "alltheores",
                        "mekanism",
                    ),
                    { "" },
                    { obj: Any -> obj is String },
                )

        @JvmField
        val machineTankCapacity: ModConfigSpec.IntValue = builder.definePositiveInt("machine_tank_capacity", 8000)

        @JvmField
        val dynamo: Dynamo

        @JvmField
        val machine: Machine

        // Storage
        @JvmField
        val energyBarColor: ModConfigSpec.IntValue

        @JvmField
        val batteryCapacity: ModConfigSpec.IntValue

        @JvmField
        val crateCapacity: ModConfigSpec.IntValue

        @JvmField
        val tankCapacity: ModConfigSpec.IntValue

        init {
            builder.push(RagiumConstants.DYNAMO)
            builder.comment("Configurations for Dynamos")
            dynamo = Dynamo(builder)
            builder.pop()

            builder.push(RagiumConstants.MACHINE)
            builder.comment("Configurations for Machines")
            machine = Machine(builder)
            builder.pop()

            builder.push("storage")
            energyBarColor = builder.definePositiveInt("energy_bar_color", 0xFF0033, 0)
            batteryCapacity = builder.definePositiveInt("battery_capacity", 1_024_000)
            crateCapacity = builder.definePositiveInt("crate_capacity", 32 * 64)
            tankCapacity = builder.definePositiveInt("tank_capacity", 16000)
            builder.pop()
        }

        class Dynamo(builder: ModConfigSpec.Builder) {
            // Mechanical
            @JvmField
            val fuelDynamo: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.FUEL_DYNAMO, 32)

            @JvmField
            val steamDynamoWaterPerTick: ModConfigSpec.IntValue

            // Heat
            @JvmField
            val magmaticDynamo: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.MAGMATIC_DYNAMO, 64)

            init {
                builder.push(RagiumConstants.FUEL_DYNAMO)
                steamDynamoWaterPerTick = builder.definePositiveInt("water_per_tick", 2)
                builder.pop()
            }
        }

        class Machine(builder: ModConfigSpec.Builder) {
            // Mechanical
            @JvmField
            val assembler: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.ASSEMBLER)

            @JvmField
            val centrifuge: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.CENTRIFUGE)

            @JvmField
            val crusher: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.CRUSHER)

            @JvmField
            val compressor: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.COMPRESSOR)

            @JvmField
            val cuttingMachine: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.CUTTING_MACHINE)

            @JvmField
            val pressMachine: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.PRESS_MACHINE)

            // Heat
            @JvmField
            val alloySmelter: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.ALLOY_SMELTER)

            @JvmField
            val freezer: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.FREEZER)

            @JvmField
            val melter: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.MELTER, 64)

            @JvmField
            val pyrolyzer: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.PYROLYZER, 32)

            @JvmField
            val refinery: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.REFINERY, 32)

            @JvmField
            val smelter: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.SMELTER)

            // Chemical
            @JvmField
            val chemicalBath: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.CHEMICAL_BATH)

            @JvmField
            val chemicalReactor: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.CHEMICAL_REACTOR)

            @JvmField
            val mixer: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.MIXER)

            // Resource
            @JvmField
            val brewery: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.BREWERY)

            @JvmField
            val planter: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.PLANTER)

            @JvmField
            val resourceMiner: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.RESOURCE_MINER, 32)

            @JvmField
            val resourcePump: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.RESOURCE_PUMP, 32)

            // Electronics
            @JvmField
            val blender: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.BLENDER, 64)

            @JvmField
            val electrolyzer: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.ELECTROLYZER, 64)

            @JvmField
            val engraver: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.ENGRAVER, 64)

            @JvmField
            val scanner: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.SCANNER, 64)

            // Arcane
            @JvmField
            val enchanter: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.ENCHANTER, 256)

            @JvmField
            val fluidDuplicator: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.FLUID_DUPLICATOR, 1024)

            @JvmField
            val massFabricator: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.MASS_FABRICATOR, 1024)
        }
    }
}
