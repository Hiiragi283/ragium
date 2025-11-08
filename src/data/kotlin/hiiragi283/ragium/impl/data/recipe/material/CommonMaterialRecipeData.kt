package hiiragi283.ragium.impl.data.recipe.material

import hiiragi283.ragium.api.data.recipe.material.HTMaterialRecipeData
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.VanillaMaterialKeys

data object CommonMaterialRecipeData {
    @JvmField
    val STEEL_COAL: HTMaterialRecipeData = HTMaterialRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.IRON)
        fuelOrDust(VanillaMaterialKeys.COAL, 2)

        addOutput(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.STEEL)
    }

    @JvmField
    val STEEL_COKE: HTMaterialRecipeData = HTMaterialRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.IRON)
        fuelOrDust(CommonMaterialKeys.COAL_COKE)

        addOutput(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.STEEL)
    }

    @JvmField
    val INVAR: HTMaterialRecipeData = HTMaterialRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.IRON, 2)
        ingotOrDust(CommonMaterialKeys.Metals.NICKEL)

        addOutput(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.INVAR, 3)
    }

    @JvmField
    val ELECTRUM: HTMaterialRecipeData = HTMaterialRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.GOLD)
        ingotOrDust(CommonMaterialKeys.Metals.SILVER)

        addOutput(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.ELECTRUM, 2)
    }

    @JvmField
    val BRONZE: HTMaterialRecipeData = HTMaterialRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.COPPER, 3)
        ingotOrDust(CommonMaterialKeys.Metals.TIN)

        addOutput(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.BRONZE, 4)
    }

    @JvmField
    val BRASS: HTMaterialRecipeData = HTMaterialRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.COPPER, 3)
        ingotOrDust(CommonMaterialKeys.Metals.ZINC)

        addOutput(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.BRASS, 4)
    }

    @JvmField
    val CONSTANTAN: HTMaterialRecipeData = HTMaterialRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.COPPER)
        ingotOrDust(CommonMaterialKeys.Metals.NICKEL)

        addOutput(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.CONSTANTAN, 2)
    }
}
