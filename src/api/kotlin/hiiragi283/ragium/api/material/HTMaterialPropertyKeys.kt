package hiiragi283.ragium.api.material

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.material.prefix.HTTagPrefix
import hiiragi283.ragium.api.property.HTPropertyKey
import net.minecraft.network.chat.MutableComponent

object HTMaterialPropertyKeys {
    @JvmField
    val MATERIAL_TYPE: HTPropertyKey.Defaulted<HTMaterialType> =
        HTPropertyKey.Defaulted(commonId("main_prefix")) { HTMaterialType.DEFAULT }

    @JvmStatic
    private val nameKeys: MutableMap<HTTagPrefix, HTPropertyKey.Simple<MutableComponent>> = mutableMapOf()

    @JvmStatic
    fun getNameKey(prefix: HTTagPrefix): HTPropertyKey.Simple<MutableComponent> =
        nameKeys.computeIfAbsent(prefix) { prefixIn: HTTagPrefix ->
            HTPropertyKey.Simple(commonId("part_name/${prefix.name}"))
        }

    //    Recipe    //

    @JvmField
    val ORE_CRUSHED_COUNT: HTPropertyKey.Defaulted<Int> =
        HTPropertyKey.Defaulted(RagiumAPI.id("ore_crushed_count")) { 3 }
}
