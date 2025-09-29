package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.common.material.HTItemMaterialVariant

class HTIngredientHelperImpl : HTIngredientHelper {
    override fun fuelOrDust(material: HTMaterialType, count: Int): HTItemIngredient = multiVariants(
        material,
        HTItemMaterialVariant.DUST,
        HTItemMaterialVariant.FUEL,
        count = count,
    )

    override fun gemOrDust(material: HTMaterialType, count: Int): HTItemIngredient = multiVariants(
        material,
        HTItemMaterialVariant.DUST,
        HTItemMaterialVariant.GEM,
        count = count,
    )

    override fun ingotOrDust(material: HTMaterialType, count: Int): HTItemIngredient = multiVariants(
        material,
        HTItemMaterialVariant.DUST,
        HTItemMaterialVariant.INGOT,
        count = count,
    )

    @Suppress("OVERRIDE_DEPRECATION")
    override fun gemOrDust(name: String, count: Int): HTItemIngredient = itemTags(
        HTItemMaterialVariant.DUST.itemTagKey(name),
        HTItemMaterialVariant.GEM.itemTagKey(name),
        count = count,
    )

    @Suppress("OVERRIDE_DEPRECATION")
    override fun ingotOrDust(name: String, count: Int): HTItemIngredient = itemTags(
        HTItemMaterialVariant.DUST.itemTagKey(name),
        HTItemMaterialVariant.INGOT.itemTagKey(name),
        count = count,
    )
}
