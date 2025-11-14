package hiiragi283.ragium.impl.data.recipe.material

import com.simibubi.create.AllItems
import hiiragi283.ragium.api.data.recipe.HTRecipeData
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import net.minecraft.world.item.Items

data object CreateMaterialRecipeData {
    @JvmField
    val ANDESITE_ALLOY: HTRecipeData = HTRecipeData.create {
        addInput(
            CommonMaterialPrefixes.NUGGET.itemTagKey(VanillaMaterialKeys.IRON),
            CommonMaterialPrefixes.NUGGET.itemTagKey(CommonMaterialKeys.Metals.ZINC),
        )
        addInput(Items.ANDESITE)

        addOutput(AllItems.ANDESITE_ALLOY, null)
    }
}
