package hiiragi283.ragium.api.material

import hiiragi283.ragium.api.material.attribute.HTMaterialAttribute
import hiiragi283.ragium.api.material.attribute.HTTranslatedNameMaterialAttribute

inline fun <reified T : HTMaterialAttribute> HTMaterialDefinition.get(): T? = get(T::class.java)

fun HTMaterialDefinition.Builder.addName(enName: String, jaName: String) {
    add(HTTranslatedNameMaterialAttribute(enName, jaName))
}
