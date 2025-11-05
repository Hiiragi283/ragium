package hiiragi283.ragium.api.material

import hiiragi283.ragium.api.material.attribute.HTDefaultPrefixMaterialAttribute
import hiiragi283.ragium.api.material.attribute.HTLangNameMaterialAttribute
import hiiragi283.ragium.api.material.attribute.HTMaterialAttribute

inline fun <reified T : HTMaterialAttribute> HTMaterialDefinition.get(): T? = get(T::class.java)

fun HTMaterialDefinition.getDefaultPrefix(): HTMaterialPrefix? = get<HTDefaultPrefixMaterialAttribute>()?.prefix

// Builder
fun HTMaterialDefinition.Builder.addDefaultPrefix(prefix: HTMaterialPrefix) {
    add(HTDefaultPrefixMaterialAttribute(prefix))
}

fun HTMaterialDefinition.Builder.addName(enName: String, jaName: String) {
    add(HTLangNameMaterialAttribute(enName, jaName))
}
