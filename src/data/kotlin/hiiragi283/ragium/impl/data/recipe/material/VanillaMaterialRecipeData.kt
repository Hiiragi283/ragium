package hiiragi283.ragium.impl.data.recipe.material

import hiiragi283.ragium.api.data.recipe.material.HTMaterialRecipeData
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

data object VanillaMaterialRecipeData {
    //    Netherite    //

    @JvmField
    val NETHERITE_SCRAP: HTMaterialRecipeData = HTMaterialRecipeData.create {
        addInput(Tags.Items.ORES_NETHERITE_SCRAP)

        addOutput(Items.NETHERITE_SCRAP, 2)
        addOutput(CommonMaterialPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE, 2)
    }

    @JvmField
    val NETHERITE: HTMaterialRecipeData = HTMaterialRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.GOLD, 4)
        addInput(CommonMaterialPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE, 4)

        addOutput(Items.NETHERITE_INGOT, 2)
        addOutput(CommonMaterialPrefixes.INGOT, VanillaMaterialKeys.NETHERITE, 2)
    }
}
