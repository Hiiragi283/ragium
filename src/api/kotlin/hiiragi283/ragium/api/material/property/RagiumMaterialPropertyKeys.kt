package hiiragi283.ragium.api.material.property

import hiiragi283.core.api.property.HTPropertyKey
import hiiragi283.ragium.api.RagiumAPI

object RagiumMaterialPropertyKeys {
    @JvmField
    val FORMING_RECIPE_FLAG: HTPropertyKey<HTFormingRecipeFlag> =
        HTPropertyKey.create(RagiumAPI.id("forming_recipe_flag"), HTFormingRecipeFlag.enableAll())
}
