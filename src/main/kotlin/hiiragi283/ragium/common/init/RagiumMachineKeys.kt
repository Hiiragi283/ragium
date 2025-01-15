package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.machine.HTMachineKey

object RagiumMachineKeys {
    //    Consumer    //
    @JvmField
    val BEDROCK_MINER: HTMachineKey = HTMachineKey.of("bedrock_miner")

    @JvmField
    val BIOMASS_FERMENTER: HTMachineKey = HTMachineKey.of("biomass_fermenter")

    @JvmField
    val DRAIN: HTMachineKey = HTMachineKey.of("drain")

    @JvmField
    val FLUID_DRILL: HTMachineKey = HTMachineKey.of("fluid_drill")

    @JvmField
    val GAS_PLANT: HTMachineKey = HTMachineKey.of("gas_plant")

    @JvmField
    val ROCK_GENERATOR: HTMachineKey = HTMachineKey.of("rock_generator")

    @JvmField
    val CONSUMERS: List<HTMachineKey> =
        listOf(
            BEDROCK_MINER,
            BIOMASS_FERMENTER,
            // DRAIN,
            FLUID_DRILL,
            // GAS_PLANT,
            // ROCK_GENERATOR,
        )

    //    Generator    //

    @JvmField
    val COMBUSTION_GENERATOR: HTMachineKey = HTMachineKey.of("combustion_generator")

    @JvmField
    val NUCLEAR_REACTOR: HTMachineKey = HTMachineKey.of("nuclear_reactor")

    @JvmField
    val SOLAR_GENERATOR: HTMachineKey = HTMachineKey.of("solar_generator")

    @JvmField
    val STEAM_GENERATOR: HTMachineKey = HTMachineKey.of("steam_generator")

    @JvmField
    val THERMAL_GENERATOR: HTMachineKey = HTMachineKey.of("thermal_generator")

    @JvmField
    val VIBRATION_GENERATOR: HTMachineKey = HTMachineKey.of("vibration_generator")

    @JvmField
    val GENERATORS: List<HTMachineKey> =
        listOf(
            COMBUSTION_GENERATOR,
            // NUCLEAR_REACTOR,
            SOLAR_GENERATOR,
            STEAM_GENERATOR,
            THERMAL_GENERATOR,
            // VIBRATION_GENERATOR,
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
    val CUTTING_MACHINE: HTMachineKey = HTMachineKey.of("cutting_machine")

    @JvmField
    val DISTILLATION_TOWER: HTMachineKey = HTMachineKey.of("distillation_tower")

    @JvmField
    val EXTRACTOR: HTMachineKey = HTMachineKey.of("extractor")

    @JvmField
    val GRINDER: HTMachineKey = HTMachineKey.of("grinder")

    @JvmField
    val GROWTH_CHAMBER: HTMachineKey = HTMachineKey.of("growth_chamber")

    @JvmField
    val LASER_TRANSFORMER: HTMachineKey = HTMachineKey.of("laser_transformer")

    @JvmField
    val MIXER: HTMachineKey = HTMachineKey.of("mixer")

    @JvmField
    val MULTI_SMELTER: HTMachineKey = HTMachineKey.of("multi_smelter")

    @JvmField
    val PROCESSORS: List<HTMachineKey> =
        listOf(
            ASSEMBLER,
            BLAST_FURNACE,
            CHEMICAL_REACTOR,
            COMPRESSOR,
            CUTTING_MACHINE,
            DISTILLATION_TOWER,
            EXTRACTOR,
            GRINDER,
            GROWTH_CHAMBER,
            LASER_TRANSFORMER,
            MIXER,
            MULTI_SMELTER,
        )
}
