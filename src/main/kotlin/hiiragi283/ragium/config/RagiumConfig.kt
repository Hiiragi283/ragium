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

            // Storage
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
            val alloySmelter: HTEnergyConfig = HTEnergyConfig.createSimple(builder, RagiumConst.ALLOY_SMELTER)

            @JvmField
            val assembler: HTEnergyConfig = HTEnergyConfig.createSimple(builder, RagiumConst.ASSEMBLER)

            @JvmField
            val autoChisel: HTEnergyConfig = HTEnergyConfig.createSimple(builder, RagiumConst.AUTO_CHISEL)

            @JvmField
            val crusher: HTEnergyConfig = HTEnergyConfig.createSimple(builder, RagiumConst.CRUSHER)

            @JvmField
            val cuttingMachine: HTEnergyConfig = HTEnergyConfig.createSimple(builder, RagiumConst.CUTTING_MACHINE)

            @JvmField
            val electricFurnace: HTEnergyConfig = HTEnergyConfig.createSimple(builder, RagiumConst.ELECTRIC_FURNACE)

            @JvmField
            val planter: HTEnergyConfig = HTEnergyConfig.createSimple(builder, RagiumConst.PLANTER)

            // Advanced
            @JvmField
            val freezer: HTEnergyConfig = HTEnergyConfig.createSimple(builder, RagiumConst.FREEZER)

            @JvmField
            val melter: HTEnergyConfig = HTEnergyConfig.createSimple(builder, RagiumConst.MELTER)

            @JvmField
            val pyrolyzer: HTEnergyConfig = HTEnergyConfig.createSimple(builder, RagiumConst.PYROLYZER)

            @JvmField
            val refinery: HTEnergyConfig = HTEnergyConfig.createSimple(builder, RagiumConst.REFINERY)

            // Elite
            @JvmField
            val chemicalWasher: HTEnergyConfig = HTEnergyConfig.createSimple(builder, RagiumConst.CHEMICAL_WASHER)

            @JvmField
            val brewery: HTEnergyConfig = HTEnergyConfig.createSimple(builder, RagiumConst.BREWERY)

            @JvmField
            val mixer: HTEnergyConfig = HTEnergyConfig.createSimple(builder, RagiumConst.MIXER)

            @JvmField
            val washer: HTEnergyConfig = HTEnergyConfig.createSimple(builder, RagiumConst.WASHER)

            // Ultimate
            @JvmField
            val fluidDuplicator: HTEnergyConfig = HTEnergyConfig.createSimple(builder, RagiumConst.FLUID_DUPLICATOR)
        }

        class Device(builder: ModConfigSpec.Builder)
    }
}
