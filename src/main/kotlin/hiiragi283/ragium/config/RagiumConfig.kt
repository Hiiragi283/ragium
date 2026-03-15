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
            // Basic
            @JvmField
            val alloySmelter: HTMachineConfig = HTMachineConfig.createSimple(builder, RagiumConst.ALLOY_SMELTER)

            @JvmField
            val autoChisel: HTMachineConfig = HTMachineConfig.createSimple(builder, RagiumConst.AUTO_CHISEL)

            @JvmField
            val compressor: HTMachineConfig = HTMachineConfig.createSimple(builder, RagiumConst.COMPRESSOR)

            @JvmField
            val crusher: HTMachineConfig = HTMachineConfig.createSimple(
                builder,
                RagiumConst.CRUSHER,
                RagiumFluidConfigType.FIRST_INPUT,
            )

            @JvmField
            val cuttingMachine: HTMachineConfig = HTMachineConfig.createSimple(
                builder,
                RagiumConst.CUTTING_MACHINE,
                RagiumFluidConfigType.FIRST_INPUT,
            )

            @JvmField
            val electricFurnace: HTMachineConfig = HTMachineConfig.createSimple(builder, RagiumConst.ELECTRIC_FURNACE)

            // Heat
            @JvmField
            val melter: HTMachineConfig = HTMachineConfig.createSimple(
                builder,
                RagiumConst.MELTER,
                RagiumFluidConfigType.FIRST_OUTPUT,
            )

            @JvmField
            val pyrolyzer: HTMachineConfig = HTMachineConfig.createSimple(
                builder,
                RagiumConst.PYROLYZER,
                RagiumFluidConfigType.FIRST_INPUT,
                RagiumFluidConfigType.FIRST_OUTPUT,
            )

            @JvmField
            val refinery: HTMachineConfig = HTMachineConfig.createSimple(
                builder,
                RagiumConst.REFINERY,
                RagiumFluidConfigType.FIRST_INPUT,
                RagiumFluidConfigType.FIRST_OUTPUT,
            )

            // Cool
            @JvmField
            val freezer: HTMachineConfig = HTMachineConfig.createSimple(
                builder,
                RagiumConst.FREEZER,
                RagiumFluidConfigType.FIRST_INPUT,
            )

            // Chemical
            @JvmField
            val brewery: HTMachineConfig = HTMachineConfig.createSimple(
                builder,
                RagiumConst.BREWERY,
                RagiumFluidConfigType.FIRST_INPUT,
                RagiumFluidConfigType.FIRST_OUTPUT,
            )

            @JvmField
            val mixer: HTMachineConfig = HTMachineConfig.createSimple(
                builder,
                RagiumConst.MIXER,
                RagiumFluidConfigType.FIRST_INPUT,
                RagiumFluidConfigType.SECOND_INPUT,
                RagiumFluidConfigType.THIRD_INPUT,
                RagiumFluidConfigType.FIRST_OUTPUT,
                RagiumFluidConfigType.SECOND_OUTPUT,
            )

            @JvmField
            val washer: HTMachineConfig = HTMachineConfig.createSimple(
                builder,
                RagiumConst.WASHER,
                RagiumFluidConfigType.FIRST_INPUT,
            )

            // Matter
        }

        class Device(builder: ModConfigSpec.Builder) {
            // Basic
            @JvmField
            val planter: HTMachineConfig = HTMachineConfig.createDevice(builder, RagiumConst.PLANTER, RagiumFluidConfigType.FIRST_INPUT)

            // Enchanting
            @JvmField
            val enchanter: HTMachineConfig = HTMachineConfig.createDevice(builder, RagiumConst.ENCHANTER, RagiumFluidConfigType.FIRST_INPUT)
        }
    }
}
