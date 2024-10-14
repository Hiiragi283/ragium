package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.data.HTLangType
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.registry.tag.TagKey

interface HTContent<T : ItemConvertible> :
    HTEntryDelegated<T>,
    ItemConvertible {

    val tagKey: TagKey<Item>?
        get() = null

    override fun asItem(): Item = value.asItem()

    //    Material    //

    interface Material<T : ItemConvertible> : HTContent<T>, HTTranslationFormatter, RagiumMaterials.Holder {
        fun getTranslation(type: HTLangType): String = getTranslation(type, material)
    }
}
