package hiiragi283.ragium.api.material.attribute

import hiiragi283.ragium.api.material.HTMaterialPrefix

/**
 * 素材のデフォルトのプレフィックスを保持する属性のクラス
 */
@JvmInline
value class HTDefaultPrefixMaterialAttribute(val prefix: HTMaterialPrefix) : HTMaterialAttribute
