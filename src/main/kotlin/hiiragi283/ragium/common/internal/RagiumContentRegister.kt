package hiiragi283.ragium.common.internal

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTBlockWithEntity
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.content.HTItemContent
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.fluid.HTVirtualFluid
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.block.*
import hiiragi283.ragium.common.block.storage.HTBackpackInterfaceBlock
import hiiragi283.ragium.common.block.storage.HTCrateBlock
import hiiragi283.ragium.common.block.storage.HTDrumBlock
import hiiragi283.ragium.common.block.transfer.*
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.item.HTRopeBlockItem
import net.minecraft.block.*
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.tag.FluidTags
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.text.Text
import net.minecraft.util.Rarity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import java.util.function.UnaryOperator

internal object RagiumContentRegister {
    //    Block    //
    @Deprecated("")
    private fun registerBlock(name: String, block: Block): Block = Registry.register(Registries.BLOCK, RagiumAPI.id(name), block)

    private fun registerBlockNew(
        content: HTBlockContent,
        parent: AbstractBlock.Settings = blockSettings(),
        block: (AbstractBlock.Settings) -> Block,
    ): Block = Registry.register(Registries.BLOCK, content.id, block(parent))

    //    Item    //
    @Deprecated("")
    private fun registerItem(name: String, item: Item): Item = Registry.register(Registries.ITEM, RagiumAPI.id(name), item)

    private fun registerItemNew(content: HTItemContent, item: (Item.Settings) -> Item = ::Item): Item =
        Registry.register(Registries.ITEM, content.id, item(itemSettings()))

    @Deprecated("")
    private fun <T : Block> registerBlockItem(
        block: T,
        settings: Item.Settings = itemSettings(),
        factory: (T, Item.Settings) -> Item = ::BlockItem,
    ) {
        registerItem(
            Registries.BLOCK
                .getKeyOrThrow(block)
                .value
                .path,
            factory(block, settings),
        )
    }

    private fun registerBlockItemNew(block: HTBlockContent, settings: UnaryOperator<Item.Settings> = UnaryOperator.identity()) {
        Registry.register(Registries.ITEM, block.id, BlockItem(block.get(), settings.apply(itemSettings())))
    }

    private fun registerBlockItemNew(block: HTBlockContent, factory: (Block, Item.Settings) -> Item) {
        Registry.register(Registries.ITEM, block.id, factory(block.get(), itemSettings()))
    }

    //    Init    //

    @JvmStatic
    fun registerContents() {
        // block
        RagiumContents.Ores.entries.forEach { ore: RagiumContents.Ores ->
            registerBlockNew(ore, blockSettings(ore.baseStone), ::Block)
            registerBlockItemNew(ore) { it.material(ore.material, ore.tagPrefix) }
        }
        RagiumContents.StorageBlocks.entries.forEach { storage: RagiumContents.StorageBlocks ->
            registerBlockNew(storage, blockSettings(Blocks.IRON_BLOCK), ::Block)
            registerBlockItemNew(storage) { it.material(storage.material, storage.tagPrefix) }
        }

        RagiumContents.Grates.entries.forEach { grate: RagiumContents.Grates ->
            registerBlockNew(grate, blockSettings(Blocks.COPPER_GRATE), ::TransparentBlock)
            registerBlockItemNew(grate) { it.tieredText(RagiumTranslationKeys.GRATE, grate.tier) }
        }
        RagiumContents.Casings.entries.forEach { casings: RagiumContents.Casings ->
            registerBlockNew(casings, blockSettings(Blocks.SMOOTH_STONE), ::Block)
            registerBlockItemNew(casings) { it.tieredText(RagiumTranslationKeys.CASING, casings.tier) }
        }
        RagiumContents.Hulls.entries.forEach { hull: RagiumContents.Hulls ->
            registerBlockNew(hull, blockSettings(Blocks.SMOOTH_STONE), ::TransparentBlock)
            registerBlockItemNew(hull) { it.tieredText(RagiumTranslationKeys.HULL, hull.tier) }
        }
        RagiumContents.Coils.entries.forEach { coil: RagiumContents.Coils ->
            registerBlockNew(coil, blockSettings(Blocks.COPPER_BLOCK), ::PillarBlock)
            registerBlockItemNew(coil) { it.tieredText(RagiumTranslationKeys.COIL, coil.tier) }
        }
        RagiumContents.Exporters.entries.forEach { exporter: RagiumContents.Exporters ->
            registerBlockNew(exporter) { HTExporterBlock(exporter.tier, it) }
            registerBlockItemNew(exporter) { it.tieredText(RagiumTranslationKeys.EXPORTER, exporter.tier) }
        }
        RagiumContents.Pipes.entries.forEach { pipe: RagiumContents.Pipes ->
            registerBlockNew(pipe) { HTSimplePipeBlock(pipe.tier, pipe.pipeType, it) }
            registerBlockItemNew(pipe) { it.tier(pipe.tier) }
        }
        RagiumContents.CrossPipes.entries.forEach { crossPipe: RagiumContents.CrossPipes ->
            registerBlockNew(crossPipe, block = ::HTPipeBlock)
            registerBlockItemNew(crossPipe)
        }
        RagiumContents.PipeStations.entries.forEach { station: RagiumContents.PipeStations ->
            registerBlockNew(station, block = ::HTPipeStationBlock)
            registerBlockItemNew(station) {
                it.descriptions(Text.translatable(RagiumTranslationKeys.PIPE_STATION))
            }
        }
        RagiumContents.FilteringPipe.entries.forEach { filtering: RagiumContents.FilteringPipe ->
            registerBlockNew(filtering) { HTFilteringPipeBlock(filtering.pipeType, it) }
            registerBlockItemNew(filtering) {
                it
                    .descriptions(Text.translatable(RagiumTranslationKeys.PIPE_STATION))
                    .component(RagiumComponentTypes.REWORK_TARGET, Unit)
            }
        }
        RagiumContents.Crates.entries.forEach { crate: RagiumContents.Crates ->
            registerBlockNew(crate, blockSettings(Blocks.SMOOTH_STONE)) { HTCrateBlock(crate.tier, it) }
            registerBlockItemNew(crate) { it.tieredText(RagiumTranslationKeys.CRATE, crate.tier) }
        }
        RagiumContents.Drums.entries.forEach { drum: RagiumContents.Drums ->
            registerBlockNew(drum, blockSettings(Blocks.SMOOTH_STONE)) { HTDrumBlock(drum.tier, it) }
            registerBlockItemNew(drum) { it.tieredText(RagiumTranslationKeys.DRUM, drum.tier) }
        }
        initBlocks()

        // item
        buildList {
            addAll(RagiumContents.Dusts.entries)
            addAll(RagiumContents.Gears.entries)
            addAll(RagiumContents.Gems.entries)
            addAll(RagiumContents.Ingots.entries)
            addAll(RagiumContents.Plates.entries)
            addAll(RagiumContents.RawMaterials.entries)
        }.forEach { content ->
            registerItemNew(content) { Item(it.material(content.material, content.tagPrefix)) }
        }
        RagiumContents.CircuitBoards.entries.forEach { board: RagiumContents.CircuitBoards ->
            registerItemNew(board) { Item(it.tieredText(RagiumTranslationKeys.CIRCUIT_BOARD, board.tier)) }
        }
        RagiumContents.Circuits.entries.forEach { circuit: RagiumContents.Circuits ->
            registerItemNew(circuit) { Item(it.tieredText(RagiumTranslationKeys.CIRCUIT, circuit.tier)) }
        }
        RagiumContents.PressMolds.entries.forEach(::registerItemNew)

        initItems()

        // fluid
        RagiumFluids.entries.forEach { fluid: RagiumFluids ->
            Registry.register(Registries.FLUID, fluid.id, HTVirtualFluid())
        }
    }

    @JvmStatic
    private fun initBlocks() {
        registerBlockNew(RagiumBlocksNew.CREATIVE_CRATE) {
            HTBlockWithEntity.buildHorizontal(
                RagiumBlockEntityTypes.CREATIVE_CRATE,
                it.mapColor(MapColor.PURPLE).requiresTool().strength(2f, 6f),
            )
        }
        registerBlockNew(RagiumBlocksNew.CREATIVE_DRUM) {
            HTBlockWithEntity.build(
                RagiumBlockEntityTypes.CREATIVE_DRUM,
                it.mapColor(MapColor.PURPLE).requiresTool().strength(2f, 6f),
            )
        }
        registerBlockNew(RagiumBlocksNew.CREATIVE_EXPORTER) {
            HTCreativeExporterBlock(
                it
                    .mapColor(MapColor.PURPLE)
                    .requiresTool()
                    .strength(2f, 6f)
                    .solid()
                    .nonOpaque(),
            )
        }
        registerBlockNew(RagiumBlocksNew.CREATIVE_SOURCE) {
            HTBlockWithEntity.build(
                RagiumBlockEntityTypes.CREATIVE_SOURCE,
                it.mapColor(MapColor.PURPLE).requiresTool().strength(2f, 6f),
            )
        }
        registerBlockItemNew(RagiumBlocksNew.CREATIVE_CRATE) { it.rarity(Rarity.EPIC) }
        registerBlockItemNew(RagiumBlocksNew.CREATIVE_DRUM) { it.rarity(Rarity.EPIC) }
        registerBlockItemNew(RagiumBlocksNew.CREATIVE_EXPORTER) { it.rarity(Rarity.EPIC) }
        registerBlockItemNew(RagiumBlocksNew.CREATIVE_SOURCE) { it.rarity(Rarity.EPIC) }

        registerBlockNew(RagiumBlocksNew.MUTATED_SOIL) {
            Block(it.mapColor(MapColor.GREEN).strength(0.5f).sounds(BlockSoundGroup.GRAVEL))
        }
        registerBlockNew(RagiumBlocksNew.POROUS_NETHERRACK) {
            HTSpongeBlock(
                it
                    .mapColor(MapColor.DARK_RED)
                    .requiresTool()
                    .strength(0.4f)
                    .sounds(BlockSoundGroup.NETHERRACK),
                Blocks.MAGMA_BLOCK::getDefaultState,
            ) { world: World, pos: BlockPos ->
                world.getFluidState(pos).isIn(FluidTags.LAVA)
            }
        }
        registerBlockItemNew(RagiumBlocksNew.MUTATED_SOIL) {
            it.descriptions(Text.translatable(RagiumTranslationKeys.MUTATED_SOIL))
        }
        registerBlockItemNew(RagiumBlocksNew.POROUS_NETHERRACK) {
            it.descriptions(Text.translatable(RagiumTranslationKeys.POROUS_NETHERRACK))
        }

        registerBlock("asphalt", RagiumBlocks.ASPHALT)
        registerBlock("asphalt_slab", RagiumBlocks.ASPHALT_SLAB)
        registerBlock("asphalt_stairs", RagiumBlocks.ASPHALT_STAIRS)
        registerBlock("polished_asphalt", RagiumBlocks.POLISHED_ASPHALT)
        registerBlock("polished_asphalt_slab", RagiumBlocks.POLISHED_ASPHALT_SLAB)
        registerBlock("polished_asphalt_stairs", RagiumBlocks.POLISHED_ASPHALT_STAIRS)
        registerBlock("gypsum", RagiumBlocks.GYPSUM)
        registerBlock("gypsum_slab", RagiumBlocks.GYPSUM_SLAB)
        registerBlock("gypsum_stairs", RagiumBlocks.GYPSUM_STAIRS)
        registerBlock("polished_gypsum", RagiumBlocks.POLISHED_GYPSUM)
        registerBlock("polished_gypsum_slab", RagiumBlocks.POLISHED_GYPSUM_SLAB)
        registerBlock("polished_gypsum_stairs", RagiumBlocks.POLISHED_GYPSUM_STAIRS)
        registerBlock("slate", RagiumBlocks.SLATE)
        registerBlock("slate_slab", RagiumBlocks.SLATE_SLAB)
        registerBlock("slate_stairs", RagiumBlocks.SLATE_STAIRS)
        registerBlock("polished_slate", RagiumBlocks.POLISHED_SLATE)
        registerBlock("polished_slate_slab", RagiumBlocks.POLISHED_SLATE_SLAB)
        registerBlock("polished_slate_stairs", RagiumBlocks.POLISHED_SLATE_STAIRS)
        registerBlock("white_line", RagiumBlocks.WHITE_LINE)
        registerBlock("t_white_line", RagiumBlocks.T_WHITE_LINE)
        registerBlock("cross_white_line", RagiumBlocks.CROSS_WHITE_LINE)
        registerBlock("steel_glass", RagiumBlocks.STEEL_GLASS)
        registerBlock("ragium_glass", RagiumBlocks.RAGIUM_GLASS)
        registerBlockItem(RagiumBlocks.ASPHALT)
        registerBlockItem(RagiumBlocks.ASPHALT_SLAB)
        registerBlockItem(RagiumBlocks.ASPHALT_STAIRS)
        registerBlockItem(RagiumBlocks.POLISHED_ASPHALT)
        registerBlockItem(RagiumBlocks.POLISHED_ASPHALT_SLAB)
        registerBlockItem(RagiumBlocks.POLISHED_ASPHALT_STAIRS)
        registerBlockItem(RagiumBlocks.GYPSUM)
        registerBlockItem(RagiumBlocks.GYPSUM_SLAB)
        registerBlockItem(RagiumBlocks.GYPSUM_STAIRS)
        registerBlockItem(RagiumBlocks.POLISHED_GYPSUM)
        registerBlockItem(RagiumBlocks.POLISHED_GYPSUM_SLAB)
        registerBlockItem(RagiumBlocks.POLISHED_GYPSUM_STAIRS)
        registerBlockItem(RagiumBlocks.SLATE)
        registerBlockItem(RagiumBlocks.SLATE_SLAB)
        registerBlockItem(RagiumBlocks.SLATE_STAIRS)
        registerBlockItem(RagiumBlocks.POLISHED_SLATE)
        registerBlockItem(RagiumBlocks.POLISHED_SLATE_SLAB)
        registerBlockItem(RagiumBlocks.POLISHED_SLATE_STAIRS)
        registerBlockItem(RagiumBlocks.WHITE_LINE)
        registerBlockItem(RagiumBlocks.T_WHITE_LINE)
        registerBlockItem(RagiumBlocks.CROSS_WHITE_LINE)
        registerBlockItem(RagiumBlocks.STEEL_GLASS)
        registerBlockItem(RagiumBlocks.RAGIUM_GLASS)

        registerBlockNew(RagiumBlocksNew.SPONGE_CAKE) {
            HayBlock(it.mapColor(MapColor.YELLOW).strength(0.5f).sounds(BlockSoundGroup.WOOL))
        }
        registerBlockNew(RagiumBlocksNew.SWEET_BERRIES_CAKE) {
            object : Block(it.solid().strength(0.5f).sounds(BlockSoundGroup.WOOL)) {
                override fun getOutlineShape(
                    state: BlockState,
                    world: BlockView,
                    pos: BlockPos,
                    context: ShapeContext,
                ): VoxelShape = createCuboidShape(1.0, 0.0, 1.0, 15.0, 8.0, 15.0)
            }
        }
        registerBlockItemNew(RagiumBlocksNew.SPONGE_CAKE) { it.descriptions(Text.translatable(RagiumTranslationKeys.SPONGE_CAKE)) }
        registerBlockItemNew(RagiumBlocksNew.SWEET_BERRIES_CAKE)

        registerBlock("auto_illuminator", RagiumBlocks.AUTO_ILLUMINATOR)
        registerBlock("extended_processor", RagiumBlocks.EXTENDED_PROCESSOR)
        registerBlock("manual_forge", RagiumBlocks.MANUAL_FORGE)
        registerBlock("manual_grinder", RagiumBlocks.MANUAL_GRINDER)
        registerBlock("manual_mixer", RagiumBlocks.MANUAL_MIXER)
        registerBlock("network_interface", RagiumBlocks.NETWORK_INTERFACE)
        registerBlock("open_crate", RagiumBlocks.OPEN_CRATE)
        registerBlock("teleport_anchor", RagiumBlocks.TELEPORT_ANCHOR)
        registerBlock("trash_box", RagiumBlocks.TRASH_BOX)
        registerBlockItem(
            RagiumBlocks.AUTO_ILLUMINATOR,
            itemSettings().descriptions(
                Text.translatable(
                    RagiumTranslationKeys.AUTO_ILLUMINATOR,
                    RagiumAPI.getInstance().config.autoIlluminatorRadius,
                ),
            ),
        )
        registerBlockItem(
            RagiumBlocks.EXTENDED_PROCESSOR,
            itemSettings().descriptions(Text.translatable(RagiumTranslationKeys.LARGE_PROCESSOR)),
        )
        registerBlockItem(RagiumBlocks.MANUAL_FORGE)
        registerBlockItem(
            RagiumBlocks.MANUAL_GRINDER,
            itemSettings().descriptions(Text.translatable(RagiumTranslationKeys.MANUAL_GRINDER)),
        )
        registerBlockItem(RagiumBlocks.MANUAL_MIXER)
        registerBlockItem(
            RagiumBlocks.NETWORK_INTERFACE,
            itemSettings().descriptions(Text.translatable(RagiumTranslationKeys.NETWORK_INTERFACE)),
        )
        registerBlockItem(
            RagiumBlocks.OPEN_CRATE,
            itemSettings().descriptions(Text.translatable(RagiumTranslationKeys.OPEN_CRATE)),
        )
        registerBlockItem(RagiumBlocks.TELEPORT_ANCHOR)
        registerBlockItem(
            RagiumBlocks.TRASH_BOX,
            itemSettings().descriptions(Text.translatable(RagiumTranslationKeys.TRASH_BOX)),
        )

        registerBlockNew(RagiumBlocksNew.BACKPACK_INTERFACE) {
            HTBackpackInterfaceBlock(it.mapColor(MapColor.BLACK).requiresTool().strength(2f, 6f))
        }
        registerBlockNew(RagiumBlocksNew.ENCHANTMENT_BOOKSHELF) {
            HTBlockWithEntity.build(
                RagiumBlockEntityTypes.ENCHANTMENT_BOOKSHELF,
                it.mapColor(MapColor.OAK_TAN).strength(1.5f).sounds(BlockSoundGroup.WOOD),
            )
        }
        registerBlockNew(RagiumBlocksNew.ITEM_DISPLAY) {
            HTItemDisplayBlock(it.strength(0.5f).sounds(BlockSoundGroup.GLASS))
        }
        registerBlockNew(RagiumBlocksNew.ROPE) {
            HTRopeBlock(it.mapColor(MapColor.BROWN).strength(0.8F).sounds(BlockSoundGroup.WOOL))
        }
        registerBlockNew(RagiumBlocksNew.SHAFT) {
            HTThinPillarBlock(it.requiresTool().strength(5f).sounds(BlockSoundGroup.METAL))
        }
        registerBlockNew(RagiumBlocksNew.INFESTING) {
            HTInfectingBlock(it.ticksRandomly().dropsNothing())
        }
        registerBlockItemNew(
            RagiumBlocksNew.BACKPACK_INTERFACE,
        ) { it.component(RagiumComponentTypes.REWORK_TARGET, Unit) }
        registerBlockItemNew(
            RagiumBlocksNew.ENCHANTMENT_BOOKSHELF,
        ) { it.component(RagiumComponentTypes.REWORK_TARGET, Unit) }
        registerBlockItemNew(RagiumBlocksNew.ITEM_DISPLAY)
        registerBlockItemNew(
            RagiumBlocksNew.ROPE,
        ) { block: Block, settings: Item.Settings ->
            HTRopeBlockItem(
                block,
                settings
                    .descriptions(Text.translatable(RagiumTranslationKeys.ROPE))
                    .component(RagiumComponentTypes.REWORK_TARGET, Unit),
            )
        }
        registerBlockItemNew(RagiumBlocksNew.SHAFT)
    }

    @JvmStatic
    private fun initItems() {
        registerItem("steel_helmet", RagiumItems.STEEL_HELMET)
        registerItem("steel_chestplate", RagiumItems.STEEL_CHESTPLATE)
        registerItem("steel_leggings", RagiumItems.STEEL_LEGGINGS)
        registerItem("steel_boots", RagiumItems.STEEL_BOOTS)
        registerItem("stella_goggle", RagiumItems.STELLA_GOGGLE)
        registerItem("stella_jacket", RagiumItems.STELLA_JACKET)
        registerItem("stella_leggings", RagiumItems.STELLA_LEGGINGS)
        registerItem("stella_boots", RagiumItems.STELLA_BOOTS)

        registerItem("forge_hammer", RagiumItems.FORGE_HAMMER)
        registerItem("steel_axe", RagiumItems.STEEL_AXE)
        registerItem("steel_hoe", RagiumItems.STEEL_HOE)
        registerItem("steel_pickaxe", RagiumItems.STEEL_PICKAXE)
        registerItem("steel_shovel", RagiumItems.STEEL_SHOVEL)
        registerItem("steel_sword", RagiumItems.STEEL_SWORD)
        registerItem("stella_saber", RagiumItems.STELLA_SABER)
        registerItem("ragium_saber", RagiumItems.RAGIUM_SABER)
        registerItem("gigant_hammer", RagiumItems.GIGANT_HAMMER)

        registerItem("dynamite", RagiumItems.DYNAMITE)
        registerItem("anvil_dynamite", RagiumItems.ANVIL_DYNAMITE)
        registerItem("bedrock_dynamite", RagiumItems.BEDROCK_DYNAMITE)
        registerItem("flattening_dynamite", RagiumItems.FLATTENING_DYNAMITE)

        registerItem("backpack", RagiumItems.BACKPACK)
        registerItem("empty_fluid_cube", RagiumItems.EMPTY_FLUID_CUBE)
        registerItem("filled_fluid_cube", RagiumItems.FILLED_FLUID_CUBE)
        registerItem("fluid_filter", RagiumItems.FLUID_FILTER)
        registerItem("guide_book", RagiumItems.GUIDE_BOOK)
        registerItem("item_filter", RagiumItems.ITEM_FILTER)
        registerItem("ragi_wrench", RagiumItems.RAGI_WRENCH)
        registerItem("trader_catalog", RagiumItems.TRADER_CATALOG)

        registerItem("sweet_berries_cake_piece", RagiumItems.SWEET_BERRIES_CAKE_PIECE)
        registerItem("melon_pie", RagiumItems.MELON_PIE)

        registerItem("butter", RagiumItems.BUTTER)
        registerItem("caramel", RagiumItems.CARAMEL)
        registerItem("dough", RagiumItems.DOUGH)
        registerItem("flour", RagiumItems.FLOUR)

        registerItem("chocolate", RagiumItems.CHOCOLATE)
        registerItem("chocolate_apple", RagiumItems.CHOCOLATE_APPLE)
        registerItem("chocolate_bread", RagiumItems.CHOCOLATE_BREAD)
        registerItem("chocolate_cookie", RagiumItems.CHOCOLATE_COOKIE)

        registerItem("minced_meat", RagiumItems.MINCED_MEAT)
        registerItem("meat_ingot", RagiumItems.MEAT_INGOT)
        registerItem("cooked_meat_ingot", RagiumItems.COOKED_MEAT_INGOT)

        registerItem("bee_wax", RagiumItems.BEE_WAX)
        registerItem("pulp", RagiumItems.PULP)
        registerItem("residual_coke", RagiumItems.RESIDUAL_COKE)

        registerItem("deepant", RagiumItems.DEEPANT)
        registerItem("luminescence_dust", RagiumItems.LUMINESCENCE_DUST)
        registerItem("ragi_alloy_compound", RagiumItems.RAGI_ALLOY_COMPOUND)
        registerItem("slag", RagiumItems.SLAG)
        registerItem("soap_ingot", RagiumItems.SOAP_INGOT)

        registerItem("polymer_resin", RagiumItems.POLYMER_RESIN)
        registerItem("plastic_plate", RagiumItems.PLASTIC_PLATE)
        registerItem("engineering_plastic_plate", RagiumItems.ENGINEERING_PLASTIC_PLATE)
        registerItem("stella_plate", RagiumItems.STELLA_PLATE)

        registerItem("crude_silicon", RagiumItems.CRUDE_SILICON)
        registerItem("silicon", RagiumItems.SILICON)
        registerItem("refined_silicon", RagiumItems.REFINED_SILICON)

        registerItem("crimson_crystal", RagiumItems.CRIMSON_CRYSTAL)
        registerItem("warped_crystal", RagiumItems.WARPED_CRYSTAL)
        registerItem("obsidian_tear", RagiumItems.OBSIDIAN_TEAR)

        registerItem("basalt_mesh", RagiumItems.BASALT_MESH)
        registerItem("blazing_carbon_electrode", RagiumItems.BLAZING_CARBON_ELECTRODE)
        registerItem("carbon_electrode", RagiumItems.CARBON_ELECTRODE)
        registerItem("charged_carbon_electrode", RagiumItems.CHARGED_CARBON_ELECTRODE)
        registerItem("engine", RagiumItems.ENGINE)
        registerItem("laser_emitter", RagiumItems.LASER_EMITTER)
        registerItem("led", RagiumItems.LED)
        registerItem("processor_socket", RagiumItems.PROCESSOR_SOCKET)
        registerItem("ragi_crystal_processor", RagiumItems.RAGI_CRYSTAL_PROCESSOR)
        registerItem("solar_panel", RagiumItems.SOLAR_PANEL)

        registerItem("uranium_fuel", RagiumItems.URANIUM_FUEL)
        registerItem("plutonium_fuel", RagiumItems.PLUTONIUM_FUEL)
        registerItem("yellow_cake", RagiumItems.YELLOW_CAKE)
        registerItem("yellow_cake_piece", RagiumItems.YELLOW_CAKE_PIECE)
        registerItem("nuclear_waste", RagiumItems.NUCLEAR_WASTE)

        registerItem("ragi_ticket", RagiumItems.RAGI_TICKET)
    }
}
