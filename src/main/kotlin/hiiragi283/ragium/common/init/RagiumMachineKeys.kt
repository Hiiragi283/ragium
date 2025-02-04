package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.machine.HTMachineKey

object RagiumMachineKeys {
    //    Consumer    //
    @JvmField
    val BEDROCK_MINER: HTMachineKey = HTMachineKey.of("bedrock_miner")

    @JvmField
    val DRAIN: HTMachineKey = HTMachineKey.of("drain")

    @JvmField
    val CONSUMERS: List<HTMachineKey> =
        listOf(
            BEDROCK_MINER,
        )

    //    Generator    //

    @JvmField
    val COMBUSTION_GENERATOR: HTMachineKey = HTMachineKey.of("combustion_generator")

    @JvmField
    val GAS_TURBINE: HTMachineKey = HTMachineKey.of("gas_turbine")

    @JvmField
    val NUCLEAR_REACTOR: HTMachineKey = HTMachineKey.of("nuclear_reactor")

    @JvmField
    val SOLAR_GENERATOR: HTMachineKey = HTMachineKey.of("solar_generator")

    @JvmField
    val STEAM_TURBINE: HTMachineKey = HTMachineKey.of("steam_turbine")

    @JvmField
    val THERMAL_GENERATOR: HTMachineKey = HTMachineKey.of("thermal_generator")

    @JvmField
    val VIBRATION_GENERATOR: HTMachineKey = HTMachineKey.of("vibration_generator")

    @JvmField
    val GENERATORS: List<HTMachineKey> =
        listOf(
            COMBUSTION_GENERATOR,
            GAS_TURBINE,
            SOLAR_GENERATOR,
            STEAM_TURBINE,
            THERMAL_GENERATOR,
        )

    //    Processor    //
    @JvmField
    val ASSEMBLER: HTMachineKey = HTMachineKey.of("assembler")

    @JvmField
    val BLAST_FURNACE: HTMachineKey = HTMachineKey.of("blast_furnace")

    @JvmField
    val CHEMICAL_REACTOR: HTMachineKey = HTMachineKey.of("chemical_reactor")

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
    val LASER_TRANSFORMER: HTMachineKey = HTMachineKey.of("laser_transformer")

    @JvmField
    val MIXER: HTMachineKey = HTMachineKey.of("mixer")

    @JvmField
    val MULTI_SMELTER: HTMachineKey = HTMachineKey.of("multi_smelter")

    @JvmField
    val REFINERY: HTMachineKey = HTMachineKey.of("refinery")

    @JvmField
    val RESOURCE_PLANT: HTMachineKey = HTMachineKey.of("resource_plant")

    @JvmField
    val STEAM_BOILER: HTMachineKey = HTMachineKey.of("steam_boiler")

    @JvmField
    val PROCESSORS: List<HTMachineKey> =
        listOf(
            ASSEMBLER,
            BLAST_FURNACE,
            CHEMICAL_REACTOR,
            COMPRESSOR,
            EXTRACTOR,
            GRINDER,
            GROWTH_CHAMBER,
            INFUSER,
            LASER_TRANSFORMER,
            MIXER,
            MULTI_SMELTER,
            REFINERY,
            RESOURCE_PLANT,
            STEAM_BOILER,
        )
}
