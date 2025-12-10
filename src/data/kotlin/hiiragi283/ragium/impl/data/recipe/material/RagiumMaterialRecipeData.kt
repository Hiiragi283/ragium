package hiiragi283.ragium.impl.data.recipe.material

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeData
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

data object RagiumMaterialRecipeData {
    //    Raginite    //

    @JvmField
    val RAGINITE_ORE: HTRecipeData = HTRecipeData.create {
        addInput(CommonMaterialPrefixes.ORE, RagiumMaterialKeys.RAGINITE)

        addOutput(RagiumItems.getDust(RagiumMaterialKeys.RAGINITE), CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE, 8)
        addOutput(RagiumItems.getDust(RagiumMaterialKeys.RAGINITE), CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE, 4, 1 / 2f)
        addOutput(
            RagiumItems.getGem(RagiumMaterialKeys.RAGI_CRYSTAL),
            CommonMaterialPrefixes.GEM,
            RagiumMaterialKeys.RAGI_CRYSTAL,
            1,
            1 / 4f,
        )
        setSuffix("_from_ore")
    }

    @JvmField
    val RAGI_ALLOY: HTRecipeData = HTRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.COPPER)
        addInput(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE, 2)

        addOutput(RagiumItems.getIngot(RagiumMaterialKeys.RAGI_ALLOY), CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
    }

    @JvmField
    val ADVANCED_RAGI_ALLOY: HTRecipeData = HTRecipeData.create {
        ingotOrDust(RagiumMaterialKeys.RAGI_ALLOY)
        addInput(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.GLOWSTONE, 2)

        addOutput(
            RagiumItems.getIngot(RagiumMaterialKeys.ADVANCED_RAGI_ALLOY),
            CommonMaterialPrefixes.INGOT,
            RagiumMaterialKeys.ADVANCED_RAGI_ALLOY,
        )
    }

    @JvmField
    val RAGI_CRYSTAL_ORE: HTRecipeData = createGemOre(RagiumMaterialKeys.RAGI_CRYSTAL)

    @JvmField
    val RAGI_CRYSTAL: HTRecipeData = HTRecipeData.create {
        gemOrDust(VanillaMaterialKeys.DIAMOND)
        addInput(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE, 6)

        addOutput(RagiumItems.getGem(RagiumMaterialKeys.RAGI_CRYSTAL), CommonMaterialPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
    }

    //    Azure    //

    @JvmField
    val AZURE_SHARD: HTRecipeData = HTRecipeData.create {
        gemOrDust(VanillaMaterialKeys.AMETHYST)
        gemOrDust(VanillaMaterialKeys.LAPIS)

        addOutput(RagiumItems.getGem(RagiumMaterialKeys.AZURE), CommonMaterialPrefixes.GEM, RagiumMaterialKeys.AZURE, 2)
    }

    @JvmField
    val AZURE_STEEL: HTRecipeData = HTRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.IRON)
        gemOrDust(RagiumMaterialKeys.AZURE, 2)

        addOutput(RagiumItems.getIngot(RagiumMaterialKeys.AZURE_STEEL), CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.AZURE_STEEL)
    }

    //    Deep    //

    @JvmField
    val DEEP_STEEL: HTRecipeData = HTRecipeData.create {
        ingotOrDust(RagiumMaterialKeys.AZURE_STEEL, 4)
        addInput(CommonMaterialPrefixes.SCRAP, RagiumMaterialKeys.DEEP_STEEL, 4)

        addOutput(RagiumItems.getIngot(RagiumMaterialKeys.DEEP_STEEL), CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.DEEP_STEEL)
    }

    //    Crimson    //

    @JvmField
    val CRIMSON_ORE: HTRecipeData = createGemOre(RagiumMaterialKeys.CRIMSON_CRYSTAL)

    //    Warped    //

    @JvmField
    val WARPED_ORE: HTRecipeData = createGemOre(RagiumMaterialKeys.WARPED_CRYSTAL)

    //    Eldritch    //

    @JvmField
    val ELDRITCH_FLUX: HTRecipeData = HTRecipeData.create {
        addInput(RagiumModTags.Items.ELDRITCH_PEARL_BINDER)
        addInput(RagiumFluidContents.CRIMSON_BLOOD, RagiumConst.MOLTEN_TO_GEM / 2)
        addInput(RagiumFluidContents.DEW_OF_THE_WARP, RagiumConst.MOLTEN_TO_GEM / 2)

        addOutput(RagiumFluidContents.ELDRITCH_FLUX, RagiumConst.MOLTEN_TO_GEM)
    }

    @JvmField
    val ELDRITCH_FLUX_CRIMSON: HTRecipeData = HTRecipeData.create {
        addInput(RagiumModTags.Items.ELDRITCH_PEARL_BINDER, count = 2)
        gemOrDust(RagiumMaterialKeys.CRIMSON_CRYSTAL)
        addInput(RagiumFluidContents.DEW_OF_THE_WARP, RagiumConst.MOLTEN_TO_GEM)

        addOutput(RagiumFluidContents.ELDRITCH_FLUX, RagiumConst.MOLTEN_TO_GEM * 2)
        addOutput(RagiumItems.getGem(RagiumMaterialKeys.ELDRITCH_PEARL), CommonMaterialPrefixes.GEM, RagiumMaterialKeys.ELDRITCH_PEARL)

        setSuffix("_crimson")
    }

    @JvmField
    val ELDRITCH_FLUX_WARPED: HTRecipeData = HTRecipeData.create {
        addInput(RagiumModTags.Items.ELDRITCH_PEARL_BINDER, count = 2)
        gemOrDust(RagiumMaterialKeys.WARPED_CRYSTAL)
        addInput(RagiumFluidContents.CRIMSON_BLOOD, RagiumConst.MOLTEN_TO_GEM)

        addOutput(RagiumFluidContents.ELDRITCH_FLUX, RagiumConst.MOLTEN_TO_GEM * 2)
        addOutput(RagiumItems.getGem(RagiumMaterialKeys.ELDRITCH_PEARL), CommonMaterialPrefixes.GEM, RagiumMaterialKeys.ELDRITCH_PEARL)

        setSuffix("_warped")
    }

    //    Other    //

    @JvmField
    val NIGHT_METAL: HTRecipeData = HTRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.GOLD)
        addInput(Tags.Items.OBSIDIANS)
        addInput(Items.BLACKSTONE, count = 4)

        addOutput(RagiumItems.getIngot(RagiumMaterialKeys.NIGHT_METAL), CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.NIGHT_METAL)
    }

    @JvmField
    val IRIDESCENT_POWDER: HTRecipeData = HTRecipeData.create {
        addInput(Tags.Items.NETHER_STARS)
        addInput(Items.HEAVY_CORE)
        addInput(Items.HEART_OF_THE_SEA)
        addInput(Items.DRAGON_BREATH)

        addInput(RagiumFluidContents.DESTABILIZED_RAGINITE, 1000)

        addOutput(RagiumItems.IRIDESCENT_POWDER, null)
    }

    @JvmStatic
    private fun createGemOre(material: HTMaterialLike): HTRecipeData = HTRecipeData.create {
        addInput(CommonMaterialPrefixes.ORE, material)

        addOutput(RagiumItems.getGem(material), CommonMaterialPrefixes.GEM, material, 2)
        setSuffix("_from_ore")
    }
}
