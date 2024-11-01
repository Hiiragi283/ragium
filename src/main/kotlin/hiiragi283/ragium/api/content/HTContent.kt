package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.data.HTLangType
import hiiragi283.ragium.api.machine.HTMachineTier
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.registry.tag.TagKey

interface HTContent<T : ItemConvertible> :
    HTRegistryContent<T>,
    ItemConvertible {
    val tagKey: TagKey<Item>?
        get() = null

    override fun asItem(): Item = value.asItem()

    //    Material    //

    interface Material<T : ItemConvertible> :
        HTContent<T>,
        HTTranslationFormatter,
        RagiumMaterials.Holder {
        fun getTranslation(type: HTLangType): String = getTranslation(type, material)
    }

    //    Tier    //

    interface Tier<T : ItemConvertible> :
        HTContent<T>,
        HTTranslationProvider {
        val tier: HTMachineTier

        override val enName: String
            get() = tier.enName
        override val jaName: String
            get() = tier.jaName
    }
}
