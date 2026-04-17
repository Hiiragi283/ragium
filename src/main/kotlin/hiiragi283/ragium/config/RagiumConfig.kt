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
        // Processor
        @JvmField
        val machine: Machine

        // Device
        @JvmField
        val device: Device

        // Storage
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
            val alloySmelter: HTMachineConfig = HTMachineConfig.createSimple(builder, RagiumConst.ALLOY_SMELTER)

            @JvmField
            val assembler: HTMachineConfig = HTMachineConfig.createSimple(builder, RagiumConst.ASSEMBLER)

            @JvmField
            val autoChisel: HTMachineConfig = HTMachineConfig.createSimple(builder, RagiumConst.AUTO_CHISEL)

            @JvmField
            val crusher: HTMachineConfig = HTMachineConfig.createSimple(builder, RagiumConst.CRUSHER)

            @JvmField
            val cuttingMachine: HTMachineConfig = HTMachineConfig.createSimple(builder, RagiumConst.CUTTING_MACHINE)

            @JvmField
            val electricFurnace: HTMachineConfig = HTMachineConfig.createSimple(builder, RagiumConst.ELECTRIC_FURNACE)

            // Advanced
            @JvmField
            val freezer: HTMachineConfig = HTMachineConfig.createSimple(builder, RagiumConst.FREEZER)

            @JvmField
            val melter: HTMachineConfig = HTMachineConfig.createSimple(builder, RagiumConst.MELTER)

            @JvmField
            val pyrolyzer: HTMachineConfig = HTMachineConfig.createSimple(builder, RagiumConst.PYROLYZER)

            @JvmField
            val refinery: HTMachineConfig = HTMachineConfig.createSimple(builder, RagiumConst.REFINERY)

            // Elite
            @JvmField
            val chemicalWasher: HTMachineConfig = HTMachineConfig.createSimple(builder, RagiumConst.CHEMICAL_WASHER)

            @JvmField
            val brewery: HTMachineConfig = HTMachineConfig.createSimple(builder, RagiumConst.BREWERY)

            @JvmField
            val mixer: HTMachineConfig = HTMachineConfig.createSimple(builder, RagiumConst.MIXER)

            @JvmField
            val washer: HTMachineConfig = HTMachineConfig.createSimple(builder, RagiumConst.WASHER)

            // Ultimate
            @JvmField
            val enchanter: HTMachineConfig = HTMachineConfig.createDevice(builder, RagiumConst.ENCHANTER)
        }

        class Device(builder: ModConfigSpec.Builder) {
            // Basic
            @JvmField
            val planter: HTMachineConfig = HTMachineConfig.createDevice(builder, RagiumConst.PLANTER)
        }
    }
}
