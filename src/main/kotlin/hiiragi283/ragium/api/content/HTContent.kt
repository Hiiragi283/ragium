package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.data.HTLangType
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.registry.tag.TagKey

interface HTContent<T : ItemConvertible> :
    HTRegistryContent<T>,
    ItemConvertible {
    val commonTagKey: TagKey<Item>?
        get() = null

    fun getTranslation(type: HTLangType): String

    override fun asItem(): Item = value.asItem()

    //    Material    //

    interface Material<T : ItemConvertible> :
        HTContent<T>,
        HTTranslationFormatter {
        val material: HTMaterialKey

        override fun getTranslation(type: HTLangType): String = getTranslation(
            type,
            RagiumMaterialTranslations.entries.firstOrNull { it.key == material }
                ?: RagiumMaterialTranslations.UNDEFINED,
        )

        val tagPrefix: HTTagPrefix

        val prefixedTagKey: TagKey<Item>
            get() = tagPrefix.createTag(material)

        override val commonTagKey: TagKey<Item>
            get() = tagPrefix.commonTagKey
    }

    //    Tier    //

    interface Tier<T : ItemConvertible> :
        HTContent<T>,
        HTTranslationFormatter {
        val tier: HTMachineTier

        override fun getTranslation(type: HTLangType): String = getTranslation(type, tier)
    }
}
