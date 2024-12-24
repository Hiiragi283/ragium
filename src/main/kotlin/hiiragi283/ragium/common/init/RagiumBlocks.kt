package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTBlockWithEntity
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineTierProvider
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.util.HTPipeType
import hiiragi283.ragium.common.block.*
import hiiragi283.ragium.common.block.machine.HTExtendedProcessorBlock
import hiiragi283.ragium.common.block.machine.HTManualGrinderBlock
import hiiragi283.ragium.common.block.machine.HTNetworkInterfaceBlock
import hiiragi283.ragium.common.block.storage.HTBackpackInterfaceBlock
import hiiragi283.ragium.common.block.storage.HTCrateBlock
import hiiragi283.ragium.common.block.storage.HTDrumBlock
import hiiragi283.ragium.common.block.transfer.*
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.tag.FluidTags
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.text.Text
import net.minecraft.util.Rarity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object RagiumBlocks {
    //    Creatives    //

    enum class Creatives : HTBlockContent {
        CRATE,
        DRUM,
        EXPORTER,
        SOURCE,
        ;

        override val key: RegistryKey<Block> = HTContent.blockKey("creative_${name.lowercase()}")
    }

    //    Minerals    //
    @JvmField
    val MUTATED_SOIL: HTBlockContent = HTContent.ofBlock("mutated_soil")

    @JvmField
    val POROUS_NETHERRACK: HTBlockContent = HTContent.ofBlock("porous_netherrack")

    @JvmField
    val NATURAL: List<HTBlockContent> = listOf(
        MUTATED_SOIL,
        POROUS_NETHERRACK,
    )

    //    Buildings    //

    enum class Stones : HTBlockContent {
        ASPHALT,
        POLISHED_ASPHALT,
        GYPSUM,
        POLISHED_GYPSUM,
        SLATE,
        POLISHED_SLATE,
        ;

        override val key: RegistryKey<Block> = HTContent.blockKey(name.lowercase())
    }

    enum class Slabs : HTBlockContent {
        ASPHALT,
        POLISHED_ASPHALT,
        GYPSUM,
        POLISHED_GYPSUM,
        SLATE,
        POLISHED_SLATE,
        ;

        val baseStone: Stones
            get() = Stones.entries.first { it.name == this.name }

        override val key: RegistryKey<Block> = HTContent.blockKey("${name.lowercase()}_slab")
    }

    enum class Stairs : HTBlockContent {
        ASPHALT,
        POLISHED_ASPHALT,
        GYPSUM,
        POLISHED_GYPSUM,
        SLATE,
        POLISHED_SLATE,
        ;

        val baseStone: Stones
            get() = Stones.entries.first { it.name == this.name }

        override val key: RegistryKey<Block> = HTContent.blockKey("${name.lowercase()}_stairs")
    }

    enum class Glasses : HTBlockContent {
        STEEL,
        OBSIDIAN,
        RAGIUM,
        ;

        override val key: RegistryKey<Block> = HTContent.blockKey("${name.lowercase()}_glass")
    }

    enum class WhiteLines(name: String) : HTBlockContent {
        SIMPLE(""),
        T_SHAPED("t_"),
        CROSS("cross_"),
        ;

        override val key: RegistryKey<Block> = HTContent.blockKey("${name}white_line")
    }

    enum class Ores(path: String, override val material: HTMaterialKey, val baseStone: Block) : HTBlockContent.Material {
        CRUDE_RAGINITE("raginite_ore", RagiumMaterialKeys.CRUDE_RAGINITE, Blocks.STONE),
        DEEP_RAGINITE("deepslate_raginite_ore", RagiumMaterialKeys.RAGINITE, Blocks.DEEPSLATE),
        NETHER_RAGINITE("nether_raginite_ore", RagiumMaterialKeys.RAGINITE, Blocks.NETHERRACK),
        END_RAGI_CRYSTAL("end_ragi_crystal_ore", RagiumMaterialKeys.RAGI_CRYSTAL, Blocks.END_STONE),
        ;

        override val key: RegistryKey<Block> = HTContent.blockKey(path)
        override val tagPrefix: HTTagPrefix = HTTagPrefix.ORE
    }

    enum class StorageBlocks(override val material: HTMaterialKey) : HTBlockContent.Material {
        RAGI_ALLOY(RagiumMaterialKeys.RAGI_ALLOY),
        RAGI_STEEL(RagiumMaterialKeys.RAGI_STEEL),
        ALUMINUM(RagiumMaterialKeys.ALUMINUM),
        FLUORITE(RagiumMaterialKeys.FLUORITE),
        STEEL(RagiumMaterialKeys.STEEL),
        RAGI_CRYSTAL(RagiumMaterialKeys.RAGI_CRYSTAL),
        REFINED_RAGI_STEEL(RagiumMaterialKeys.REFINED_RAGI_STEEL),
        CRYOLITE(RagiumMaterialKeys.CRYOLITE),
        DEEP_STEEL(RagiumMaterialKeys.DEEP_STEEL),
        RAGIUM(RagiumMaterialKeys.RAGIUM),
        ;

        override val key: RegistryKey<Block> = HTContent.blockKey("${name.lowercase()}_block")
        override val tagPrefix: HTTagPrefix = HTTagPrefix.STORAGE_BLOCK
    }

    enum class Grates(override val tier: HTMachineTier) :
        HTBlockContent,
        HTMachineTierProvider {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val key: RegistryKey<Block> = HTContent.blockKey("${name.lowercase()}_grate")
    }

    enum class Casings(override val tier: HTMachineTier) :
        HTBlockContent,
        HTMachineTierProvider {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val key: RegistryKey<Block> = HTContent.blockKey("${name.lowercase()}_casing")
    }

    enum class Hulls(override val tier: HTMachineTier) :
        HTBlockContent,
        HTMachineTierProvider {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val key: RegistryKey<Block> = HTContent.blockKey("${name.lowercase()}_hull")
    }

    enum class Coils(override val tier: HTMachineTier) :
        HTBlockContent,
        HTMachineTierProvider {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val key: RegistryKey<Block> = HTContent.blockKey("${name.lowercase()}_coil")
    }

    //    Foods    //

    @JvmField
    val SPONGE_CAKE: HTBlockContent = HTContent.ofBlock("sponge_cake")

    @JvmField
    val SWEET_BERRIES_CAKE: HTBlockContent = HTContent.ofBlock("sweet_berries_cake")

    @JvmField
    val FOODS: List<HTBlockContent> = listOf(
        SPONGE_CAKE,
        SWEET_BERRIES_CAKE,
    )

    //    Transport    //

    enum class Crates(override val tier: HTMachineTier) :
        HTBlockContent,
        HTMachineTierProvider {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val key: RegistryKey<Block> = HTContent.blockKey("${name.lowercase()}_crate")
    }

    enum class Drums(override val tier: HTMachineTier) :
        HTBlockContent,
        HTMachineTierProvider {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val key: RegistryKey<Block> = HTContent.blockKey("${name.lowercase()}_drum")
    }

    enum class Exporters(override val tier: HTMachineTier) :
        HTBlockContent,
        HTMachineTierProvider {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        override val key: RegistryKey<Block> = HTContent.blockKey("${name.lowercase()}_exporter")
    }

    enum class Pipes(override val tier: HTMachineTier, val pipeType: HTPipeType) :
        HTBlockContent,
        HTMachineTierProvider {
        STONE(HTMachineTier.PRIMITIVE, HTPipeType.ITEM),
        WOODEN(HTMachineTier.PRIMITIVE, HTPipeType.FLUID),
        IRON(HTMachineTier.BASIC, HTPipeType.ITEM),
        COPPER(HTMachineTier.BASIC, HTPipeType.FLUID),
        UNIVERSAL(HTMachineTier.ADVANCED, HTPipeType.ALL),
        ;

        override val key: RegistryKey<Block> = HTContent.blockKey("${name.lowercase()}_pipe")
    }

    enum class CrossPipes(val pipeType: HTPipeType) : HTBlockContent {
        STEEL(HTPipeType.ITEM),
        GOLD(HTPipeType.FLUID),
        ;

        override val key: RegistryKey<Block> = HTContent.blockKey("${name.lowercase()}_pipe")
    }

    enum class PipeStations(val pipeType: HTPipeType) : HTBlockContent {
        ITEM(HTPipeType.ITEM),
        FLUID(HTPipeType.FLUID),
        ;

        override val key: RegistryKey<Block> = HTContent.blockKey("${name.lowercase()}_pipe_station")
    }

    enum class FilteringPipes(val pipeType: HTPipeType) : HTBlockContent {
        ITEM(HTPipeType.ITEM),
        FLUID(HTPipeType.FLUID),
        ;

        override val key: RegistryKey<Block> = HTContent.blockKey("${name.lowercase()}_filtering_pipe")
    }

    //    Mechanics    //
    @JvmField
    val AUTO_ILLUMINATOR: HTBlockContent = HTContent.ofBlock("auto_illuminator")

    @JvmField
    val EXTENDED_PROCESSOR: HTBlockContent = HTContent.ofBlock("extended_processor")

    @JvmField
    val MANUAL_FORGE: HTBlockContent = HTContent.ofBlock("manual_forge")

    @JvmField
    val MANUAL_GRINDER: HTBlockContent = HTContent.ofBlock("manual_grinder")

    @JvmField
    val MANUAL_MIXER: HTBlockContent = HTContent.ofBlock("manual_mixer")

    @JvmField
    val NETWORK_INTERFACE: HTBlockContent = HTContent.ofBlock("network_interface")

    @JvmField
    val OPEN_CRATE: HTBlockContent = HTContent.ofBlock("open_crate")

    @JvmField
    val TELEPORT_ANCHOR: HTBlockContent = HTContent.ofBlock("teleport_anchor")

    @JvmField
    val TRASH_BOX: HTBlockContent = HTContent.ofBlock("trash_box")

    @JvmField
    val MECHANICS: List<HTBlockContent> = listOf(
        // colored
        EXTENDED_PROCESSOR, // red
        AUTO_ILLUMINATOR, // yellow
        OPEN_CRATE, // green
        TELEPORT_ANCHOR, // blue
        TRASH_BOX, // gray
        NETWORK_INTERFACE, // white
        // manual machines
        MANUAL_FORGE,
        MANUAL_GRINDER,
        MANUAL_MIXER,
    )

    //    Misc    //

    @JvmField
    val BACKPACK_INTERFACE: HTBlockContent = HTContent.ofBlock("backpack_interface")

    @JvmField
    val ITEM_DISPLAY: HTBlockContent = HTContent.ofBlock("item_display")

    @JvmField
    val SHAFT: HTBlockContent = HTContent.ofBlock("shaft")

    @JvmField
    val INFESTING: HTBlockContent = HTContent.ofBlock("infesting")

    @JvmField
    val MISC: List<HTBlockContent> = listOf(
        BACKPACK_INTERFACE,
        ITEM_DISPLAY,
        SHAFT,
    )

    //    Register    //

    private fun registerBlock(
        content: HTBlockContent,
        parent: AbstractBlock.Settings = blockSettings(),
        block: (AbstractBlock.Settings) -> Block,
    ): Block = Registry.register(Registries.BLOCK, content.key, block(parent))

    private fun registerSimpleBlock(content: HTBlockContent, parent: AbstractBlock.Settings = blockSettings()): Block =
        registerBlock(content, parent, ::Block)

    private fun registerBlockWithBE(
        content: HTBlockContent,
        type: BlockEntityType<*>,
        parent: AbstractBlock.Settings = blockSettings(),
    ): Block = registerBlock(content, parent) { HTBlockWithEntity.build(type, it) }

    private fun registerHorizontalBlockWithBE(
        content: HTBlockContent,
        type: BlockEntityType<*>,
        parent: AbstractBlock.Settings = blockSettings(),
    ): Block = registerBlock(content, parent) { HTBlockWithEntity.buildHorizontal(type, it) }

    private fun registerBlockItem(block: HTBlockContent, parent: Item.Settings = itemSettings()) {
        Registry.register(Registries.ITEM, block.id, BlockItem(block.get(), parent))
    }

    private fun registerBlockItem(block: HTBlockContent, factory: (Block, Item.Settings) -> Item) {
        Registry.register(Registries.ITEM, block.id, factory(block.get(), itemSettings()))
    }

    @JvmStatic
    internal fun register() {
        // creative
        registerHorizontalBlockWithBE(
            Creatives.CRATE,
            RagiumBlockEntityTypes.CREATIVE_CRATE,
            blockSettings().mapColor(MapColor.PURPLE).requiresTool().strength(2f, 6f),
        )
        registerBlockWithBE(
            Creatives.DRUM,
            RagiumBlockEntityTypes.CREATIVE_DRUM,
            blockSettings().mapColor(MapColor.PURPLE).requiresTool().strength(2f, 6f),
        )
        registerBlock(
            Creatives.EXPORTER,
            blockSettings()
                .mapColor(MapColor.PURPLE)
                .requiresTool()
                .strength(2f, 6f)
                .solid()
                .nonOpaque(),
            ::HTCreativeExporterBlock,
        )
        registerBlockWithBE(
            Creatives.SOURCE,
            RagiumBlockEntityTypes.CREATIVE_SOURCE,
            blockSettings().mapColor(MapColor.PURPLE).requiresTool().strength(2f, 6f),
        )
        Creatives.entries.forEach { registerBlockItem(it, itemSettings().rarity(Rarity.EPIC)) }
        // natural
        registerSimpleBlock(
            MUTATED_SOIL,
            blockSettings().mapColor(MapColor.GREEN).strength(0.5f).sounds(BlockSoundGroup.GRAVEL),
        )
        registerBlock(POROUS_NETHERRACK) {
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
        registerBlockItem(MUTATED_SOIL, itemSettings().descriptions(RagiumTranslationKeys.MUTATED_SOIL))
        registerBlockItem(POROUS_NETHERRACK, itemSettings().descriptions(RagiumTranslationKeys.POROUS_NETHERRACK))
        // stone
        Stones.entries.forEach { stone: Stones ->
            registerSimpleBlock(stone, blockSettings(Blocks.SMOOTH_STONE))
            registerBlockItem(stone)
        }
        // slab
        Slabs.entries.forEach { slab: Slabs ->
            registerBlock(slab, blockSettings(Blocks.SMOOTH_STONE), ::SlabBlock)
            registerBlockItem(slab)
        }
        // stair
        Stairs.entries.forEach { stair: Stairs ->
            registerBlock(stair, blockSettings(Blocks.SMOOTH_STONE)) {
                StairsBlock(stair.baseStone.get().defaultState, it)
            }
            registerBlockItem(stair)
        }
        // white line
        WhiteLines.entries.forEach {
            registerBlock(it, block = ::HTSurfaceLineBlock)
            registerBlockItem(it)
        }
        // glass
        registerBlock(
            Glasses.STEEL,
            blockSettings(Blocks.GLASS).strength(2f, 100f),
            ::TransparentBlock,
        )
        registerBlock(
            Glasses.OBSIDIAN,
            blockSettings(Blocks.GLASS).strength(2f, 1200f),
            ::TransparentBlock,
        )
        registerBlock(
            Glasses.RAGIUM,
            blockSettings(Blocks.GLASS).strength(2f, 3600000.0F),
            ::TransparentBlock,
        )
        registerBlockItem(Glasses.STEEL, itemSettings().descriptions(RagiumTranslationKeys.STEEL_GLASS))
        registerBlockItem(Glasses.OBSIDIAN, itemSettings().descriptions(RagiumTranslationKeys.OBSIDIAN_GLASS))
        registerBlockItem(Glasses.RAGIUM, itemSettings().descriptions(RagiumTranslationKeys.RAGIUM_GLASS))
        // ore
        Ores.entries.forEach { ore: Ores ->
            registerSimpleBlock(ore, blockSettings(ore.baseStone))
            registerBlockItem(ore, itemSettings().material(ore.material, ore.tagPrefix))
        }
        // storage block
        StorageBlocks.entries.forEach { storage: StorageBlocks ->
            registerSimpleBlock(storage, blockSettings(Blocks.IRON_BLOCK))
            registerBlockItem(storage, itemSettings().material(storage.material, storage.tagPrefix))
        }
        // grate
        Grates.entries.forEach { grate: Grates ->
            registerBlock(grate, blockSettings(Blocks.COPPER_GRATE), ::TransparentBlock)
            registerBlockItem(grate, itemSettings().tieredText(RagiumTranslationKeys.GRATE, grate.tier))
        }
        // casing
        Casings.entries.forEach { casings: Casings ->
            registerSimpleBlock(casings, blockSettings(Blocks.SMOOTH_STONE))
            registerBlockItem(casings, itemSettings().tieredText(RagiumTranslationKeys.CASING, casings.tier))
        }
        // hull
        Hulls.entries.forEach { hull: Hulls ->
            registerBlock(hull, blockSettings(Blocks.SMOOTH_STONE), ::TransparentBlock)
            registerBlockItem(hull, itemSettings().tieredText(RagiumTranslationKeys.HULL, hull.tier))
        }
        // coil
        Coils.entries.forEach { coil: Coils ->
            registerBlock(coil, blockSettings(Blocks.COPPER_BLOCK), ::PillarBlock)
            registerBlockItem(coil, itemSettings().tieredText(RagiumTranslationKeys.COIL, coil.tier))
        }
        // food
        registerBlock(
            SPONGE_CAKE,
            blockSettings().mapColor(MapColor.PURPLE).requiresTool().strength(2f, 6f),
            ::HayBlock,
        )
        registerBlock(
            SWEET_BERRIES_CAKE,
            blockSettings().solid().strength(0.5f).sounds(BlockSoundGroup.WOOL),
            ::HTCakeBlock,
        )
        registerBlockItem(SPONGE_CAKE, itemSettings().descriptions(RagiumTranslationKeys.SPONGE_CAKE))
        registerBlockItem(SWEET_BERRIES_CAKE)
        // crate
        Crates.entries.forEach { crate: Crates ->
            registerBlock(crate, blockSettings(Blocks.SMOOTH_STONE)) { HTCrateBlock(crate.tier, it) }
            registerBlockItem(crate, itemSettings().tieredText(RagiumTranslationKeys.CRATE, crate.tier))
        }
        // drum
        Drums.entries.forEach { drum: Drums ->
            registerBlock(drum, blockSettings(Blocks.SMOOTH_STONE)) { HTDrumBlock(drum.tier, it) }
            registerBlockItem(drum, itemSettings().tieredText(RagiumTranslationKeys.DRUM, drum.tier))
        }
        // exporter
        Exporters.entries.forEach { exporter: Exporters ->
            registerBlock(exporter) { HTExporterBlock(exporter.tier, it) }
            registerBlockItem(exporter, itemSettings().tieredText(RagiumTranslationKeys.EXPORTER, exporter.tier))
        }
        // pipe
        Pipes.entries.forEach { pipe: Pipes ->
            registerBlock(pipe) { HTSimplePipeBlock(pipe.tier, pipe.pipeType, it) }
            registerBlockItem(pipe, itemSettings().tier(pipe.tier))
        }
        // cross pipe
        CrossPipes.entries.forEach { crossPipe: CrossPipes ->
            registerBlock(crossPipe, block = ::HTPipeBlock)
            registerBlockItem(crossPipe)
        }
        // pipe station
        PipeStations.entries.forEach { station: PipeStations ->
            registerBlock(station, block = ::HTPipeStationBlock)
            registerBlockItem(
                station,
                itemSettings().descriptions(RagiumTranslationKeys.PIPE_STATION),
            )
        }
        // filtering pipe
        FilteringPipes.entries.forEach { filtering: FilteringPipes ->
            registerBlock(filtering) { HTFilteringPipeBlock(filtering.pipeType, it) }
            registerBlockItem(
                filtering,
                itemSettings()
                    .descriptions(RagiumTranslationKeys.PIPE_STATION)
                    .maybeRework(),
            )
        }
        // mechanics
        registerBlockWithBE(
            AUTO_ILLUMINATOR,
            RagiumBlockEntityTypes.AUTO_ILLUMINATOR,
            blockSettings(Blocks.SMOOTH_STONE),
        )
        registerBlock(
            EXTENDED_PROCESSOR,
            blockSettings(Blocks.SMOOTH_STONE),
            ::HTExtendedProcessorBlock,
        )
        registerBlockWithBE(
            MANUAL_FORGE,
            RagiumBlockEntityTypes.MANUAL_FORGE,
            blockSettings(Blocks.BRICKS).nonOpaque(),
        )
        registerBlock(MANUAL_GRINDER, blockSettings(Blocks.BRICKS), ::HTManualGrinderBlock)
        registerBlockWithBE(
            MANUAL_MIXER,
            RagiumBlockEntityTypes.MANUAL_MIXER,
            blockSettings(Blocks.BRICKS),
        )
        registerBlock(
            NETWORK_INTERFACE,
            blockSettings(Blocks.SMOOTH_STONE),
            ::HTNetworkInterfaceBlock,
        )
        registerBlock(OPEN_CRATE, blockSettings(Blocks.SMOOTH_STONE), ::Block)
        registerBlock(TELEPORT_ANCHOR, blockSettings(Blocks.SMOOTH_STONE), ::Block)
        registerBlock(TRASH_BOX, blockSettings(Blocks.SMOOTH_STONE), ::Block)
        registerBlockItem(
            AUTO_ILLUMINATOR,
            itemSettings().descriptions(
                Text.translatable(
                    RagiumTranslationKeys.AUTO_ILLUMINATOR,
                    RagiumAPI.getInstance().config.autoIlluminatorRadius,
                ),
            ),
        )
        registerBlockItem(EXTENDED_PROCESSOR, itemSettings().descriptions(RagiumTranslationKeys.LARGE_PROCESSOR))
        registerBlockItem(MANUAL_FORGE)
        registerBlockItem(MANUAL_GRINDER, itemSettings().descriptions(RagiumTranslationKeys.MANUAL_GRINDER))
        registerBlockItem(MANUAL_MIXER)
        registerBlockItem(NETWORK_INTERFACE, itemSettings().descriptions(RagiumTranslationKeys.NETWORK_INTERFACE))
        registerBlockItem(OPEN_CRATE, itemSettings().descriptions(RagiumTranslationKeys.OPEN_CRATE))
        registerBlockItem(TELEPORT_ANCHOR)
        registerBlockItem(TRASH_BOX, itemSettings().descriptions(RagiumTranslationKeys.TRASH_BOX))
        // misc
        registerBlock(
            BACKPACK_INTERFACE,
            blockSettings().mapColor(MapColor.BLACK).requiresTool().strength(2f, 6f),
            ::HTBackpackInterfaceBlock,
        )
        registerBlock(
            ITEM_DISPLAY,
            blockSettings().strength(0.5f).sounds(BlockSoundGroup.GLASS),
            ::HTItemDisplayBlock,
        )
        registerBlock(
            SHAFT,
            blockSettings().requiresTool().strength(5f).sounds(BlockSoundGroup.METAL),
            ::HTThinPillarBlock,
        )
        registerBlock(
            INFESTING,
            blockSettings().ticksRandomly().dropsNothing(),
            ::HTInfectingBlock,
        )
        registerBlockItem(
            BACKPACK_INTERFACE,
            itemSettings().maybeRework(),
        )
        registerBlockItem(ITEM_DISPLAY)
        registerBlockItem(SHAFT)
    }
}
