package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.machine.HTMachineKey

object RagiumMachineKeys {
    //    Consumer    //
    @JvmField
    val BEDROCK_MINER: HTMachineKey = HTMachineKey.of("bedrock_miner")

    @JvmField
    val DRAIN: HTMachineKey = HTMachineKey.of("drain")

    //    Generator    //

    @JvmField
    val COMBUSTION_GENERATOR: HTMachineKey = HTMachineKey.of("combustion_generator")

    @JvmField
    val SOLAR_GENERATOR: HTMachineKey = HTMachineKey.of("solar_generator")

    @JvmField
    val STIRLING_GENERATOR: HTMachineKey = HTMachineKey.of("stirling_generator")

    @JvmField
    val THERMAL_GENERATOR: HTMachineKey = HTMachineKey.of("thermal_generator")

    @JvmField
    val VIBRATION_GENERATOR: HTMachineKey = HTMachineKey.of("vibration_generator")

    //    Processor    //
    @JvmField
    val ASSEMBLER: HTMachineKey = HTMachineKey.of("assembler")

    @JvmField
    val BLAST_FURNACE: HTMachineKey = HTMachineKey.of("blast_furnace")

    @JvmField
    val COMPRESSOR: HTMachineKey = HTMachineKey.of("compressor")

    @JvmField
    val EXTRACTOR: HTMachineKey = HTMachineKey.of("extractor")

    @JvmField
    val GRINDER: HTMachineKey = HTMachineKey.of("grinder")

    @JvmField
    val GROWTH_CHAMBER: HTMachineKey = HTMachineKey.of("growth_chamber")

    @JvmField
    val INFUSER: HTMachineKey = HTMachineKey.of("infuser")

    @JvmField
    val LASER_ASSEMBLY: HTMachineKey = HTMachineKey.of("laser_assembly")

    @JvmField
    val MIXER: HTMachineKey = HTMachineKey.of("mixer")

    @JvmField
    val MULTI_SMELTER: HTMachineKey = HTMachineKey.of("multi_smelter")

    @JvmField
    val REFINERY: HTMachineKey = HTMachineKey.of("refinery")
}
