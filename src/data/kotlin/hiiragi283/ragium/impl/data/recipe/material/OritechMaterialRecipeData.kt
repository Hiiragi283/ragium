package hiiragi283.ragium.impl.data.recipe.material

import hiiragi283.ragium.api.data.recipe.material.HTMaterialRecipeData
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.ModMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import rearth.oritech.init.ItemContent

data object OritechMaterialRecipeData {
    @JvmField
    val ADAMANT: HTMaterialRecipeData = HTMaterialRecipeData.create {
        ingotOrDust(CommonMaterialKeys.Metals.NICKEL)
        gemOrDust(VanillaMaterialKeys.DIAMOND)

        addOutput(ItemContent.ADAMANT_INGOT, CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.ADAMANT, 2)
    }

    @JvmField
    val BIOSTEEL: HTMaterialRecipeData = HTMaterialRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.IRON)
        addInput(ItemContent.BIOMASS)

        addOutput(ItemContent.BIOSTEEL_INGOT, CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.BIOSTEEL)
    }

    @JvmField
    val DURATIUM: HTMaterialRecipeData = HTMaterialRecipeData.create {
        ingotOrDust(CommonMaterialKeys.Metals.PLATINUM)
        gemOrDust(VanillaMaterialKeys.NETHERITE)

        addOutput(ItemContent.DURATIUM_INGOT, CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.DURATIUM, 2)
    }

    @JvmField
    val ENERGITE: HTMaterialRecipeData = HTMaterialRecipeData.create {
        ingotOrDust(CommonMaterialKeys.Metals.NICKEL)
        gemOrDust(ModMaterialKeys.Gems.FLUXITE)

        addOutput(ItemContent.ENERGITE_INGOT, CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.ENERGITE, 2)
    }
}
