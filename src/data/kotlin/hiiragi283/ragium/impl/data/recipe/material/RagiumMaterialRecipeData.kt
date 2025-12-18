package hiiragi283.ragium.impl.data.recipe.material

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeData
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.HTMoldType
import hiiragi283.ragium.common.material.CommonMaterialKeys
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

        addMaterialOutput(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE, 8)
        addMaterialOutput(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE, 4, 1 / 2f)
        addMaterialOutput(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL, 1, 1 / 4f)
        setSuffix("_from_ore")
    }

    @JvmField
    val RAGI_ALLOY: HTRecipeData = HTRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.COPPER)
        addInput(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE, 2)

        addMaterialOutput(CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
    }

    @JvmField
    val ADVANCED_RAGI_ALLOY: HTRecipeData = HTRecipeData.create {
        ingotOrDust(RagiumMaterialKeys.RAGI_ALLOY)
        addInput(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.GLOWSTONE, 2)

        addMaterialOutput(CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY)
    }

    @JvmField
    val RAGI_CRYSTAL_ORE: HTRecipeData = createGemOre(RagiumMaterialKeys.RAGI_CRYSTAL)

    @JvmField
    val RAGI_CRYSTAL: HTRecipeData = HTRecipeData.create {
        gemOrDust(VanillaMaterialKeys.DIAMOND)
        addInput(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE, 6)

        addMaterialOutput(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
    }

    //    Azure    //

    @JvmField
    val AZURE_SHARD: HTRecipeData = HTRecipeData.create {
        gemOrDust(VanillaMaterialKeys.AMETHYST)
        gemOrDust(VanillaMaterialKeys.LAPIS)

        addMaterialOutput(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.AZURE, 2)
    }

    @JvmField
    val AZURE_DUST: HTRecipeData = HTRecipeData.create {
        addInput(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.AZURE)

        addMaterialOutput(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.AZURE)
    }

    @JvmField
    val AZURE_STEEL: HTRecipeData = HTRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.IRON)
        gemOrDust(RagiumMaterialKeys.AZURE, 2)

        addMaterialOutput(CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.AZURE_STEEL)
    }

    //    Deep    //

    @JvmField
    val DEEP_STEEL: HTRecipeData = HTRecipeData.create {
        ingotOrDust(RagiumMaterialKeys.AZURE_STEEL, 4)
        addInput(CommonMaterialPrefixes.SCRAP, RagiumMaterialKeys.DEEP_STEEL, 4)

        addMaterialOutput(CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.DEEP_STEEL)
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
        addMaterialOutput(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.ELDRITCH_PEARL)

        setSuffix("_crimson")
    }

    @JvmField
    val ELDRITCH_FLUX_WARPED: HTRecipeData = HTRecipeData.create {
        addInput(RagiumModTags.Items.ELDRITCH_PEARL_BINDER, count = 2)
        gemOrDust(RagiumMaterialKeys.WARPED_CRYSTAL)
        addInput(RagiumFluidContents.CRIMSON_BLOOD, RagiumConst.MOLTEN_TO_GEM)

        addOutput(RagiumFluidContents.ELDRITCH_FLUX, RagiumConst.MOLTEN_TO_GEM * 2)
        addMaterialOutput(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.ELDRITCH_PEARL)

        setSuffix("_warped")
    }

    //    Rubber    //

    @JvmField
    val RAW_RUBBER: HTRecipeData = HTRecipeData.create {
        addInput(RagiumFluidContents.LATEX, 1000)

        setCatalyst(HTMoldType.PLATE)

        addMaterialOutput(CommonMaterialPrefixes.RAW_MATERIAL, CommonMaterialKeys.RUBBER, 2)
    }

    @JvmField
    val RUBBER_SHEET: HTRecipeData = HTRecipeData.create {
        addInput(CommonMaterialPrefixes.RAW_MATERIAL, CommonMaterialKeys.RUBBER)
        addInput(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SULFUR)

        addMaterialOutput(CommonMaterialPrefixes.PLATE, CommonMaterialKeys.RUBBER, 2)
    }

    //    Night Metal    //

    @JvmField
    val NIGHT_METAL: HTRecipeData = HTRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.GOLD)
        addInput(Tags.Items.OBSIDIANS)
        addInput(Items.BLACKSTONE, count = 4)

        addMaterialOutput(CommonMaterialPrefixes.RAW_MATERIAL, RagiumMaterialKeys.NIGHT_METAL)
    }

    @JvmField
    val NIGHT_METAL_ALT: HTRecipeData = HTRecipeData.create {
        addInput(Items.GILDED_BLACKSTONE)
        addInput(Tags.Items.OBSIDIANS)

        addMaterialOutput(CommonMaterialPrefixes.RAW_MATERIAL, RagiumMaterialKeys.NIGHT_METAL, 2)

        setSuffix("_alt")
    }

    //    Other    //

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

    private fun HTRecipeData.Builder.addMaterialOutput(
        prefix: HTPrefixLike,
        material: HTMaterialLike,
        count: Int = 1,
        chance: Float = 1f,
    ): HTRecipeData.Builder =
        this.addOutput(RagiumItems.MATERIALS[prefix.asMaterialPrefix(), material.asMaterialKey()], prefix, material, count, chance)
}
