package hiiragi283.ragium.impl.data.recipe.material

import hiiragi283.ragium.api.data.recipe.HTRecipeData
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.Items

data object VanillaMaterialRecipeData {
    //    Netherite    //

    @JvmField
    val NETHERITE: HTRecipeData = HTRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.GOLD, 4)
        addInput(CommonMaterialPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE, 4)

        addOutput(Items.NETHERITE_INGOT, CommonMaterialPrefixes.INGOT, VanillaMaterialKeys.NETHERITE, 2)
    }

    //    Other    //

    @JvmField
    val AMETHYST_DUST: HTRecipeData = HTRecipeData.create {
        addInput(CommonMaterialPrefixes.GEM, VanillaMaterialKeys.AMETHYST)

        addOutput(RagiumItems.getDust(VanillaMaterialKeys.AMETHYST), CommonMaterialPrefixes.DUST, VanillaMaterialKeys.AMETHYST)
    }

    @JvmField
    val ECHO_DUST: HTRecipeData = HTRecipeData.create {
        addInput(CommonMaterialPrefixes.GEM, VanillaMaterialKeys.ECHO)

        addOutput(RagiumItems.getDust(VanillaMaterialKeys.ECHO), CommonMaterialPrefixes.DUST, VanillaMaterialKeys.ECHO)
    }
}
