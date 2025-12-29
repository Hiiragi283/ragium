package hiiragi283.ragium.data.client.model

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.model.HTBlockStateProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.resource.blockId
import hiiragi283.core.api.resource.vanillaId
import hiiragi283.core.common.block.HTHorizontalEntityBlock
import hiiragi283.core.common.registry.HTSimpleDeferredBlock
import hiiragi283.ragium.api.RagiumAPI
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
    val advancedCasing: ResourceLocation = RagiumAPI.id(HTConst.BLOCK, "advanced_machine_casing")
    val advancedFrame: ResourceLocation = RagiumAPI.id(HTConst.BLOCK, "advanced_machine_frame")

    val blackstone: ResourceLocation = vanillaId("block", "polished_blackstone_bricks")
    
    override fun registerStatesAndModels() {
        registerMaterials()

        // Processors
        machineBlock(RagiumBlocks.MELTER, advancedCasing, blackstone)

        // Storages
        altModelBlock(RagiumBlocks.TANK)

        // Fluid
        RagiumFluids.REGISTER.entries.forEach(::liquidBlock)
    }

    private fun registerMaterials() {
        RagiumBlocks.MATERIALS.forEach { (prefix: HTMaterialPrefix, material: HTMaterialKey, block: HTSimpleDeferredBlock) ->
            val textureId: ResourceLocation = RagiumAPI.id(HTConst.BLOCK, prefix.name, material.name)
            existTexture(block, textureId, ::altTextureBlock)
        }
    }

    //    Extensions    //

    private fun machineBlock(
        block: HTHolderLike<Block, *>,
        top: ResourceLocation,
        bottom: ResourceLocation,
        front: ResourceLocation = block.blockId.withSuffix("_front"),
    ) {
        val (inactive: BlockModelBuilder, active: BlockModelBuilder) = machineModel(block, top, bottom, front)
        machineBlock(block, inactive, active)
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

    private fun machineModel(
        block: HTHolderLike<Block, *>,
        top: ResourceLocation,
        bottom: ResourceLocation,
        front: ResourceLocation = block.blockId.withSuffix("_front"),
    ): Pair<BlockModelBuilder, BlockModelBuilder> {
        val path: String = block.blockId.path
        // inactive
        val inactive: BlockModelBuilder = models()
            .withExistingParent(path, RagiumAPI.id(HTConst.BLOCK, "machine_base"))
            .texture("top", top)
            .texture("bottom", bottom)
            .texture("front", front)
        // active
        val active: BlockModelBuilder = models()
            .withExistingParent("${path}_active", RagiumAPI.id(HTConst.BLOCK, "machine_base"))
            .texture("top", top)
            .texture("bottom", bottom)
            .texture("front", front.withSuffix("_active"))
        return inactive to active
    }
}
