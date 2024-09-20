package hiiragi283.ragium.data

import hiiragi283.ragium.common.Ragium
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

object RagiumDataGenerator : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
        val pack: FabricDataGenerator.Pack = fabricDataGenerator.createPack()

        pack.addProvider(::RagiumAdvancementProvider)
        pack.addProvider(::RagiumLootProvider)
        pack.addProvider(::RagiumRecipeProvider)
        pack.addProvider(::RagiumDynamicRegistryProvider)
        RagiumTagProviders.init(pack)

        pack.addProvider(::RagiumModelProvider)
        RagiumLangProviders.init(pack)

        Ragium.log { info("Ragium data generation is done!") }
    }

    override fun getEffectiveModId(): String = Ragium.MOD_ID
}
