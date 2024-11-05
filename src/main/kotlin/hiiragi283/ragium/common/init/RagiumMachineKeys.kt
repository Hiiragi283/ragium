package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineKey

object RagiumMachineKeys {
    //    Consumer    //

    @JvmField
    val DRAIN: HTMachineKey = HTMachineKey.of(RagiumAPI.id("drain"))

    @JvmField
    val CONSUMERS: List<HTMachineKey> = listOf(
        DRAIN,
    )

    //    Generator    //

    @JvmField
    val COMBUSTION_GENERATOR: HTMachineKey = HTMachineKey.of(RagiumAPI.id("combustion_generator"))

    @JvmField
    val SOLAR_PANEL: HTMachineKey = HTMachineKey.of(RagiumAPI.id("solar_panel"))

    @JvmField
    val STEAM_GENERATOR: HTMachineKey = HTMachineKey.of(RagiumAPI.id("steam_generator"))

    @JvmField
    val THERMAL_GENERATOR: HTMachineKey = HTMachineKey.of(RagiumAPI.id("thermal_generator"))

    @JvmField
    val WATER_GENERATOR: HTMachineKey = HTMachineKey.of(RagiumAPI.id("water_generator"))

    @JvmField
    val GENERATORS: List<HTMachineKey> = listOf(
        COMBUSTION_GENERATOR,
        SOLAR_PANEL,
        STEAM_GENERATOR,
        THERMAL_GENERATOR,
        WATER_GENERATOR,
    )

    //    Processor    //

    @JvmField
    val ALLOY_FURNACE: HTMachineKey = HTMachineKey.of(RagiumAPI.id("alloy_furnace"))

    @JvmField
    val ASSEMBLER: HTMachineKey = HTMachineKey.of(RagiumAPI.id("assembler"))

    @JvmField
    val BLAST_FURNACE: HTMachineKey = HTMachineKey.of(RagiumAPI.id("blast_furnace"))

    @JvmField
    val CHEMICAL_REACTOR: HTMachineKey = HTMachineKey.of(RagiumAPI.id("chemical_reactor"))

    @JvmField
    val DISTILLATION_TOWER: HTMachineKey = HTMachineKey.of(RagiumAPI.id("distillation_tower"))

    @JvmField
    val ELECTROLYZER: HTMachineKey = HTMachineKey.of(RagiumAPI.id("electrolyzer"))

    @JvmField
    val EXTRACTOR: HTMachineKey = HTMachineKey.of(RagiumAPI.id("extractor"))

    @JvmField
    val GRINDER: HTMachineKey = HTMachineKey.of(RagiumAPI.id("grinder"))

    @JvmField
    val METAL_FORMER: HTMachineKey = HTMachineKey.of(RagiumAPI.id("metal_former"))

    @JvmField
    val MIXER: HTMachineKey = HTMachineKey.of(RagiumAPI.id("mixer"))

    @JvmField
    val MULTI_SMELTER: HTMachineKey = HTMachineKey.of(RagiumAPI.id("multi_smelter"))

    @JvmField
    val ROCK_GENERATOR: HTMachineKey = HTMachineKey.of(RagiumAPI.id("rock_generator"))

    @JvmField
    val SAW_MILL: HTMachineKey = HTMachineKey.of(RagiumAPI.id("saw_mill"))

    @JvmField
    val PROCESSORS: List<HTMachineKey> = listOf(
        ALLOY_FURNACE,
        ASSEMBLER,
        BLAST_FURNACE,
        CHEMICAL_REACTOR,
        DISTILLATION_TOWER,
        ELECTROLYZER,
        EXTRACTOR,
        GRINDER,
        METAL_FORMER,
        MIXER,
        MULTI_SMELTER,
        ROCK_GENERATOR,
        SAW_MILL,
    )
}
