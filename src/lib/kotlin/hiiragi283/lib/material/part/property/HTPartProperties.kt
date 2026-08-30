package hiiragi283.lib.material.part.property

import hiiragi283.lib.data.lang.HTLangPatternProvider
import hiiragi283.lib.material.HTMaterial
import hiiragi283.lib.material.part.HTPart
import hiiragi283.lib.property.HTPropertyMap
import hiiragi283.lib.property.getOrDefault
import hiiragi283.lib.tag.HTTagPrefix

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
val HTPart.tagPrefix: HTTagPrefix? get() = this[HTPartPropertyKeys.TAG_PREFIX]

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
fun HTPart.getScaledAmount(base: Int, material: HTMaterial): Float = this.getScaledAmount(base.toFloat(), material)

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
fun HTPart.getScaledAmount(base: Float, material: HTMaterial): Float = this.getOrDefault(HTPartPropertyKeys.ITEM_SCALE)(base, material)

// Mutable

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
fun HTPropertyMap.Mutable.addNamePattern(enPattern: String, jaPattern: String) {
    this.addNamePattern(HTLangPatternProvider(enPattern, jaPattern))
}

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
fun HTPropertyMap.Mutable.addNamePattern(value: HTLangPatternProvider) {
    this[HTPartPropertyKeys.LANG_PATTERN] = value
}
