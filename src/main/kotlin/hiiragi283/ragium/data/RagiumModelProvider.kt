package hiiragi283.ragium.data

import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator

class RagiumModelProvider(output: FabricDataOutput) : FabricModelProvider(output) {
    //   BlockState    //

    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        RagiumBlocks.REGISTER.generateState(generator)
    }

    //    Model    //

    override fun generateItemModels(generator: ItemModelGenerator) {
        RagiumItems.REGISTER.generateModel(generator)
    }
}
