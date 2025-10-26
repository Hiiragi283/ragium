package hiiragi283.ragium.common.variant

import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.data.lang.HTTranslationProvider
import hiiragi283.ragium.api.registry.impl.HTBasicDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredBlock
import hiiragi283.ragium.api.variant.HTVariantKey
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.WallBlock

enum class HTDecorationVariant(private val enPattern: String, private val jaPattern: String) :
    HTVariantKey,
    HTTranslationProvider {
    RAGI_BRICK("Ragi-Brick %s", "らぎレンガの%s"),
    AZURE_TILE("Azure Tile %s", "紺碧のタイルの%s"),
    ELDRITCH_STONE("Eldritch Stone %s", "エルドリッチストーンの%s"),
    ELDRITCH_STONE_BRICK("Eldritch Stone Brick %s", "エルドリッチストーンレンガの%s"),
    PLASTIC_BRICK("Plastic Brick %s", "プラスチックレンガの%s"),
    PLASTIC_TILE("Plastic Tile %s", "プラスチックタイルの%s"),
    BLUE_NETHER_BRICK("Blue Nether Brick %s", "青いネザーレンガの%s"),
    SPONGE_CAKE("Sponge Cake %s", "スポンジケーキの%s"),
    ;

    val base: HTDeferredBlock<*, *> get() = when (this) {
        RAGI_BRICK -> RagiumBlocks.RAGI_BRICKS
        AZURE_TILE -> RagiumBlocks.AZURE_TILES
        ELDRITCH_STONE -> RagiumBlocks.ELDRITCH_STONE
        ELDRITCH_STONE_BRICK -> RagiumBlocks.ELDRITCH_STONE_BRICKS
        PLASTIC_BRICK -> RagiumBlocks.PLASTIC_BRICKS
        PLASTIC_TILE -> RagiumBlocks.PLASTIC_TILES
        BLUE_NETHER_BRICK -> RagiumBlocks.BLUE_NETHER_BRICKS
        SPONGE_CAKE -> RagiumBlocks.SPONGE_CAKE
    }
    val slab: HTBasicDeferredBlock<SlabBlock> get() = RagiumBlocks.SLABS[this]!!
    val stairs: HTBasicDeferredBlock<StairBlock> get() = RagiumBlocks.STAIRS[this]!!
    val wall: HTBasicDeferredBlock<WallBlock> get() = RagiumBlocks.WALLS[this]!!

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enPattern
        HTLanguageType.JA_JP -> jaPattern
    }.replace("%s", value)

    override fun variantName(): String = name.lowercase()
}
