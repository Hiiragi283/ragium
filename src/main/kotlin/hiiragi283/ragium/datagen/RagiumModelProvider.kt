package hiiragi283.ragium.datagen

import hiiragi283.ragium.client.data.RagiumModels
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.recipe.HTMachineTier
import hiiragi283.ragium.common.recipe.HTMachineType
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.data.client.*
import net.minecraft.util.Identifier

class RagiumModelProvider(output: FabricDataOutput) : FabricModelProvider(output) {
    companion object {
        @JvmField
        val HULL_TEXTURE_FACTORY: TexturedModel.Factory =
            TexturedModel.makeFactory({ block: Block ->
                val tier: HTMachineTier =
                    block.asItem().components.getOrDefault(RagiumComponentTypes.TIER, HTMachineTier.HEAT)
                TextureMap()
                    .put(
                        TextureKey.TOP,
                        when (tier) {
                            HTMachineTier.HEAT -> RagiumBlocks.RAGI_ALLOY_BLOCK
                            HTMachineTier.KINETIC -> RagiumBlocks.RAGI_STEEL_BLOCK
                            HTMachineTier.ELECTRIC -> RagiumBlocks.REFINED_RAGI_STEEL_BLOCK
                            else -> throw IllegalStateException()
                        }.let(TextureMap::getId),
                    ).put(
                        TextureKey.BOTTOM,
                        when (tier) {
                            HTMachineTier.HEAT -> Blocks.BRICKS
                            HTMachineTier.KINETIC -> Blocks.DEEPSLATE_TILES
                            HTMachineTier.ELECTRIC -> Blocks.CHISELED_QUARTZ_BLOCK
                            else -> throw IllegalStateException()
                        }.let(TextureMap::getId),
                    )
            }, RagiumModels.HULL)
    }

    //   BlockState    //

    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        RagiumBlocks.REGISTER.generateState(generator)

        registerMachines(generator)
    }

    private fun registerMachines(generator: BlockStateModelGenerator) {
        HTMachineType.getEntries().forEach { type: HTMachineType ->
            generator.registerNorthDefaultHorizontalRotated(
                type.block,
                TexturedModel.makeFactory({ createMachineTextureMap(type) }, RagiumModels.MACHINE),
            )
        }
    }

    private fun createMachineTextureMap(type: HTMachineType): TextureMap {
        val topTex: Identifier =
            when (type.tier) {
                HTMachineTier.HEAT -> RagiumBlocks.RAGI_ALLOY_BLOCK
                HTMachineTier.KINETIC -> RagiumBlocks.RAGI_STEEL_BLOCK
                else -> RagiumBlocks.REFINED_RAGI_STEEL_BLOCK
            }.let(TextureMap::getId)
        val bottomTex: Identifier =
            when (type.tier) {
                HTMachineTier.HEAT -> Blocks.BRICKS
                HTMachineTier.KINETIC -> Blocks.DEEPSLATE_TILES
                else -> Blocks.CHISELED_QUARTZ_BLOCK
            }.let(TextureMap::getId)
        return TextureMap()
            .put(TextureKey.TOP, topTex)
            .put(TextureKey.BOTTOM, bottomTex)
            .put(TextureKey.FRONT, TextureMap.getSubId(type.block, "_front"))
    }

    //    Model    //

    override fun generateItemModels(generator: ItemModelGenerator) {
        RagiumItems.REGISTER.generateModel(generator)
    }
}
