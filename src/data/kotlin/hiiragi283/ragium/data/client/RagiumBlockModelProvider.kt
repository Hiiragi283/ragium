package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.blockTexture
import hiiragi283.ragium.api.extension.getBuilder
import hiiragi283.ragium.api.machine.HTMachineType
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.BlockModelProvider
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.common.data.ExistingFileHelper

class RagiumBlockModelProvider(output: PackOutput, existingFileHelper: ExistingFileHelper) :
    BlockModelProvider(output, RagiumAPI.MOD_ID, existingFileHelper) {
    override fun registerModels() {
        // Machine
        fun basicMachine(type: HTMachineType) {
            getBuilder(type.getBlock())
                .parent(ModelFile.UncheckedModelFile(RagiumAPI.id("block/basic_machine")))
                .blockTexture("front", RagiumAPI.id(type.serializedName + "_front"))
        }

        basicMachine(HTMachineType.FISHER)

        basicMachine(HTMachineType.STIRLING_GENERATOR)

        // Assembler
        basicMachine(HTMachineType.BLAST_FURNACE)
        basicMachine(HTMachineType.COMPRESSOR)
        basicMachine(HTMachineType.CRUSHER)
        basicMachine(HTMachineType.GRINDER)
        basicMachine(HTMachineType.MULTI_SMELTER)

        fun chemicalMachine(type: HTMachineType) {
            getBuilder(type.getBlock())
                .parent(ModelFile.UncheckedModelFile(RagiumAPI.id("block/chemical_machine")))
                .blockTexture("front", RagiumAPI.id(type.serializedName + "_front"))
        }

        chemicalMachine(HTMachineType.EXTRACTOR)
        chemicalMachine(HTMachineType.GROWTH_CHAMBER)
        chemicalMachine(HTMachineType.INFUSER)
        chemicalMachine(HTMachineType.MIXER)
        // Refinery
        chemicalMachine(HTMachineType.SOLIDIFIER)

        fun precisionMachine(type: HTMachineType) {
            getBuilder(type.getBlock())
                .parent(ModelFile.UncheckedModelFile(RagiumAPI.id("block/precision_machine")))
                .blockTexture("front", RagiumAPI.id(type.serializedName + "_front"))
        }

        // Enchanter
        // Brewery
        precisionMachine(HTMachineType.LASER_ASSEMBLY)
    }
}
