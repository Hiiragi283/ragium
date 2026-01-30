package hiiragi283.ragium.data.client.model

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.model.HTBlockStateProvider
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.resource.blockId
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.block.HTHorizontalEntityBlock
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.block.HTMachineBlock
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.client.model.generators.ModelFile

class RagiumBlockStateProvider(context: HTDataGenContext) : HTBlockStateProvider(RagiumAPI.MOD_ID, context) {
    val basic = "basic"
    val heat = "heat"

    override fun registerStatesAndModels() {
        registerMaterials()

        // Machine
        frontMachineBlock(RagiumBlocks.ALLOY_SMELTER, RagiumConst.MACHINE, basic)
        frontMachineBlock(RagiumBlocks.CRUSHER, RagiumConst.MACHINE, basic)
        frontMachineBlock(RagiumBlocks.CUTTING_MACHINE, RagiumConst.MACHINE, basic)
        frontMachineBlock(RagiumBlocks.ELECTRIC_FURNACE, RagiumConst.MACHINE, basic)

        frontMachineBlock(RagiumBlocks.MELTER, RagiumConst.MACHINE, heat)
        frontMachineBlock(RagiumBlocks.PYROLYZER, RagiumConst.MACHINE, heat)

        // Device
        // frontMachineBlock(RagiumBlocks.PLANTER, RagiumConst.DEVICE, basic)

        // Storage
        altModelBlock(RagiumBlocks.TANK)
        altModelBlock(RagiumBlocks.CREATIVE_TANK, id = RagiumBlocks.TANK.blockId)

        layeredBlock(
            RagiumBlocks.UNIVERSAL_CHEST,
            HTConst.MINECRAFT.toId("block", "white_concrete"),
            RagiumBlocks.UNIVERSAL_CHEST.blockId,
        )

        // Utilities
        cutoutSimpleBlock(RagiumBlocks.IMITATION_SPAWNER)

        // Fluid
        RagiumFluids.REGISTER
            .asSequence()
            .filterIsInstance<HTFluidContent.Flowing<*, *, *, *>>()
            .forEach(::liquidBlock)
    }

    private fun registerMaterials() {
        registerOres()

        registerMaterials(CommonTagPrefixes.BLOCK)
        registerMaterials(CommonTagPrefixes.RAW_BLOCK)
    }

    //    Extensions    //

    private fun machineBlock(block: HTHolderLike<Block, *>, inactive: ModelFile, active: ModelFile) {
        getVariantBuilder(block.get())
            .forAllStates { state: BlockState ->
                ConfiguredModel
                    .builder()
                    .modelFile(
                        when (state.getValue(HTMachineBlock.IS_ACTIVE)) {
                            true -> active
                            false -> inactive
                        },
                    ).rotationY(state.getValue(HTHorizontalEntityBlock.FACING).getRotationY())
                    .build()
            }
        itemModels().simpleBlockItem(block.getId())
    }

    private fun frontMachineBlock(
        block: HTHolderLike<Block, *>,
        prefix: String,
        tier: String,
        front: ResourceLocation = block.getId().withPath { "${HTConst.BLOCK}/$prefix/${it}_front" },
    ) {
        val (inactive: BlockModelBuilder, active: BlockModelBuilder) = frontMachineModel(block, prefix, tier, front)
        machineBlock(block, inactive, active)
    }

    /**
     * @see net.minecraft.data.models.model.ModelTemplates.CUBE_ORIENTABLE_TOP_BOTTOM
     */
    private fun frontMachineModel(
        block: HTHolderLike<Block, *>,
        prefix: String,
        tier: String,
        front: ResourceLocation,
    ): Pair<BlockModelBuilder, BlockModelBuilder> {
        val path: String = block.blockId.path
        val modelId: ResourceLocation = HTConst.MINECRAFT.toId(HTConst.BLOCK, "orientable_with_bottom")

        val top: ResourceLocation = RagiumAPI.id(HTConst.BLOCK, prefix, "top_$tier")
        val side: ResourceLocation = RagiumAPI.id(HTConst.BLOCK, prefix, "side_$tier")
        val bottom: ResourceLocation = RagiumAPI.id(HTConst.BLOCK, prefix, "bottom")
        // inactive
        val inactive: BlockModelBuilder = models()
            .withExistingParent(path, modelId)
            .texture("top", top)
            .texture("side", side)
            .texture("bottom", bottom)
            .texture("front", front)
        // active
        val active: BlockModelBuilder = models()
            .withExistingParent("${path}_active", modelId)
            .texture("top", top)
            .texture("side", side)
            .texture("bottom", bottom)
            .texture("front", front.withSuffix("_active"))
        return inactive to active
    }
}
