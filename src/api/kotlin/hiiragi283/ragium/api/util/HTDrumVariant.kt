package hiiragi283.ragium.api.util

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import net.minecraft.tags.TagKey
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Item

enum class HTDrumVariant(modifier: Int, val baseTag: TagKey<Item>) : StringRepresentable {
    COPPER(8, HTTagPrefix.INGOT, VanillaMaterials.COPPER),
    GOLD(16, HTTagPrefix.INGOT, VanillaMaterials.GOLD),
    ALUMINUM(64, HTTagPrefix.INGOT, CommonMaterials.ALUMINUM),
    EMERALD(128, HTTagPrefix.GEM, VanillaMaterials.EMERALD),
    RAGIUM(256, HTTagPrefix.INGOT, RagiumMaterials.RAGIUM),
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTDrumVariant> = StringRepresentable.fromEnum(HTDrumVariant::values)
    }

    constructor(modifier: Int, prefix: HTTagPrefix, key: HTMaterialKey) : this(modifier, prefix.createTag(key))

    val capacity: Int = modifier * 1000

    //    StringRepresentable    //

    override fun getSerializedName(): String = name.lowercase()
}
