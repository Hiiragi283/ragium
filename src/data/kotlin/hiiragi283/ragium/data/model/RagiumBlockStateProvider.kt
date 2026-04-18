package hiiragi283.ragium.data.model

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.model.HTBlockStateProvider
import hiiragi283.core.api.data.model.blockTexture
import hiiragi283.core.api.data.model.existsTexture
import hiiragi283.core.api.data.model.fixedBlockTexture
import hiiragi283.core.api.data.model.withExistingParent
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.blockId
import hiiragi283.core.api.resource.toId
import hiiragi283.core.common.block.HTHorizontalEntityBlock
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.block.HTMachineBlock
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.common.data.ExistingFileHelper

class RagiumBlockStateProvider(fileHelper: ExistingFileHelper, output: PackOutput) :
    HTBlockStateProvider(fileHelper, output, RagiumAPI.MOD_ID) {
    val basic = "basic"
    val heat = "heat"
    val cool = "cool"
    val chemical = "chemical"

    override fun registerStatesAndModels() {
        // Machine
        frontMachineBlock(RagiumBlocks.ALLOY_SMELTER, RagiumConst.MACHINE, basic)
        frontMachineBlock(RagiumBlocks.ASSEMBLER, RagiumConst.MACHINE, basic)
        frontMachineBlock(RagiumBlocks.AUTO_CHISEL, RagiumConst.MACHINE, basic)
        frontMachineBlock(RagiumBlocks.CRUSHER, RagiumConst.MACHINE, basic)
        frontMachineBlock(RagiumBlocks.CUTTING_MACHINE, RagiumConst.MACHINE, basic)
        frontMachineBlock(RagiumBlocks.ELECTRIC_FURNACE, RagiumConst.MACHINE, basic)

        frontMachineBlock(RagiumBlocks.FREEZER, RagiumConst.MACHINE, cool)
        frontMachineBlock(RagiumBlocks.MELTER, RagiumConst.MACHINE, heat)
        frontMachineBlock(RagiumBlocks.PYROLYZER, RagiumConst.MACHINE, heat)
        refineryBlock(RagiumBlocks.REFINERY)
        frontMachineBlock(RagiumBlocks.WASHER, RagiumConst.MACHINE, cool)

        val mixerFront: ResourceLocation = RagiumAPI.id(HTConst.BLOCK, RagiumConst.MACHINE, RagiumConst.MIXER)
        frontMachineBlock(RagiumBlocks.BREWERY, RagiumConst.MACHINE, chemical)
        refineryBlock(RagiumBlocks.CHEMICAL_WASHER)
        frontMachineBlock(RagiumBlocks.FLUID_MIXER, RagiumConst.MACHINE, chemical, mixerFront)
        frontMachineBlock(RagiumBlocks.MIXER, RagiumConst.MACHINE, chemical)

        // Storage
        variableBlock(RagiumBlocks.BATTERY, RagiumBlocks.CREATIVE_BATTERY)
        variableBlock(RagiumBlocks.CRATE, RagiumBlocks.CREATIVE_CRATE)

        val tankFactory: (HTIdLike) -> Array<ConfiguredModel> = { block: HTIdLike ->
            ConfiguredModel
                .builder()
                .modelFile(
                    models()
                        .withExistingParent(block, RagiumAPI.id(HTConst.BLOCK, "tank_template"))
                        .fixedBlockTexture("side", block)
                        .fixedBlockTexture("top", block),
                ).build()
        }
        simpleBlockAndItem(RagiumBlocks.TANK, tankFactory, itemFactory = { builtIn })
        simpleBlockAndItem(RagiumBlocks.VOID_TANK, tankFactory)
        simpleBlockAndItem(RagiumBlocks.CREATIVE_TANK, models().getExistingFile(RagiumBlocks.TANK.blockId), builtIn)

        layeredBlock(
            RagiumBlocks.UNIVERSAL_CHEST,
            HTConst.MINECRAFT.toId("block", "white_concrete"),
            RagiumBlocks.UNIVERSAL_CHEST.blockId,
        )

        // Utilities
        cutoutSimpleBlock(RagiumBlocks.IMITATION_SPAWNER)

        // Fluid
        RagiumFluids.REGISTER.asSequence().forEach(::liquidBlock)
    }

    //    Extensions    //

    private fun variableBlock(base: HTBlockHolderLike<*>, creative: HTBlockHolderLike<*>) {
        val model: ModelFile.ExistingModelFile = models().getExistingFile(base.blockId)
        simpleBlockAndItem(base, model, builtIn)
        simpleBlockAndItem(creative, model, builtIn)
    }

    private fun machineBlock(block: HTHolderLike<Block, *>, model: ModelFile) {
        getVariantBuilder(block.get())
            .forAllStates { state: BlockState ->
                ConfiguredModel
                    .builder()
                    .modelFile(model)
                    .rotationY(state.getValue(HTHorizontalEntityBlock.FACING).getRotationY())
                    .build()
            }
        itemModels().simpleBlockItem(block.getId())
    }

    private fun refineryBlock(block: HTHolderLike<Block, *>) {
        machineBlock(
            block,
            models()
                .withExistingParent(block, RagiumAPI.id(HTConst.BLOCK, "refinery_template"))
                .blockTexture("all", block),
        )
    }

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
        if (models().existsTexture(front)) {
            inactive.texture("front", front)
        }
        // active
        val active: BlockModelBuilder = models()
            .withExistingParent("${path}_active", modelId)
            .texture("top", top)
            .texture("side", side)
            .texture("bottom", bottom)
        val frontActive: ResourceLocation = front.withSuffix("_active")
        if (models().existsTexture(frontActive)) {
            active.texture("front", frontActive)
        }
        return inactive to active
    }
}
