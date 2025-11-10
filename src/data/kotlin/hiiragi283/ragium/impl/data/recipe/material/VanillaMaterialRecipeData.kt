package hiiragi283.ragium.impl.data.recipe.material

import hiiragi283.ragium.api.data.recipe.material.HTMaterialRecipeData
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

data object VanillaMaterialRecipeData {
    //    Netherite    //

    @JvmField
    val NETHERITE_SCRAP: HTMaterialRecipeData = HTMaterialRecipeData.create {
        addInput(Tags.Items.ORES_NETHERITE_SCRAP)

        addOutput(Items.NETHERITE_SCRAP, CommonMaterialPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE, 2)
    }

    @JvmField
    val NETHERITE: HTMaterialRecipeData = HTMaterialRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.GOLD, 4)
        addInput(CommonMaterialPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE, 4)

        addOutput(Items.NETHERITE_INGOT, CommonMaterialPrefixes.INGOT, VanillaMaterialKeys.NETHERITE, 2)
    }

    //    Other    //

    @JvmField
    val AMETHYST_DUST: HTMaterialRecipeData = HTMaterialRecipeData.create {
        addInput(CommonMaterialPrefixes.GEM, VanillaMaterialKeys.AMETHYST)

        addOutput(RagiumItems.getDust(VanillaMaterialKeys.AMETHYST), CommonMaterialPrefixes.DUST, VanillaMaterialKeys.AMETHYST)
    }

    @JvmField
    val ECHO_DUST: HTMaterialRecipeData = HTMaterialRecipeData.create {
        addInput(CommonMaterialPrefixes.GEM, VanillaMaterialKeys.ECHO)

        addOutput(RagiumItems.getDust(VanillaMaterialKeys.ECHO), CommonMaterialPrefixes.DUST, VanillaMaterialKeys.ECHO)
    }

    @JvmField
    val BLACKSTONE_DUST: HTMaterialRecipeData = HTMaterialRecipeData.create {
        addInput(Items.BLACKSTONE)

        addOutput(RagiumItems.getDust(VanillaMaterialKeys.BLACKSTONE), CommonMaterialPrefixes.DUST, VanillaMaterialKeys.BLACKSTONE, 4)
    }

    @JvmField
    val OBSIDIAN_DUST: HTMaterialRecipeData = HTMaterialRecipeData.create {
        addInput(Tags.Items.OBSIDIANS_NORMAL)

        addOutput(RagiumItems.getDust(VanillaMaterialKeys.OBSIDIAN), CommonMaterialPrefixes.DUST, VanillaMaterialKeys.OBSIDIAN, 4)
    }
}
