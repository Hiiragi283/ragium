package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.content.HTContent

object RagiumBlocksNew {
    //    Creatives    //

    @JvmField
    val CREATIVE_CRATE: HTBlockContent = HTContent.ofBlock("creative_crate")

    @JvmField
    val CREATIVE_DRUM: HTBlockContent = HTContent.ofBlock("creative_drum")

    @JvmField
    val CREATIVE_EXPORTER: HTBlockContent = HTContent.ofBlock("creative_exporter")

    @JvmField
    val CREATIVE_SOURCE: HTBlockContent = HTContent.ofBlock("creative_source")

    @JvmField
    val CREATIVES: List<HTBlockContent> = listOf(
        CREATIVE_CRATE,
        CREATIVE_DRUM,
        CREATIVE_EXPORTER,
        CREATIVE_SOURCE,
    )

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

    /*@JvmField
    val ASPHALT: Block = Block(blockSettings(Blocks.SMOOTH_STONE))

    @JvmField
    val ASPHALT_SLAB: Block = SlabBlock(blockSettings(ASPHALT))

    @JvmField
    val ASPHALT_STAIRS = StairsBlock(ASPHALT.defaultState, blockSettings(ASPHALT))

    @JvmField
    val POLISHED_ASPHALT: Block = Block(blockSettings(Blocks.SMOOTH_STONE))

    @JvmField
    val POLISHED_ASPHALT_SLAB: Block = SlabBlock(blockSettings(POLISHED_ASPHALT))

    @JvmField
    val POLISHED_ASPHALT_STAIRS = StairsBlock(POLISHED_ASPHALT.defaultState, blockSettings(POLISHED_ASPHALT))

    @JvmField
    val GYPSUM: Block = Block(blockSettings(Blocks.SMOOTH_STONE))

    @JvmField
    val GYPSUM_SLAB: Block = SlabBlock(blockSettings(GYPSUM))

    @JvmField
    val GYPSUM_STAIRS = StairsBlock(GYPSUM.defaultState, blockSettings(GYPSUM))

    @JvmField
    val POLISHED_GYPSUM: Block = Block(blockSettings(Blocks.SMOOTH_STONE))

    @JvmField
    val POLISHED_GYPSUM_SLAB: Block = SlabBlock(blockSettings(POLISHED_GYPSUM))

    @JvmField
    val POLISHED_GYPSUM_STAIRS = StairsBlock(POLISHED_GYPSUM.defaultState, blockSettings(POLISHED_GYPSUM))

    @JvmField
    val SLATE: Block = Block(blockSettings(Blocks.SMOOTH_STONE))

    @JvmField
    val SLATE_SLAB: Block = SlabBlock(blockSettings(SLATE))

    @JvmField
    val SLATE_STAIRS = StairsBlock(SLATE.defaultState, blockSettings(SLATE))

    @JvmField
    val POLISHED_SLATE: Block = Block(blockSettings(Blocks.SMOOTH_STONE))

    @JvmField
    val POLISHED_SLATE_SLAB: Block = SlabBlock(blockSettings(POLISHED_SLATE))

    @JvmField
    val POLISHED_SLATE_STAIRS = StairsBlock(POLISHED_SLATE.defaultState, blockSettings(POLISHED_SLATE))

    @JvmField
    val WHITE_LINE: Block = HTSurfaceLineBlock(blockSettings().breakInstantly())

    @JvmField
    val T_WHITE_LINE: Block = HTSurfaceLineBlock(blockSettings().breakInstantly())

    @JvmField
    val CROSS_WHITE_LINE: Block = HTSurfaceBlock(blockSettings().breakInstantly())

    @JvmField
    val STEEL_GLASS = TransparentBlock(blockSettings(Blocks.GLASS).strength(2f, 1200f))

    @JvmField
    val RAGIUM_GLASS = TransparentBlock(blockSettings(Blocks.GLASS).strength(2f, 3600000.0F))

    @JvmField
    val BUILDINGS: List<Block> = listOf(
        // asphalt
        ASPHALT,
        ASPHALT_SLAB,
        ASPHALT_STAIRS,
        POLISHED_ASPHALT,
        POLISHED_ASPHALT_SLAB,
        POLISHED_ASPHALT_STAIRS,
        // gypsum
        GYPSUM,
        GYPSUM_SLAB,
        GYPSUM_STAIRS,
        POLISHED_GYPSUM,
        POLISHED_GYPSUM_SLAB,
        POLISHED_GYPSUM_STAIRS,
        // slate
        SLATE,
        SLATE_SLAB,
        SLATE_STAIRS,
        POLISHED_SLATE,
        POLISHED_SLATE_SLAB,
        POLISHED_SLATE_STAIRS,
        // white line
        WHITE_LINE,
        T_WHITE_LINE,
        CROSS_WHITE_LINE,
        // glass
        STEEL_GLASS,
        RAGIUM_GLASS,
    )*/

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

    //    Mechanics    //

    /*@JvmField
    val AUTO_ILLUMINATOR: Block =
        HTBlockWithEntity.build(RagiumBlockEntityTypes.AUTO_ILLUMINATOR, blockSettings(Blocks.SMOOTH_STONE))

    @JvmField
    val EXTENDED_PROCESSOR: Block = HTExtendedProcessorBlock

    @JvmField
    val MANUAL_FORGE: Block =
        HTBlockWithEntity.build(RagiumBlockEntityTypes.MANUAL_FORGE, blockSettings(Blocks.BRICKS).nonOpaque())

    @JvmField
    val MANUAL_GRINDER: Block = HTManualGrinderBlock

    @JvmField
    val MANUAL_MIXER: Block = HTBlockWithEntity.build(RagiumBlockEntityTypes.MANUAL_MIXER, blockSettings(Blocks.BRICKS))

    @JvmField
    val NETWORK_INTERFACE: Block = HTNetworkInterfaceBlock

    @JvmField
    val OPEN_CRATE: Block = Block(blockSettings(Blocks.SMOOTH_STONE))

    @JvmField
    val TELEPORT_ANCHOR: Block = Block(blockSettings(Blocks.SMOOTH_STONE))

    @JvmField
    val TRASH_BOX: Block = Block(blockSettings(Blocks.SMOOTH_STONE))

    @JvmField
    val MECHANICS: List<Block> = listOf(
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
    )*/

    //    Misc    //

    @JvmField
    val BACKPACK_INTERFACE: HTBlockContent = HTContent.ofBlock("backpack_interface")

    @JvmField
    val ENCHANTMENT_BOOKSHELF: HTBlockContent = HTContent.ofBlock("enchantment_bookshelf")

    @JvmField
    val ITEM_DISPLAY: HTBlockContent = HTContent.ofBlock("item_display")

    @JvmField
    val ROPE: HTBlockContent = HTContent.ofBlock("rope")

    @JvmField
    val SHAFT: HTBlockContent = HTContent.ofBlock("shaft")

    @JvmField
    val INFESTING: HTBlockContent = HTContent.ofBlock("infesting")

    @JvmField
    val MISC: List<HTBlockContent> = listOf(
        BACKPACK_INTERFACE,
        ENCHANTMENT_BOOKSHELF,
        ITEM_DISPLAY,
        ROPE,
        SHAFT,
    )
}
