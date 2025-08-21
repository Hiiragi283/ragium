package hiiragi283.ragium.util.variant

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTLanguageType
import hiiragi283.ragium.api.registry.HTDeferredBlockHolder
import hiiragi283.ragium.api.registry.HTSimpleDeferredBlockHolder
import hiiragi283.ragium.api.registry.HTVariantKey
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.WallBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor

enum class HTDecorationVariant(
    val properties: BlockBehaviour.Properties,
    private val enUsPattern: String,
    private val jaJpPattern: String,
) : HTVariantKey {
    RAGI_STONE(Blocks.STONE, MapColor.COLOR_RED, "Ragi-Stone %s", "らぎストーンの%s"),
    RAGI_STONE_BRICK(Blocks.STONE, MapColor.COLOR_RED, "Ragi-Stone Brick %s", "らぎストーンレンガの%s"),
    RAGI_STONE_SQUARE(Blocks.STONE, MapColor.COLOR_RED, "Ragi-Stone (Square) %s", "らぎストーン（正方）の%s"),
    AZURE_TILE(Blocks.STONE, MapColor.TERRACOTTA_BLUE, "Azure Tile %s", "紺碧のタイルの%s"),
    EMBER_STONE(Blocks.AMETHYST_BLOCK, MapColor.COLOR_ORANGE, "Ember Stone %s", "熾火石の%s"),
    PLASTIC_BLOCK(Blocks.COPPER_BLOCK, MapColor.NONE, "Plastic Block %s", "プラスチックブロックの%s"),
    BLUE_NETHER_BRICK(Blocks.NETHER_BRICKS, MapColor.COLOR_BLUE, "Blue Nether Brick %s", "青いネザーレンガの%s"),
    SPONGE_CAKE(Blocks.YELLOW_WOOL, "Sponge Cake %s", "スポンジケーキの%s"),
    ;

    constructor(parent: Block, enUsPattern: String, jaJpPattern: String) : this(
        BlockBehaviour.Properties.ofFullCopy(
            parent,
        ),
        enUsPattern,
        jaJpPattern,
    )

    constructor(parent: Block, mapColor: MapColor, enUsPattern: String, jaJpPattern: String) : this(
        BlockBehaviour.Properties.ofFullCopy(parent).mapColor(mapColor),
        enUsPattern,
        jaJpPattern,
    )

    val base: HTSimpleDeferredBlockHolder get() = RagiumBlocks.DECORATION_MAP[this]!!
    val slab: HTDeferredBlockHolder<SlabBlock, BlockItem> get() = RagiumBlocks.SLABS[this]!!
    val stairs: HTDeferredBlockHolder<StairBlock, BlockItem> get() = RagiumBlocks.STAIRS[this]!!
    val wall: HTDeferredBlockHolder<WallBlock, BlockItem> get() = RagiumBlocks.WALLS[this]!!

    val textureName: ResourceLocation
        get() = when (this) {
            RAGI_STONE_BRICK -> "ragi_stone_bricks"
            AZURE_TILE -> "azure_tiles"
            BLUE_NETHER_BRICK -> "blue_nether_bricks"
            else -> serializedName
        }.let(RagiumAPI::id).withPrefix("block/")

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enUsPattern
        HTLanguageType.JA_JP -> jaJpPattern
    }.replace("%s", value)

    override fun getSerializedName(): String = name.lowercase()
}
