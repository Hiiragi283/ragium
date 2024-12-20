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
    /*@JvmField
    val MUTATED_SOIL: Block = Block(blockSettings(Blocks.DIRT))

    @JvmField
    val POROUS_NETHERRACK: Block = HTSpongeBlock(
        blockSettings(Blocks.NETHERRACK),
        Blocks.MAGMA_BLOCK::getDefaultState,
    ) { world: World, pos: BlockPos ->
        world.getFluidState(pos).isIn(FluidTags.LAVA)
    }

    @JvmField
    val NATURAL: List<Block> = listOf(
        MUTATED_SOIL,
        POROUS_NETHERRACK,
    )

    //    Buildings    //

    @JvmField
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
    )

    //    Foods    //

    @JvmField
    val SPONGE_CAKE: Block = HayBlock(blockSettings(Blocks.HAY_BLOCK).sounds(BlockSoundGroup.WOOL))

    @JvmField
    val SWEET_BERRIES_CAKE: Block = object : Block(blockSettings(Blocks.CAKE)) {
        override fun getOutlineShape(
            state: BlockState,
            world: BlockView,
            pos: BlockPos,
            context: ShapeContext,
        ): VoxelShape = createCuboidShape(1.0, 0.0, 1.0, 15.0, 8.0, 15.0)
    }

    @JvmField
    val FOODS: List<Block> = listOf(
        SPONGE_CAKE,
        SWEET_BERRIES_CAKE,
    )

    //    Mechanics    //

    @JvmField
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
    )

    //    Misc    //

    @JvmField
    val BACKPACK_INTERFACE: Block = HTBackpackInterfaceBlock

    @JvmField
    val BUFFER: Block =
        HTBlockWithEntity.build(RagiumBlockEntityTypes.BUFFER, blockSettings(Blocks.SMOOTH_STONE))

    @JvmField
    val ENCHANTMENT_BOOKSHELF: Block =
        HTBlockWithEntity.build(RagiumBlockEntityTypes.ENCHANTMENT_BOOKSHELF, blockSettings(Blocks.BOOKSHELF))

    @JvmField
    val ITEM_DISPLAY: Block = HTItemDisplayBlock

    @JvmField
    val ROPE: Block = HTRopeBlock

    @JvmField
    val SHAFT: Block = HTThinPillarBlock(blockSettings(Blocks.CHAIN))

    @JvmField
    val INFESTING: Block = HTInfectingBlock

    @JvmField
    val MISC: List<Block> = listOf(
        BACKPACK_INTERFACE,
        BUFFER,
        ENCHANTMENT_BOOKSHELF,
        ITEM_DISPLAY,
        ROPE,
        SHAFT,
    )*/
}
