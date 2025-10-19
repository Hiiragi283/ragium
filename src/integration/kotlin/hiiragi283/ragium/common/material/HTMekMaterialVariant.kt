package hiiragi283.ragium.common.material

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.material.HTMaterialVariant
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.createCommonTag
import hiiragi283.ragium.api.tag.createTagKey
import mekanism.common.tags.MekanismTags
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

enum class HTMekMaterialVariant(private val enPattern: String, private val jaPattern: String, private val tagPrefix: String) :
    HTMaterialVariant.ItemTag {
    // Common
    DIRTY_DUST("Dirty %s Dust", "汚れた%sの粉", "dirty_dusts"),
    CLUMP("%s Clump", "%sの塊", "clumps"),
    SHARD("%s Shard", "%sの欠片", "shards"),
    CRYSTAL("%s Crystal", "%sの結晶", "crystals"),
    PELLET("%s Pellet", "%sペレット", "pellets"),

    // Mekanism
    ENRICHED("Enriched %s", "濃縮%s", "enriched"),
    ;

    override val itemCommonTag: TagKey<Item>
        get() = when (this) {
            DIRTY_DUST -> MekanismTags.Items.DIRTY_DUSTS
            CLUMP -> MekanismTags.Items.CLUMPS
            SHARD -> MekanismTags.Items.SHARDS
            CRYSTAL -> MekanismTags.Items.CRYSTALS
            PELLET -> RagiumCommonTags.Items.PELLETS
            ENRICHED -> MekanismTags.Items.ENRICHED
        }

    override fun canGenerateTag(): Boolean = true

    override fun itemTagKey(path: String): TagKey<Item> = when (this) {
        ENRICHED -> Registries.ITEM.createTagKey(RagiumAPI.id(tagPrefix, path))
        else -> Registries.ITEM.createCommonTag(tagPrefix, path)
    }

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enPattern
        HTLanguageType.JA_JP -> jaPattern
    }.replace("%s", value)

    override fun variantName(): String = name.lowercase()
}
