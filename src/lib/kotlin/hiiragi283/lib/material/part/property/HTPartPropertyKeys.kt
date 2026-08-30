package hiiragi283.lib.material.part.property

import hiiragi283.lib.data.lang.HTLangPatternProvider
import hiiragi283.lib.material.HTMaterial
import hiiragi283.lib.property.HTPropertyKey
import hiiragi283.lib.tag.HTTagPrefix
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.resources.Identifier
import net.minecraft.world.level.block.state.BlockBehaviour

data object HTPartPropertyKeys {
    /**
     * 基準値に対する数量を管理する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val ITEM_SCALE: HTPropertyKey.Defaulted<(Float, HTMaterial) -> Float> = HTPropertyKey.Defaulted(RagiumAPI.id("item_scale")) { base: Float, _ -> base }

    @JvmField
    val TAG_PREFIX: HTPropertyKey.Simple<HTTagPrefix> = HTPropertyKey.Simple(RagiumAPI.id("tag_prefix"))

    //    Block    //

    /**
     * ブロックの[プロパティ][BlockBehaviour.Properties]を管理する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val BLOCK_PROP: HTPropertyKey.Simple<BlockBehaviour.Properties> = HTPropertyKey.Simple(RagiumAPI.id("block_properties"))

    /**
     * 鉱石ブロックの母岩部分のテクスチャを管理する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val ORE_STONE_TEX: HTPropertyKey.Simple<Identifier> = HTPropertyKey.Simple(RagiumAPI.id("ore_stone_tex"))

    //    Data Gen    //

    // Data Map
    /**
     * かまど燃料の燃焼時間の倍率を管理する[プロパティキー][HTPropertyKey]
     * @since 0.10.0
     */
    @JvmField
    val FUEL_SCALE: HTPropertyKey.Simple<Float> = HTPropertyKey.Simple(RagiumAPI.id("fuel_scale"))

    // Lang
    /**
     * 翻訳のパターンを管理する[プロパティキー][HTPropertyKey]
     */
    @JvmField
    val LANG_PATTERN: HTPropertyKey.Defaulted<HTLangPatternProvider> = HTPropertyKey.Defaulted(RagiumAPI.id("lang_pattern"), HTLangPatternProvider.IDENTITY)
}
