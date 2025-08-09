package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.util.HTLanguageType
import net.minecraft.tags.TagKey
import net.minecraft.util.StringRepresentable

interface HTVariantKey : StringRepresentable {
    fun translate(type: HTLanguageType, value: String): String

    interface Tagged<T : Any> : HTVariantKey {
        val tagKey: TagKey<T>
    }
}
