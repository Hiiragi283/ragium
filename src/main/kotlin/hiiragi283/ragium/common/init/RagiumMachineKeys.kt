package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineKey

object RagiumMachineKeys {
    //    Consumer    //
    @JvmField
    val BEDROCK_MINER: HTMachineKey = HTMachineKey.of(RagiumAPI.id("bedrock_miner"))

    @JvmField
    val BIOMASS_FERMENTER: HTMachineKey = HTMachineKey.of(RagiumAPI.id("biomass_fermenter"))

    @JvmField
    val DRAIN: HTMachineKey = HTMachineKey.of(RagiumAPI.id("drain"))

    @JvmField
    val FLUID_DRILL: HTMachineKey = HTMachineKey.of(RagiumAPI.id("fluid_drill"))

    @JvmField
    val ROCK_GENERATOR: HTMachineKey = HTMachineKey.of(RagiumAPI.id("rock_generator"))

    @JvmField
    val CONSUMERS: List<HTMachineKey> = listOf(
        BEDROCK_MINER,
        BIOMASS_FERMENTER,
        DRAIN,
        FLUID_DRILL,
        ROCK_GENERATOR,
    )

    //    Generator    //

    @JvmField
    val COMBUSTION_GENERATOR: HTMachineKey = HTMachineKey.of(RagiumAPI.id("combustion_generator"))

    @JvmField
    val NUCLEAR_REACTOR: HTMachineKey = HTMachineKey.of(RagiumAPI.id("nuclear_reactor"))

    @JvmField
    val SOLAR_GENERATOR: HTMachineKey = HTMachineKey.of(RagiumAPI.id("solar_generator"))

    @JvmField
    val STEAM_GENERATOR: HTMachineKey = HTMachineKey.of(RagiumAPI.id("steam_generator"))

    @JvmField
    val THERMAL_GENERATOR: HTMachineKey = HTMachineKey.of(RagiumAPI.id("thermal_generator"))

    @JvmField
    val GENERATORS: List<HTMachineKey> = listOf(
        COMBUSTION_GENERATOR,
        NUCLEAR_REACTOR,
        SOLAR_GENERATOR,
        STEAM_GENERATOR,
        THERMAL_GENERATOR,
    )

    //    Processor    //
    @JvmField
    val ASSEMBLER: HTMachineKey = HTMachineKey.of(RagiumAPI.id("assembler"))

    @JvmField
    val BLAST_FURNACE: HTMachineKey = HTMachineKey.of(RagiumAPI.id("blast_furnace"))

    @JvmField
    val CHEMICAL_REACTOR: HTMachineKey = HTMachineKey.of(RagiumAPI.id("chemical_reactor"))

    @JvmField
    val COMPRESSOR: HTMachineKey = HTMachineKey.of(RagiumAPI.id("compressor"))

    @JvmField
    val CUTTING_MACHINE: HTMachineKey = HTMachineKey.of(RagiumAPI.id("cutting_machine"))

    @JvmField
    val DISTILLATION_TOWER: HTMachineKey = HTMachineKey.of(RagiumAPI.id("distillation_tower"))

    @JvmField
    val ELECTROLYZER: HTMachineKey = HTMachineKey.of(RagiumAPI.id("electrolyzer"))

    @JvmField
    val EXTRACTOR: HTMachineKey = HTMachineKey.of(RagiumAPI.id("extractor"))

    @JvmField
    val GRINDER: HTMachineKey = HTMachineKey.of(RagiumAPI.id("grinder"))

    @JvmField
    val GROWTH_CHAMBER: HTMachineKey = HTMachineKey.of(RagiumAPI.id("growth_chamber"))

    @JvmField
    val INFUSER: HTMachineKey = HTMachineKey.of(RagiumAPI.id("infuser"))

    @JvmField
    val LASER_TRANSFORMER: HTMachineKey = HTMachineKey.of(RagiumAPI.id("laser_transformer"))

    @JvmField
    val MIXER: HTMachineKey = HTMachineKey.of(RagiumAPI.id("mixer"))

    @JvmField
    val MULTI_SMELTER: HTMachineKey = HTMachineKey.of(RagiumAPI.id("multi_smelter"))

    @JvmField
    val PROCESSORS: List<HTMachineKey> = listOf(
        ASSEMBLER,
        BLAST_FURNACE,
        CHEMICAL_REACTOR,
        COMPRESSOR,
        CUTTING_MACHINE,
        DISTILLATION_TOWER,
        ELECTROLYZER,
        EXTRACTOR,
        GRINDER,
        GROWTH_CHAMBER,
        INFUSER,
        LASER_TRANSFORMER,
        MIXER,
        MULTI_SMELTER,
    )
}
