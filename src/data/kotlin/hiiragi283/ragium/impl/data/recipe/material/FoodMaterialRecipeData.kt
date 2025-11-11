package hiiragi283.ragium.impl.data.recipe.material

import hiiragi283.ragium.api.data.recipe.HTRecipeData
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems

object FoodMaterialRecipeData {
    //    Cutting    //

    @JvmField
    val RAGI_CHERRY_PULP: HTRecipeData = HTRecipeData.create {
        addInput(RagiumItems.RAGI_CHERRY)

        addOutput(RagiumItems.RAGI_CHERRY_PULP, null, 2)
    }

    @JvmField
    val SWEET_BERRIES_CAKE_SLICE: HTRecipeData = HTRecipeData.create {
        addInput(RagiumBlocks.SWEET_BERRIES_CAKE)

        addOutput(RagiumItems.SWEET_BERRIES_CAKE_SLICE, null, 7)
    }
}
