package hiiragi283.lib.material.property

import hiiragi283.lib.data.lang.HTLangName
import hiiragi283.lib.material.HTMaterial
import hiiragi283.lib.material.part.HTPartKey
import hiiragi283.lib.property.HTPropertyMap
import hiiragi283.lib.property.computeIfAbsent
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
fun HTMaterial.getDefaultPart(): HTDefaultPart? = this[HTMaterialPropertyKeys.DEFAULT_PART]

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
fun HTMaterial.getDefaultTag(): TagKey<Item>? = this.getDefaultPart()?.getTag(key)

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
fun HTMaterial.getDefaultHolderSet(getter: HolderGetter<Item>): HolderSet.Named<Item>? = this.getDefaultPart()?.getHolderSet(this.key, getter)

// Mutable
/**
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
fun HTPropertyMap.Mutable.setDefaultPart(prefixed: HTDefaultPart) {
    this[HTMaterialPropertyKeys.DEFAULT_PART] = prefixed
}

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
fun HTPropertyMap.Mutable.setName(enName: String, jaName: String) {
    this.setName(HTLangName(enName, jaName))
}

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
fun HTPropertyMap.Mutable.setName(value: HTLangName) {
    this[HTMaterialPropertyKeys.LANG_NAME] = value
}

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
fun HTPropertyMap.Mutable.addCustomName(part: HTPartKey, enName: String, jaName: String) {
    this.addCustomName(part, HTLangName(enName, jaName))
}

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
fun HTPropertyMap.Mutable.addCustomName(part: HTPartKey, value: HTLangName) {
    this.computeIfAbsent(HTMaterialPropertyKeys.CUSTOM_LANG_NAME) { it.plus(part to value) }
}

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
fun HTPropertyMap.Mutable.setTextureSet(name: String) {
    this.setTextureSet(HTMaterialTextureSet(name, HTMaterialTextureSet.DEFAULT))
}

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
fun HTPropertyMap.Mutable.setTextureSet(name: String, parent: HTMaterialTextureSet) {
    this.setTextureSet(HTMaterialTextureSet(name, parent))
}

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
fun HTPropertyMap.Mutable.setTextureSet(textureSet: HTMaterialTextureSet) {
    this[HTMaterialPropertyKeys.TEXTURE_SET] = textureSet
}
