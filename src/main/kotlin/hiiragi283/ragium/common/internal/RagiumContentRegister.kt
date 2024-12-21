package hiiragi283.ragium.common.internal

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTBlockWithEntity
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.content.HTItemContent
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.fluid.HTVirtualFluid
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.block.*
import hiiragi283.ragium.common.block.machine.HTExtendedProcessorBlock
import hiiragi283.ragium.common.block.machine.HTManualGrinderBlock
import hiiragi283.ragium.common.block.machine.HTNetworkInterfaceBlock
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
        registerBlockNew(RagiumBlocks.CREATIVE_CRATE) {
            HTBlockWithEntity.buildHorizontal(
                RagiumBlockEntityTypes.CREATIVE_CRATE,
                it.mapColor(MapColor.PURPLE).requiresTool().strength(2f, 6f),
            )
        }
        registerBlockNew(RagiumBlocks.CREATIVE_DRUM) {
            HTBlockWithEntity.build(
                RagiumBlockEntityTypes.CREATIVE_DRUM,
                it.mapColor(MapColor.PURPLE).requiresTool().strength(2f, 6f),
            )
        }
        registerBlockNew(RagiumBlocks.CREATIVE_EXPORTER) {
            HTCreativeExporterBlock(
                it
                    .mapColor(MapColor.PURPLE)
                    .requiresTool()
                    .strength(2f, 6f)
                    .solid()
                    .nonOpaque(),
            )
        }
        registerBlockNew(RagiumBlocks.CREATIVE_SOURCE) {
            HTBlockWithEntity.build(
                RagiumBlockEntityTypes.CREATIVE_SOURCE,
                it.mapColor(MapColor.PURPLE).requiresTool().strength(2f, 6f),
            )
        }
        registerBlockItemNew(RagiumBlocks.CREATIVE_CRATE) { it.rarity(Rarity.EPIC) }
        registerBlockItemNew(RagiumBlocks.CREATIVE_DRUM) { it.rarity(Rarity.EPIC) }
        registerBlockItemNew(RagiumBlocks.CREATIVE_EXPORTER) { it.rarity(Rarity.EPIC) }
        registerBlockItemNew(RagiumBlocks.CREATIVE_SOURCE) { it.rarity(Rarity.EPIC) }

        registerBlockNew(RagiumBlocks.MUTATED_SOIL) {
            Block(it.mapColor(MapColor.GREEN).strength(0.5f).sounds(BlockSoundGroup.GRAVEL))
        }
        registerBlockNew(RagiumBlocks.POROUS_NETHERRACK) {
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
        registerBlockItemNew(RagiumBlocks.MUTATED_SOIL) {
            it.descriptions(Text.translatable(RagiumTranslationKeys.MUTATED_SOIL))
        }
        registerBlockItemNew(RagiumBlocks.POROUS_NETHERRACK) {
            it.descriptions(Text.translatable(RagiumTranslationKeys.POROUS_NETHERRACK))
        }

        RagiumBlocks.Stones.entries.forEach { stone: RagiumBlocks.Stones ->
            registerBlockNew(stone, blockSettings(Blocks.SMOOTH_STONE), ::Block)
            registerBlockItemNew(stone)
        }
        RagiumBlocks.Slabs.entries.forEach { slab: RagiumBlocks.Slabs ->
            registerBlockNew(slab, blockSettings(Blocks.SMOOTH_STONE), ::SlabBlock)
            registerBlockItemNew(slab)
        }
        RagiumBlocks.Stairs.entries.forEach { stair: RagiumBlocks.Stairs ->
            registerBlockNew(stair, blockSettings(Blocks.SMOOTH_STONE)) {
                StairsBlock(stair.baseStone.get().defaultState, it)
            }
            registerBlockItemNew(stair)
        }

        registerBlockNew(RagiumBlocks.WHITE_LINE, block = ::HTSurfaceLineBlock)
        registerBlockNew(RagiumBlocks.T_WHITE_LINE, block = ::HTSurfaceLineBlock)
        registerBlockNew(RagiumBlocks.CROSS_WHITE_LINE, block = ::HTSurfaceLineBlock)
        registerBlockNew(
            RagiumBlocks.STEEL_GLASS,
            blockSettings(Blocks.GLASS).strength(2f, 1200f),
            ::TransparentBlock,
        )
        registerBlockNew(
            RagiumBlocks.RAGIUM_GLASS,
            blockSettings(Blocks.GLASS).strength(2f, 3600000.0F),
            ::TransparentBlock,
        )
        registerBlockItemNew(RagiumBlocks.WHITE_LINE)
        registerBlockItemNew(RagiumBlocks.T_WHITE_LINE)
        registerBlockItemNew(RagiumBlocks.CROSS_WHITE_LINE)
        registerBlockItemNew(RagiumBlocks.STEEL_GLASS)
        registerBlockItemNew(RagiumBlocks.RAGIUM_GLASS)

        registerBlockNew(RagiumBlocks.SPONGE_CAKE) {
            HayBlock(it.mapColor(MapColor.YELLOW).strength(0.5f).sounds(BlockSoundGroup.WOOL))
        }
        registerBlockNew(RagiumBlocks.SWEET_BERRIES_CAKE) {
            object : Block(it.solid().strength(0.5f).sounds(BlockSoundGroup.WOOL)) {
                override fun getOutlineShape(
                    state: BlockState,
                    world: BlockView,
                    pos: BlockPos,
                    context: ShapeContext,
                ): VoxelShape = createCuboidShape(1.0, 0.0, 1.0, 15.0, 8.0, 15.0)
            }
        }
        registerBlockItemNew(RagiumBlocks.SPONGE_CAKE) { it.descriptions(Text.translatable(RagiumTranslationKeys.SPONGE_CAKE)) }
        registerBlockItemNew(RagiumBlocks.SWEET_BERRIES_CAKE)

        registerBlockNew(RagiumBlocks.AUTO_ILLUMINATOR, blockSettings(Blocks.SMOOTH_STONE)) {
            HTBlockWithEntity.build(RagiumBlockEntityTypes.AUTO_ILLUMINATOR, it)
        }
        registerBlockNew(
            RagiumBlocks.EXTENDED_PROCESSOR,
            blockSettings(Blocks.SMOOTH_STONE),
            ::HTExtendedProcessorBlock,
        )
        registerBlockNew(RagiumBlocks.MANUAL_FORGE, blockSettings(Blocks.BRICKS)) {
            HTBlockWithEntity.build(RagiumBlockEntityTypes.MANUAL_FORGE, it.nonOpaque())
        }
        registerBlockNew(RagiumBlocks.MANUAL_GRINDER, blockSettings(Blocks.BRICKS), ::HTManualGrinderBlock)
        registerBlockNew(RagiumBlocks.MANUAL_MIXER, blockSettings(Blocks.BRICKS)) {
            HTBlockWithEntity.build(RagiumBlockEntityTypes.MANUAL_MIXER, it)
        }
        registerBlockNew(
            RagiumBlocks.NETWORK_INTERFACE,
            blockSettings(Blocks.SMOOTH_STONE),
            ::HTNetworkInterfaceBlock,
        )
        registerBlockNew(RagiumBlocks.OPEN_CRATE, blockSettings(Blocks.SMOOTH_STONE), ::Block)
        registerBlockNew(RagiumBlocks.TELEPORT_ANCHOR, blockSettings(Blocks.SMOOTH_STONE), ::Block)
        registerBlockNew(RagiumBlocks.TRASH_BOX, blockSettings(Blocks.SMOOTH_STONE), ::Block)
        registerBlockItemNew(RagiumBlocks.AUTO_ILLUMINATOR) {
            it.descriptions(
                Text.translatable(
                    RagiumTranslationKeys.AUTO_ILLUMINATOR,
                    RagiumAPI.getInstance().config.autoIlluminatorRadius,
                ),
            )
        }
        registerBlockItemNew(RagiumBlocks.EXTENDED_PROCESSOR) {
            it.descriptions(
                Text.translatable(
                    RagiumTranslationKeys.LARGE_PROCESSOR,
                ),
            )
        }
        registerBlockItemNew(RagiumBlocks.MANUAL_FORGE)
        registerBlockItemNew(RagiumBlocks.MANUAL_GRINDER) { it.descriptions(Text.translatable(RagiumTranslationKeys.MANUAL_GRINDER)) }
        registerBlockItemNew(RagiumBlocks.MANUAL_MIXER)
        registerBlockItemNew(RagiumBlocks.NETWORK_INTERFACE) {
            it.descriptions(Text.translatable(RagiumTranslationKeys.NETWORK_INTERFACE))
        }
        registerBlockItemNew(RagiumBlocks.OPEN_CRATE) { it.descriptions(Text.translatable(RagiumTranslationKeys.OPEN_CRATE)) }
        registerBlockItemNew(RagiumBlocks.TELEPORT_ANCHOR)
        registerBlockItemNew(RagiumBlocks.TRASH_BOX) { it.descriptions(Text.translatable(RagiumTranslationKeys.TRASH_BOX)) }

        registerBlockNew(RagiumBlocks.BACKPACK_INTERFACE) {
            HTBackpackInterfaceBlock(it.mapColor(MapColor.BLACK).requiresTool().strength(2f, 6f))
        }
        registerBlockNew(RagiumBlocks.ENCHANTMENT_BOOKSHELF) {
            HTBlockWithEntity.build(
                RagiumBlockEntityTypes.ENCHANTMENT_BOOKSHELF,
                it.mapColor(MapColor.OAK_TAN).strength(1.5f).sounds(BlockSoundGroup.WOOD),
            )
        }
        registerBlockNew(RagiumBlocks.ITEM_DISPLAY) {
            HTItemDisplayBlock(it.strength(0.5f).sounds(BlockSoundGroup.GLASS))
        }
        registerBlockNew(RagiumBlocks.ROPE) {
            HTRopeBlock(it.mapColor(MapColor.BROWN).strength(0.8F).sounds(BlockSoundGroup.WOOL))
        }
        registerBlockNew(RagiumBlocks.SHAFT) {
            HTThinPillarBlock(it.requiresTool().strength(5f).sounds(BlockSoundGroup.METAL))
        }
        registerBlockNew(RagiumBlocks.INFESTING) {
            HTInfectingBlock(it.ticksRandomly().dropsNothing())
        }
        registerBlockItemNew(
            RagiumBlocks.BACKPACK_INTERFACE,
        ) { it.component(RagiumComponentTypes.REWORK_TARGET, Unit) }
        registerBlockItemNew(
            RagiumBlocks.ENCHANTMENT_BOOKSHELF,
        ) { it.component(RagiumComponentTypes.REWORK_TARGET, Unit) }
        registerBlockItemNew(RagiumBlocks.ITEM_DISPLAY)
        registerBlockItemNew(
            RagiumBlocks.ROPE,
        ) { block: Block, settings: Item.Settings ->
            HTRopeBlockItem(
                block,
                settings
                    .descriptions(Text.translatable(RagiumTranslationKeys.ROPE))
                    .component(RagiumComponentTypes.REWORK_TARGET, Unit),
            )
        }
        registerBlockItemNew(RagiumBlocks.SHAFT)
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
