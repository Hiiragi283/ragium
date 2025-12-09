package hiiragi283.ragium.api.material

import hiiragi283.ragium.api.data.lang.HTLangName
import hiiragi283.ragium.api.material.attribute.HTDefaultPrefixMaterialAttribute
import hiiragi283.ragium.api.material.attribute.HTLangNameMaterialAttribute
import hiiragi283.ragium.api.material.attribute.HTMaterialAttribute
import hiiragi283.ragium.api.material.prefix.HTMaterialPrefix
import hiiragi283.ragium.api.material.prefix.HTPrefixLike

inline fun <reified T : HTMaterialAttribute> HTMaterialDefinition.get(): T? = get(T::class.java)

fun HTMaterialDefinition.getDefaultPrefix(): HTMaterialPrefix? = get<HTDefaultPrefixMaterialAttribute>()?.prefix

// Builder
fun HTMaterialDefinition.Builder.addDefaultPrefix(prefix: HTPrefixLike) {
    add(HTDefaultPrefixMaterialAttribute(prefix.asMaterialPrefix()))
}

fun HTMaterialDefinition.Builder.addName(enName: String, jaName: String) {
    add(HTLangNameMaterialAttribute(enName, jaName))
}

fun HTMaterialDefinition.Builder.addName(delegate: HTLangName) {
    add(HTLangNameMaterialAttribute(delegate))
}
