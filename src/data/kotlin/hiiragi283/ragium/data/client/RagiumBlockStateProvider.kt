package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.altModelBlock
import hiiragi283.ragium.api.extension.altTextureBlock
import hiiragi283.ragium.api.extension.cubeColumn
import hiiragi283.ragium.api.extension.cutoutSimpleBlock
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.extension.layeredBlock
import hiiragi283.ragium.api.extension.rowValues
import hiiragi283.ragium.api.extension.translucentSimpleBlock
import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.registry.HTBlockHolderLike
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import hiiragi283.ragium.common.block.HTCropBlock
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredBlock
import java.util.function.Supplier

class RagiumBlockStateProvider(output: PackOutput, exFileHelper: ExistingFileHelper) :
    BlockStateProvider(output, RagiumAPI.MOD_ID, exFileHelper) {
    override fun registerStatesAndModels() {
        // Simple Blocks
        buildList {
            add(RagiumBlocks.CRIMSON_SOIL)
            add(RagiumBlocks.SILT)

            addAll(RagiumBlocks.DECORATION_MAP.values)
            addAll(RagiumBlocks.MATERIALS.rowValues(HTMaterialVariant.STORAGE_BLOCK))
            addAll(RagiumBlocks.LEDBlocks.entries)

            add(RagiumBlocks.Casings.DEVICE.holder)

            add(RagiumBlocks.Devices.CEU)
            add(RagiumBlocks.Devices.ENI)
            add(RagiumBlocks.Devices.EXP_COLLECTOR)
            add(RagiumBlocks.Devices.ITEM_BUFFER)
            add(RagiumBlocks.Devices.SPRINKLER)
            add(RagiumBlocks.Devices.DIM_ANCHOR)
        }.map(Supplier<out Block>::get).forEach(::simpleBlock)

        layeredBlock(
            RagiumBlocks.MYSTERIOUS_OBSIDIAN,
            vanillaId("block/obsidian"),
            RagiumAPI.id("block/mysterious_obsidian"),
        )

        for (slab: RagiumBlocks.Slabs in RagiumBlocks.Slabs.entries) {
            val textureName: ResourceLocation = slab.variant.textureName
            slabBlock(slab.holder.get(), textureName, textureName)
        }
        for (stair: RagiumBlocks.Stairs in RagiumBlocks.Stairs.entries) {
            val textureName: ResourceLocation = stair.variant.textureName
            stairsBlock(stair.holder.get(), textureName)
        }
        for (wall: RagiumBlocks.Walls in RagiumBlocks.Walls.entries) {
            val textureName: ResourceLocation = wall.variant.textureName
            wallBlock(wall.holder.get(), textureName)
        }

        RagiumBlocks.MATERIALS.rowValues(HTMaterialVariant.GLASS_BLOCK).forEach(::cutoutSimpleBlock)
        RagiumBlocks.MATERIALS.rowValues(HTMaterialVariant.TINTED_GLASS_BLOCK).forEach(::translucentSimpleBlock)
        Blocks.TINTED_GLASS

        // Ore
        RagiumBlocks.ORES.forEach { (variant: HTMaterialVariant, material: HTMaterialType, ore: DeferredBlock<*>) ->
            val textureId: String = RagiumAPI.id("block/${material.serializedName}").toString()
            val stoneTex: String = when (variant) {
                HTMaterialVariant.ORE -> "block/stone"
                HTMaterialVariant.DEEP_ORE -> "block/deepslate"
                HTMaterialVariant.NETHER_ORE -> "block/netherrack"
                HTMaterialVariant.END_ORE -> "block/end_stone"
                else -> null
            } ?: return@forEach
            simpleBlock(
                ore.get(),
                ConfiguredModel(
                    models()
                        .withExistingParent(ore.id.path, RagiumAPI.id("block/layered"))
                        .texture("layer0", vanillaId(stoneTex))
                        .texture("layer1", textureId)
                        .renderType("cutout"),
                ),
            )
        }

        cubeColumn(RagiumBlocks.RESONANT_DEBRIS)
        // Log
        logBlockWithRenderType(RagiumBlocks.ASH_LOG.get(), "cutout")

        // Crop
        getVariantBuilder(RagiumBlocks.EXP_BERRY_BUSH.get())
            .forAllStates { state: BlockState ->
                val age: Int = state.getValue(HTCropBlock.AGE)
                val id: ResourceLocation = RagiumBlocks.EXP_BERRY_BUSH.id.withSuffix("_stage$age")
                ConfiguredModel
                    .builder()
                    .modelFile(
                        models()
                            .withExistingParent(id.path, "cross")
                            .texture("cross", id.withPrefix("block/"))
                            .renderType("cutout"),
                    ).build()
            }

        getVariantBuilder(RagiumBlocks.WARPED_WART.get())
            .forAllStates { state: BlockState ->
                Blocks.NETHER_WART
                val age: Int = when (state.getValue(HTCropBlock.AGE)) {
                    0 -> 0
                    1 -> 1
                    2 -> 1
                    else -> 2
                }
                val id: ResourceLocation = RagiumBlocks.WARPED_WART.id.withSuffix("_stage$age")
                ConfiguredModel
                    .builder()
                    .modelFile(
                        models()
                            .withExistingParent(id.path, "crop")
                            .texture("crop", id.withPrefix("block/"))
                            .renderType("cutout"),
                    ).build()
            }

        // Food
        altModelBlock(RagiumBlocks.SWEET_BERRIES_CAKE)

        // Machine Frame
        altTextureBlock(RagiumBlocks.Casings.WOODEN, vanillaId("block/note_block"))

        cubeColumn(
            RagiumBlocks.Casings.STONE,
            vanillaId("block/furnace_side"),
            vanillaId("block/furnace_top"),
        )

        RagiumBlocks.Frames.entries.forEach(::cutoutSimpleBlock)

        // Machine
        fun machine(holder: HTBlockHolderLike, top: ResourceLocation, bottom: ResourceLocation) {
            horizontalBlock(
                holder.get(),
                models()
                    .withExistingParent("block/" + holder.id.path, RagiumAPI.id("block/machine_base"))
                    .texture("top", top)
                    .texture("bottom", bottom)
                    .texture("front", holder.id.withPath { "block/${it}_front" }),
            )
        }

        val basicMachine: ResourceLocation = RagiumAPI.id("block/basic_machine_casing")
        machine(RagiumBlocks.Machines.CRUSHER, basicMachine, vanillaId("block/bricks"))
        machine(RagiumBlocks.Machines.BLOCK_BREAKER, basicMachine, vanillaId("block/bricks"))
        machine(RagiumBlocks.Machines.COMPRESSOR, basicMachine, vanillaId("block/bricks"))
        machine(RagiumBlocks.Machines.ENGRAVER, basicMachine, vanillaId("block/bricks"))
        machine(RagiumBlocks.Machines.EXTRACTOR, basicMachine, vanillaId("block/bricks"))
        machine(RagiumBlocks.Machines.PULVERIZER, basicMachine, vanillaId("block/bricks"))

        val advancedMachine: ResourceLocation = RagiumAPI.id("block/advanced_machine_casing")
        machine(RagiumBlocks.Machines.ALLOY_SMELTER, advancedMachine, vanillaId("block/nether_bricks"))
        machine(RagiumBlocks.Machines.INFUSER, advancedMachine, vanillaId("block/polished_blackstone_bricks"))
        machine(RagiumBlocks.Machines.MELTER, advancedMachine, vanillaId("block/polished_blackstone_bricks"))
        machine(RagiumBlocks.Machines.MIXER, advancedMachine, vanillaId("block/polished_blackstone_bricks"))
        machine(RagiumBlocks.Machines.REFINERY, advancedMachine, vanillaId("block/polished_blackstone_bricks"))
        machine(RagiumBlocks.Machines.SOLIDIFIER, advancedMachine, vanillaId("block/polished_blackstone_bricks"))

        // Device
        layeredBlock(
            RagiumBlocks.Devices.WATER_COLLECTOR,
            vanillaId("block/water_still"),
            RagiumAPI.id("block/device_overlay"),
        )

        layeredBlock(
            RagiumBlocks.Devices.LAVA_COLLECTOR,
            vanillaId("block/lava_still"),
            RagiumAPI.id("block/device_overlay"),
        )

        altModelBlock(RagiumBlocks.Devices.MILK_DRAIN)

        // Storages
        for (drum: RagiumBlocks.Drums in RagiumBlocks.Drums.entries) {
            val id: ResourceLocation = drum.blockId
            simpleBlock(
                drum.get(),
                models().cubeColumn(
                    id.path,
                    id.withSuffix("_side"),
                    id.withSuffix("_top"),
                ),
            )
        }
    }

    //    Extensions    //

    /*private fun Direction.getRotationY(): Int = ((this.toYRot() + 180) % 360).toInt()

    private fun <T : Any> ConfiguredModel.Builder<T>.rotationY(state: BlockState): ConfiguredModel.Builder<T> =
        rotationY(state.getValue(HTBlockStateProperties.HORIZONTAL).getRotationY())

    private fun cauldronBlock(holder: DeferredBlock<*>) {
        if (holder == RagiumBlocks.HONEY_CAULDRON) return
        getVariantBuilder(holder.get())
            .forAllStates { state: BlockState ->
                val level: Int = state.getValue(LayeredCauldronBlock.LEVEL)
                val suffix: String = when (level) {
                    3 -> "_full"
                    else -> "_level$level"
                }
                ConfiguredModel
                    .builder()
                    .modelFile(modelFile(vanillaId("block/water_cauldron").withSuffix(suffix)))
                    .build()
            }
    }

    private fun feastBlock(holder: DeferredBlock<out HTFeastBlock>) {
        getVariantBuilder(holder.get()).forAllStates { state: BlockState ->
            val block: HTFeastBlock = holder.get()
            val property: IntegerProperty = block.getServingsProperty()
            val servings: Int = state.getValue(property)
            val suffix: String =
                if (servings == 0) {
                    if (block.hasLeftovers) {
                        "_leftover"
                    } else {
                        "_stage${property.possibleValues.size - 2}"
                    }
                } else {
                    "_stage${block.getMaxServings() - servings}"
                }
            ConfiguredModel
                .builder()
                .modelFile(modelFile(holder.blockId.withSuffix(suffix)))
                .rotationY(state)
                .build()
        }
    }*/
}
