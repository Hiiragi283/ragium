package hiiragi283.ragium.datagen

import hiiragi283.ragium.client.data.RagiumModels
import hiiragi283.ragium.client.util.*
import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.fluid.HTFluidContent
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.recipe.HTMachineTier
import hiiragi283.ragium.common.recipe.HTMachineType
import hiiragi283.ragium.common.util.getFilteredInstances
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.data.client.*
import net.minecraft.util.Identifier

class RagiumModelProvider(output: FabricDataOutput) : FabricModelProvider(output) {
    //   BlockState    //

    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        registerMachines(generator)

        generator.registerSimpleCubeAll(RagiumBlocks.RAGINITE_ORE)
        generator.registerSimpleCubeAll(RagiumBlocks.DEEPSLATE_RAGINITE_ORE)
        // tier1
        generator.registerSimpleCubeAll(RagiumBlocks.RAGI_ALLOY_BLOCK)
        generator.registerSingleton(RagiumBlocks.RAGI_ALLOY_HULL, hullTextureFactory)

        generator.blockStateCollector.accept(
            buildMultipartState(RagiumBlocks.MANUAL_GRINDER) {
                with(buildModelVariant("block/smooth_stone_slab"))
                RagiumBlocks.Properties.LEVEL_7.values.forEach { level: Int ->
                    with(
                        buildWhen(RagiumBlocks.Properties.LEVEL_7, level),
                        buildStateVariant {
                            model(
                                Ragium.id(
                                    when (level % 2 == 0) {
                                        true -> "block/manual_grinder"
                                        else -> "block/manual_grinder_diagonal"
                                    }
                                )

                            )
                            rotY(
                                when (level / 2) {
                                    0 -> VariantSettings.Rotation.R0
                                    1 -> VariantSettings.Rotation.R90
                                    2 -> VariantSettings.Rotation.R180
                                    3 -> VariantSettings.Rotation.R270
                                    else -> throw IllegalStateException()
                                }
                            )
                        }
                    )
                }
            }
        )

        generator.registerSingleton(
            RagiumBlocks.BURNING_BOX,
            TexturedModel.makeFactory(
                { block: Block ->
                    TextureMap()
                        .put(TextureKey.TOP, TextureMap.getId(Blocks.BRICKS))
                        .put(TextureKey.BOTTOM, TextureMap.getId(Blocks.BRICKS))
                        .put(TextureKey.FRONT, TextureMap.getSubId(block, "_front"))
                },
                RagiumModels.MACHINE
            )
        )
        // tier2
        generator.registerSimpleCubeAll(RagiumBlocks.RAGI_STEEL_BLOCK)
        generator.registerSingleton(RagiumBlocks.RAGI_STEEL_HULL, hullTextureFactory)
        // tier3
        generator.registerSimpleCubeAll(RagiumBlocks.REFINED_RAGI_STEEL_BLOCK)
        generator.registerSingleton(RagiumBlocks.REFINED_RAGI_STEEL_HULL, hullTextureFactory)
        // tier4
        // tier5

        // fluids
        RagiumFluids.getFilteredInstances<HTFluidContent>().forEach { it.generateBlockState(generator) }
    }

    private val hullTextureFactory: TexturedModel.Factory = TexturedModel.makeFactory({ block: Block ->
        TextureMap()
            .put(
                TextureKey.TOP, when (block) {
                    RagiumBlocks.RAGI_ALLOY_HULL -> RagiumBlocks.RAGI_ALLOY_BLOCK
                    RagiumBlocks.RAGI_STEEL_HULL -> RagiumBlocks.RAGI_STEEL_BLOCK
                    RagiumBlocks.REFINED_RAGI_STEEL_HULL -> RagiumBlocks.REFINED_RAGI_STEEL_BLOCK
                    else -> throw IllegalStateException()
                }.let(TextureMap::getId)
            )
            .put(
                TextureKey.BOTTOM,
                when (block) {
                    RagiumBlocks.RAGI_ALLOY_HULL -> Blocks.BRICKS
                    RagiumBlocks.RAGI_STEEL_HULL -> Blocks.DEEPSLATE_TILES
                    RagiumBlocks.REFINED_RAGI_STEEL_HULL -> Blocks.CHISELED_QUARTZ_BLOCK
                    else -> throw IllegalStateException()
                }.let(TextureMap::getId)
            )

    }, RagiumModels.HULL)

    private fun registerMachines(generator: BlockStateModelGenerator) {
        HTMachineType.getEntries().forEach { type: HTMachineType ->
            generator.registerNorthDefaultHorizontalRotated(
                type.block,
                TexturedModel.makeFactory({ createMachineTextureMap(type) }, RagiumModels.MACHINE)
            )
        }
    }

    private fun createMachineTextureMap(type: HTMachineType): TextureMap {
        val topTex: Identifier = when (type.tier) {
            HTMachineTier.HEAT -> "block/ragi_alloy_block"
            HTMachineTier.KINETIC -> "block/ragi_steel_block"
            else -> "block/refined_ragi_steel_block"
        }.let(Ragium::id)
        val bottomTex: Identifier = when (type.tier) {
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
        generator.register(RagiumItems.POWER_METER, Models.GENERATED)
        // tier1
        generator.register(RagiumItems.RAW_RAGINITE, Models.GENERATED)
        generator.register(RagiumItems.RAW_RAGINITE_DUST, Models.GENERATED)
        generator.register(RagiumItems.RAGINITE_DUST, Models.GENERATED)
        generator.register(RagiumItems.RAGI_ALLOY_COMPOUND, Models.GENERATED)
        generator.register(RagiumItems.RAGI_ALLOY_INGOT, Models.GENERATED)
        generator.register(RagiumItems.RAGI_ALLOY_PLATE, Models.GENERATED)
        generator.register(RagiumItems.RAGI_ALLOY_ROD, Models.GENERATED)
        // tier2
        generator.register(RagiumItems.RAGI_STEEL_INGOT, Models.GENERATED)
        generator.register(RagiumItems.RAGI_STEEL_PLATE, Models.GENERATED)
        // tier3
        generator.register(RagiumItems.REFINED_RAGI_STEEL_INGOT, Models.GENERATED)
        generator.register(RagiumItems.REFINED_RAGI_STEEL_PLATE, Models.GENERATED)
        // tier4

        // tier5

        // fluids
        RagiumFluids.getFilteredInstances<HTFluidContent>().forEach { it.generateBucketModel(generator) }
    }
}