package hiiragi283.ragium.impl.data.recipe.material

import hiiragi283.ragium.api.data.recipe.HTRecipeData
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.VanillaMaterialKeys

data object CommonMaterialRecipeData {
    @JvmField
    val STEEL_COAL: HTRecipeData = HTRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.IRON)
        fuelOrDust(VanillaMaterialKeys.COAL, 2)

        addOutput(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.STEEL)
        setSuffix("_from_coal")
    }

    @JvmField
    val STEEL_COKE: HTRecipeData = HTRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.IRON)
        fuelOrDust(CommonMaterialKeys.COAL_COKE)

        addOutput(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.STEEL)
        setSuffix("_from_coke")
    }

    @JvmField
    val INVAR: HTRecipeData = HTRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.IRON, 2)
        ingotOrDust(CommonMaterialKeys.Metals.NICKEL)

        addOutput(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.INVAR, 3)
    }

    @JvmField
    val ELECTRUM: HTRecipeData = HTRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.GOLD)
        ingotOrDust(CommonMaterialKeys.Metals.SILVER)

        addOutput(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.ELECTRUM, 2)
    }

    @JvmField
    val BRONZE: HTRecipeData = HTRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.COPPER, 3)
        ingotOrDust(CommonMaterialKeys.Metals.TIN)

        addOutput(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.BRONZE, 4)
    }

    @JvmField
    val BRASS: HTRecipeData = HTRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.COPPER, 3)
        ingotOrDust(CommonMaterialKeys.Metals.ZINC)

        addOutput(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.BRASS, 4)
    }

    @JvmField
    val CONSTANTAN: HTRecipeData = HTRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.COPPER)
        ingotOrDust(CommonMaterialKeys.Metals.NICKEL)

        addOutput(CommonMaterialPrefixes.INGOT, CommonMaterialKeys.Alloys.CONSTANTAN, 2)
    }
}
