package hiiragi283.ragium.common

import hiiragi283.ragium.api.data.lang.HTLangName
import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.registry.impl.HTBasicDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredBlock
import hiiragi283.ragium.setup.RagiumBlocks
import net.minecraft.util.StringRepresentable
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.WallBlock

enum class HTDecorationType(private val enName: String, private val jaName: String) :
    StringRepresentable,
    HTLangName {
    RAGI_BRICK("Ragi-Brick", "らぎレンガ"),
    AZURE_TILE("Azure Tile", "紺碧のタイル"),
    ELDRITCH_STONE("Eldritch Stone", "エルドリッチストーン"),
    ELDRITCH_STONE_BRICK("Eldritch Stone Brick", "エルドリッチストーンレンガ"),
    PLASTIC_BRICK("Plastic Brick", "プラスチックレンガ"),
    PLASTIC_TILE("Plastic Tile", "プラスチックタイル"),
    BLUE_NETHER_BRICK("Blue Nether Brick", "青いネザーレンガ"),
    SPONGE_CAKE("Sponge Cake", "スポンジケーキ"),
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

    override fun getTranslatedName(type: HTLanguageType): String = when (type) {
        HTLanguageType.EN_US -> enName
        HTLanguageType.JA_JP -> jaName
    }

    override fun getSerializedName(): String = name.lowercase()
}
