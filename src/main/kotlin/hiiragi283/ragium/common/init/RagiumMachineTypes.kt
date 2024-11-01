package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.machine.HTMachineTypeKey

object RagiumMachineTypes {
    //    Consumer    //

    enum class Consumer : HTMachineConvertible {
        DRAIN,
        ;

        override val key: HTMachineTypeKey = HTMachineTypeKey.of(RagiumAPI.id(name.lowercase()))

        override fun asMachine(): HTMachineType = key.asMachine()
    }

    //    Generator    //

    enum class Generator : HTMachineConvertible {
        COMBUSTION,
        SOLAR,
        STEAM,
        THERMAL,
        WATER,
        ;

        override val key: HTMachineTypeKey = HTMachineTypeKey.of(RagiumAPI.id(name.lowercase()))

        override fun asMachine(): HTMachineType = key.asMachine()
    }

    //    Processor    //

    @JvmField
    val BLAST_FURNACE: HTMachineTypeKey = HTMachineTypeKey.of(RagiumAPI.id("blast_furnace"))

    @JvmField
    val DISTILLATION_TOWER: HTMachineTypeKey = HTMachineTypeKey.of(RagiumAPI.id("distillation_tower"))

    @JvmField
    val SAW_MILL: HTMachineTypeKey = HTMachineTypeKey.of(RagiumAPI.id("saw_mill"))

    enum class Processor : HTMachineConvertible {
        ALLOY_FURNACE,
        ASSEMBLER,
        CHEMICAL_REACTOR,
        ELECTROLYZER,
        EXTRACTOR,
        GRINDER,
        METAL_FORMER,
        MIXER,
        ROCK_GENERATOR,
        ;

        override val key: HTMachineTypeKey = HTMachineTypeKey.of(RagiumAPI.id(name.lowercase()))

        override fun asMachine(): HTMachineType = key.asMachine()
    }
}
