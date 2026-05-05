package hiiragi283.ragium.config

import hiiragi283.core.api.config.definePositiveInt
import hiiragi283.ragium.api.RagiumConst
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

    class Common(builder: ModConfigSpec.Builder) {
        // Generator

        // Machine
        @JvmField
        val machine: Machine

        // Device
        @JvmField
        val device: Device

        // Item
        @JvmField
        val electricIgniter: HTEnergyConfig

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
            builder.push("machine")
            builder.comment("Configurations for Machines")
            machine = Machine(builder)
            builder.pop()

            builder.push("device")
            builder.comment("Configurations for Devices")
            device = Device(builder)
            builder.pop()

            builder.push("item")
            electricIgniter = HTEnergyConfig.createItem(builder, "electric_igniter", 160, 160 * 64)
            builder.pop()

            builder.push("storage")
            energyBarColor = builder.definePositiveInt("energy_bar_color", 0xFF0033, 0)
            batteryCapacity = builder.definePositiveInt("battery_capacity", 1_024_000)
            crateCapacity = builder.definePositiveInt("crate_capacity", 32 * 64)
            tankCapacity = builder.definePositiveInt("tank_capacity", 16000)
            builder.pop()
        }

        class Machine(builder: ModConfigSpec.Builder) {
            @JvmField
            val tankCapacity: ModConfigSpec.IntValue = builder.definePositiveInt("tank_capacity", 8000)

            // Basic
            @JvmField
            val alloySmelter: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConst.ALLOY_SMELTER)

            @JvmField
            val assembler: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConst.ASSEMBLER)

            @JvmField
            val autoChisel: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConst.AUTO_CHISEL)

            @JvmField
            val crusher: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConst.CRUSHER)

            @JvmField
            val compressor: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConst.COMPRESSOR)

            @JvmField
            val cuttingMachine: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConst.CUTTING_MACHINE)

            @JvmField
            val electricFurnace: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConst.ELECTRIC_FURNACE)

            @JvmField
            val planter: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConst.PLANTER)

            // Advanced
            @JvmField
            val freezer: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConst.FREEZER)

            @JvmField
            val melter: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConst.MELTER)

            @JvmField
            val pyrolyzer: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConst.PYROLYZER)

            @JvmField
            val refinery: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConst.REFINERY)

            // Elite
            @JvmField
            val brewery: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConst.BREWERY)

            @JvmField
            val chemicalBath: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConst.CHEMICAL_BATH)

            @JvmField
            val chemicalReactor: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConst.CHEMICAL_REACTOR)

            @JvmField
            val mixer: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConst.MIXER)

            @JvmField
            val washer: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConst.WASHER)

            // Ultimate
            @JvmField
            val fluidDuplicator: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConst.FLUID_DUPLICATOR, 1024)

            @JvmField
            val massFabricator: HTEnergyConfig = HTEnergyConfig.createMachine(builder, RagiumConst.MASS_FABRICATOR, 1024)
        }

        class Device(builder: ModConfigSpec.Builder)
    }
}
