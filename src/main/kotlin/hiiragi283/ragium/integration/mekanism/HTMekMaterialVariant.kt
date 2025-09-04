package hiiragi283.ragium.integration.mekanism

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.material.HTMaterialVariant
import mekanism.common.tags.MekanismTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

enum class HTMekMaterialVariant(private val enUsPattern: String, private val jaJpPattern: String, private val tagPrefix: String) :
    HTMaterialVariant.ItemTag {
    // Common
    DIRTY_DUST("Dirty %s Dust", "汚れた%sの粉", "dirty_dusts"),
    CLUMP("%s Clump", "%sの塊", "clumps"),
    SHARD("%s Shard", "%sの欠片", "shards"),
    CRYSTAL("%s Crystal", "%sの結晶", "crystals"),

    // Mekanism
    ENRICHED("Enriched %s", "濃縮%s", "enriched"),
    ;

    override val itemCommonTag: TagKey<Item>
        get() = when (this) {
            DIRTY_DUST -> MekanismTags.Items.DIRTY_DUSTS
            CLUMP -> MekanismTags.Items.CLUMPS
            SHARD -> MekanismTags.Items.SHARDS
            CRYSTAL -> MekanismTags.Items.CRYSTALS
            ENRICHED -> MekanismTags.Items.ENRICHED
        }

    override fun canGenerateTag(): Boolean = true

    override fun itemTagKey(path: String): TagKey<Item> = when (this) {
        ENRICHED -> RagiumAPI::id
        else -> ::commonId
    }("$tagPrefix/$path").let(::itemTagKey)

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enUsPattern
        HTLanguageType.JA_JP -> jaJpPattern
    }.replace("%s", value)

    override fun getSerializedName(): String = name.lowercase()
}
