package hiiragi283.ragium.datagen

import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.Models

class RagiumModelProvider(output: FabricDataOutput) : FabricModelProvider(output) {
    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        generator.registerSimpleCubeAll(RagiumBlocks.RAGINITE_ORE)
        generator.registerSimpleCubeAll(RagiumBlocks.DEEPSLATE_RAGINITE_ORE)

        // tier1
        // tier2
        // tier3
        // tier4
        // tier5
    }

    override fun generateItemModels(generator: ItemModelGenerator) {
        generator.register(RagiumItems.POWER_METER, Models.GENERATED)
        // tier1
        generator.register(RagiumItems.RAW_RAGINITE, Models.GENERATED)
        generator.register(RagiumItems.RAW_RAGINITE_DUST, Models.GENERATED)
        generator.register(RagiumItems.RAGI_ALLOY_COMPOUND, Models.GENERATED)
        generator.register(RagiumItems.RAGI_ALLOY_INGOT, Models.GENERATED)
        // tier2
        generator.register(RagiumItems.RAGI_STEEL_INGOT, Models.GENERATED)
        // tier3
        generator.register(RagiumItems.REFINED_RAGI_STEEL_INGOT, Models.GENERATED)
        // tier4

        // tier5
    }
}