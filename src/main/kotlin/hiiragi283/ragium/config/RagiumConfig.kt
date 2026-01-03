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
        val processor: Processor

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
            builder.push("processor")
            builder.comment("Configurations for Processor Machines")
            processor = Processor(builder)
            builder.pop()

            builder.push("device")
            builder.comment("Configurations for Device Machines")
            device = Device(builder)
            builder.pop()

            // Storage
            builder.push("storage")
            batteryCapacity = builder.definePositiveInt("battery_capacity", 1_024_000)
            crateCapacity = builder.definePositiveInt("crate_capacity", 32 * 64)
            tankCapacity = builder.definePositiveInt("tank_capacity", 16000)
            builder.pop()
        }

        class Processor(builder: ModConfigSpec.Builder) {
            @JvmField
            val alloySmelter: HTMachineConfig = HTMachineConfig.createSimple(builder, RagiumConst.ALLOY_SMELTER)

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
            val dryer: HTMachineConfig = HTMachineConfig.createSimple(
                builder,
                RagiumConst.DRYER,
                RagiumFluidConfigType.FIRST_INPUT,
                RagiumFluidConfigType.FIRST_OUTPUT,
            )

            @JvmField
            val melter: HTMachineConfig = HTMachineConfig.createSimple(builder, RagiumConst.MELTER, RagiumFluidConfigType.FIRST_OUTPUT)

            @JvmField
            val mixer: HTMachineConfig = HTMachineConfig.createSimple(
                builder,
                RagiumConst.MIXER,
                RagiumFluidConfigType.FIRST_INPUT,
                RagiumFluidConfigType.FIRST_OUTPUT,
            )

            @JvmField
            val pyrolyzer: HTMachineConfig = HTMachineConfig.createSimple(
                builder,
                RagiumConst.PYROLYZER,
                RagiumFluidConfigType.FIRST_OUTPUT,
            )
        }

        class Device(builder: ModConfigSpec.Builder) {
            @JvmField
            val planter: HTMachineConfig = HTMachineConfig.createDevice(builder, RagiumConst.PLANTER, RagiumFluidConfigType.FIRST_INPUT)
        }
    }
}
