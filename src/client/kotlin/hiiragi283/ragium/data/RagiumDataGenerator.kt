package hiiragi283.ragium.data

import hiiragi283.ragium.api.RagiumAPI
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

object RagiumDataGenerator : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
        val pack: FabricDataGenerator.Pack = fabricDataGenerator.createPack()
        // server
        pack.addProvider(::RagiumAdvancementProvider)
        pack.addProvider(::RagiumBlockLootProvider)
        pack.addProvider(::RagiumEntityLootProvider)
        pack.addProvider(::RagiumVanillaRecipeProvider)
        pack.addProvider(::RagiumMachineRecipeProvider)
        RagiumTagProviders.init(pack)
        // client
        pack.addProvider(::RagiumModelProvider)
        RagiumLangProviders.init(pack)

        RagiumAPI.log { info("Ragium data generation is done!") }
    }
}