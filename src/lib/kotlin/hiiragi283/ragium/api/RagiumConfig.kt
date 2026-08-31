package hiiragi283.ragium.api

import hiiragi283.lib.HTConstants
import hiiragi283.ragium.api.config.HTEnergyConfig
import net.minecraft.resources.Identifier
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

    class Common(builder: ModConfigSpec.Builder)

    class Server(builder: ModConfigSpec.Builder) {
        @JvmField
        val modIdComparator: Comparator<Identifier> = Comparator
            .comparingInt { id: Identifier ->
                val modIds: List<String> = tagOutputPriority.get()
                when (val priority: Int = modIds.indexOf(id.namespace)) {
                    -1 -> modIds.size
                    else -> priority
                }
            }.thenBy(Identifier::getNamespace)

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
        val machine: Machine

        class Machine(builder: ModConfigSpec.Builder) {
            @JvmField
            val tankCapacity: ModConfigSpec.IntValue = builder.defineInRange("tank_capacity", 8000, 1, Int.MAX_VALUE)

            // Mechanical
            @JvmField
            val assembler: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.ASSEMBLER)

            @JvmField
            val crusher: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.CRUSHER)

            @JvmField
            val compressor: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.COMPRESSOR)

            @JvmField
            val cuttingMachine: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.CUTTING_MACHINE)

            // Heat
            @JvmField
            val freezer: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.FREEZER)

            @JvmField
            val melter: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.MELTER)

            @JvmField
            val pyrolyzer: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.PYROLYZER)

            @JvmField
            val refinery: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.REFINERY)

            // Chemical
            @JvmField
            val chemicalBath: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.CHEMICAL_BATH)

            @JvmField
            val chemicalReactor: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.CHEMICAL_REACTOR)

            @JvmField
            val electrolyzer: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.ELECTROLYZER)

            @JvmField
            val mixer: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.MIXER)

            @JvmField
            val washer: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.WASHER)

            // Bio
            @JvmField
            val brewery: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.BREWERY)

            @JvmField
            val planter: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.PLANTER)

            // Electronics
            @JvmField
            val scanner: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.SCANNER)

            // Arcane
            @JvmField
            val fluidDuplicator: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.FLUID_DUPLICATOR, 1024)

            @JvmField
            val massFabricator: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConstants.MASS_FABRICATOR, 1024)
        }

        init {
            builder.push("machine")
            builder.comment("Configurations for Machines")
            machine = Machine(builder)
            builder.pop()
        }
    }
}
