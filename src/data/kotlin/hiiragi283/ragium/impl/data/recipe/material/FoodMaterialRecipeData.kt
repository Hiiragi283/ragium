package hiiragi283.ragium.impl.data.recipe.material

import hiiragi283.ragium.api.data.recipe.HTRecipeData
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.registry.HTFluidHolderLike
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.FoodMaterialKeys
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
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

    //    Solidifying    //

    @JvmField
    val CHOCOLATE_INGOT: HTRecipeData = createCast(FoodMaterialKeys.CHOCOLATE, RagiumFluidContents.CHOCOLATE)

    @JvmField
    val RAW_MEAT_INGOT: HTRecipeData = createCast(FoodMaterialKeys.RAW_MEAT, RagiumFluidContents.MEAT)

    @JvmStatic
    private fun createCast(material: HTMaterialLike, fluid: HTFluidHolderLike): HTRecipeData = HTRecipeData.create {
        addInput(fluid, 250)

        setCatalyst(RagiumItems.getMold(CommonMaterialPrefixes.INGOT))

        addOutput(RagiumItems.getIngot(material), CommonMaterialPrefixes.INGOT, material)
    }
}
