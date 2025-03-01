package hiiragi283.ragium.api.util

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Item

enum class HTCrateVariant(modifier: Int, val baseTag: TagKey<Item>) : StringRepresentable {
    WOODEN(1, ItemTags.PLANKS),
    IRON(8, HTTagPrefix.INGOT, VanillaMaterials.IRON),
    STEEL(16, HTTagPrefix.INGOT, CommonMaterials.STEEL),
    DEEP_STEEL(64, HTTagPrefix.INGOT, RagiumMaterials.DEEP_STEEL),
    DIAMOND(128, HTTagPrefix.GEM, VanillaMaterials.DIAMOND),
    NETHERITE(256, HTTagPrefix.INGOT, VanillaMaterials.NETHERITE),
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTCrateVariant> = StringRepresentable.fromEnum(HTCrateVariant::values)
    }

    constructor(modifier: Int, prefix: HTTagPrefix, key: HTMaterialKey) : this(modifier, prefix.createTag(key))

    val capacity: Int = 64 * modifier

    //    StringRepresentable    //

    override fun getSerializedName(): String = name.lowercase()
}
