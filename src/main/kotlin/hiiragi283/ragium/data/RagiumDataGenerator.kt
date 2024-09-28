package hiiragi283.ragium.data

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.data.integration.RagiumAlloyForgeryRecipeProvider
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

object RagiumDataGenerator : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
        val pack: FabricDataGenerator.Pack = fabricDataGenerator.createPack()
        // server
        pack.addProvider(::RagiumAdvancementProvider)
        pack.addProvider(::RagiumFluidPumpEntryProvider)
        pack.addProvider(::RagiumLootProvider)
        pack.addProvider(::RagiumVanillaRecipeProvider)
        pack.addProvider(::RagiumMachineRecipeProvider)
        RagiumTagProviders.init(pack)

        pack.addProvider(::RagiumAlloyForgeryRecipeProvider)
        // client
        pack.addProvider(::RagiumModelProvider)
        RagiumLangProviders.init(pack)

        Ragium.log { info("Ragium data generation is done!") }
    }
}
