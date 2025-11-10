package hiiragi283.ragium.impl.data.recipe.material

import hiiragi283.ragium.api.data.recipe.material.HTMaterialRecipeData
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems

object FoodMaterialRecipeData {
    //    Cutting    //

    @JvmField
    val RAGI_CHERRY_PULP: HTMaterialRecipeData = HTMaterialRecipeData.create {
        addInput(RagiumItems.RAGI_CHERRY)

        addOutput(RagiumItems.RAGI_CHERRY_PULP, null, 2)
    }

    @JvmField
    val SWEET_BERRIES_CAKE_SLICE: HTMaterialRecipeData = HTMaterialRecipeData.create {
        addInput(RagiumBlocks.SWEET_BERRIES_CAKE)

        addOutput(RagiumItems.SWEET_BERRIES_CAKE_SLICE, null, 7)
    }
}
