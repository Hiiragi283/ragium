package hiiragi283.ragium.data.server.recipe.compat

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTMaterialVariant
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.common.material.HTBlockMaterialVariant
import hiiragi283.ragium.common.material.HTItemMaterialVariant
import hiiragi283.ragium.common.material.HTMoltenCrystalData
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.impl.data.recipe.HTFluidTransformRecipeBuilder
import hiiragi283.ragium.integration.mekanism.HTMekMaterialVariant
import hiiragi283.ragium.integration.mekanism.RagiumMekanismAddon
import hiiragi283.ragium.setup.RagiumItems
import mekanism.api.IMekanismAccess
import mekanism.api.chemical.ChemicalStack
import mekanism.api.datagen.recipe.builder.ChemicalChemicalToChemicalRecipeBuilder
import mekanism.api.datagen.recipe.builder.ChemicalCrystallizerRecipeBuilder
import mekanism.api.datagen.recipe.builder.ItemStackChemicalToItemStackRecipeBuilder
import mekanism.api.datagen.recipe.builder.ItemStackToChemicalRecipeBuilder
import mekanism.api.datagen.recipe.builder.ItemStackToItemStackRecipeBuilder
import mekanism.api.datagen.recipe.builder.RotaryRecipeBuilder
import mekanism.api.recipes.ingredients.ChemicalStackIngredient
import mekanism.api.recipes.ingredients.ItemStackIngredient
import mekanism.api.recipes.ingredients.creator.IChemicalStackIngredientCreator
import mekanism.api.recipes.ingredients.creator.IFluidStackIngredientCreator
import mekanism.api.recipes.ingredients.creator.IItemStackIngredientCreator
import mekanism.common.registries.MekanismItems
import mekanism.common.tags.MekanismTags
import net.minecraft.world.item.Items

object RagiumMekanismRecipeProvider : HTRecipeProvider.Integration(RagiumConst.MEKANISM) {
    override fun buildRecipeInternal() {
        chemicalConversion()

        raginite()
        azure()
        molten()
        deep()
    }

    @JvmStatic
    private fun chemicalConversion() {
        fun toChemical(factory: (ItemStackIngredient, ChemicalStack) -> ItemStackToChemicalRecipeBuilder, prefix: String) {
            for (type: HTMaterialType in listOf(RagiumMaterialType.RAGINITE, RagiumMaterialType.AZURE)) {
                val name: String = type.serializedName
                // Dust -> Chemical
                factory(
                    itemHelper.from(HTItemMaterialVariant.DUST, type),
                    type.asStack(10),
                ).build(output, id("$prefix/$name/from_dust"))
                // Enriched -> Chemical
                factory(
                    itemHelper.from(HTMekMaterialVariant.ENRICHED, type),
                    type.asStack(80),
                ).build(output, id("$prefix/$name/from_enriched"))
            }
        }

        toChemical(ItemStackToChemicalRecipeBuilder::chemicalConversion, "chemical_conversion")
        toChemical(ItemStackToChemicalRecipeBuilder::oxidizing, "oxidizing")
    }

    @JvmStatic
    private fun raginite() {
        // Ore -> Dust
        ItemStackToItemStackRecipeBuilder
            .enriching(
                itemHelper.from(HTBlockMaterialVariant.ORE, RagiumMaterialType.RAGINITE),
                RagiumItems.getDust(RagiumMaterialType.RAGINITE).toStack(12),
            ).build(output, id("processing/raginite/from_ore"))
        // Enrich
        ItemStackToItemStackRecipeBuilder
            .enriching(
                itemHelper.from(HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE),
                RagiumMekanismAddon.getEnrichedStack(RagiumMaterialType.RAGINITE),
            ).build(output, id("enriching/enrich/raginite"))
        // Raginite + Copper -> Ragi-Alloy
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.ingotOrDust(HTVanillaMaterialType.COPPER, 1),
                chemicalHelper.from(RagiumMaterialType.RAGINITE, 40),
                RagiumItems.getIngot(RagiumMaterialType.RAGI_ALLOY).toStack(),
                false,
            ).build(output, id("metallurgic_infusing/ragi_alloy"))
        // Raginite + Gold -> Advanced Ragi-Alloy
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.ingotOrDust(HTVanillaMaterialType.GOLD, 1),
                chemicalHelper.from(RagiumMaterialType.RAGINITE, 80),
                RagiumItems.getIngot(RagiumMaterialType.ADVANCED_RAGI_ALLOY).toStack(),
                false,
            ).build(output, id("metallurgic_infusing/advanced_ragi_alloy"))

        // Raginite + Diamond -> Ragi-Crystal
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.gemOrDust(HTVanillaMaterialType.DIAMOND, 1),
                chemicalHelper.from(RagiumMaterialType.RAGINITE, 60),
                RagiumItems.getGem(RagiumMaterialType.RAGI_CRYSTAL).toStack(),
                false,
            ).build(output, id("metallurgic_infusing/ragi_crystal"))
        // Ore -> Crystal
        oreToGem(RagiumMaterialType.RAGI_CRYSTAL)

        // Raginite + Apple -> Ragi-Cherry
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.from(RagiumCommonTags.Items.FOODS_APPLE),
                chemicalHelper.from(RagiumMaterialType.RAGINITE, 40),
                RagiumItems.RAGI_CHERRY.toStack(),
                false,
            ).build(output, id("metallurgic_infusing/${RagiumConst.RAGI_CHERRY}"))
    }

    @JvmStatic
    private fun azure() {
        // Enrich
        ItemStackToItemStackRecipeBuilder
            .enriching(
                itemHelper.gemOrDust(RagiumMaterialType.AZURE, 1),
                RagiumMekanismAddon.getEnrichedStack(RagiumMaterialType.AZURE),
            ).build(output, id("enriching/enrich/azure"))
        // Azure + Amethyst -> Azure Shard
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.gemOrDust(HTVanillaMaterialType.AMETHYST, 1),
                chemicalHelper.from(RagiumMaterialType.AZURE, 10),
                RagiumItems.getGem(RagiumMaterialType.AZURE).toStack(),
                false,
            ).build(output, id("metallurgic_infusing/azure_shard"))
        // Azure + Iron -> Azure Steel
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.ingotOrDust(HTVanillaMaterialType.IRON, 1),
                chemicalHelper.from(RagiumMaterialType.AZURE, 40),
                RagiumItems.getIngot(RagiumMaterialType.AZURE_STEEL).toStack(),
                false,
            ).build(output, id("metallurgic_infusing/azure_steel"))

        // Ethene + Catalyst -> HDPE
        HTFluidTransformRecipeBuilder
            .solidifying(
                ingredientHelper.item(RagiumItems.POLYMER_CATALYST),
                ingredientHelper.fluid(MekanismTags.Fluids.ETHENE, 100),
                resultHelper.item(MekanismItems.HDPE_PELLET),
            ).save(output)
    }

    @JvmStatic
    private fun molten() {
        oreToGem(RagiumMaterialType.CRIMSON_CRYSTAL)
        oreToGem(RagiumMaterialType.WARPED_CRYSTAL)

        // Crimson + Warped -> Eldritch
        ChemicalChemicalToChemicalRecipeBuilder
            .chemicalInfusing(
                chemicalHelper.from(RagiumMaterialType.CRIMSON_CRYSTAL, 1),
                chemicalHelper.from(RagiumMaterialType.WARPED_CRYSTAL, 1),
                RagiumMaterialType.ELDRITCH_PEARL.asStack(1),
            ).build(output, id("chemical_infusing/eldritch_flux"))

        for (data: HTMoltenCrystalData in HTMoltenCrystalData.entries) {
            val molten: HTFluidContent<*, *, *> = data.molten
            val material: HTMaterialType = data.material
            // Fluid <-> Chemical
            RotaryRecipeBuilder
                .rotary(
                    fluidHelper.from(molten.commonTag, 1),
                    chemicalHelper.from(material, 1),
                    material.asStack(1),
                    molten.toStack(1),
                ).build(output, id("rotary/${molten.getPath()}"))
            // Chemical -> Item
            ChemicalCrystallizerRecipeBuilder
                .crystallizing(
                    chemicalHelper.from(material, HTMoltenCrystalData.MOLTEN_TO_GEM),
                    RagiumItems.getGem(material).toStack(),
                ).build(output, id("crystallizing/${material.serializedName}"))
        }
    }

    @JvmStatic
    private fun deep() {
        // Ore -> Scrap
        ItemStackToItemStackRecipeBuilder
            .enriching(
                itemHelper.from(RagiumCommonTags.Items.ORES_DEEP_SCRAP),
                RagiumItems.DEEP_SCRAP.toStack(2),
            ).build(output, id("processing/deep_steel/resonant_debris_to_scrap"))

        // Azure + Netherite Scrap -> Deep Scrap
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.from(Items.NETHERITE_SCRAP),
                chemicalHelper.from(RagiumMaterialType.AZURE, 80),
                RagiumItems.DEEP_SCRAP.toStack(),
                false,
            ).build(output, id("metallurgic_infusing/deep_scrap"))
        // Azure + Netherite Ingot -> Deep Ingot
        ItemStackChemicalToItemStackRecipeBuilder
            .metallurgicInfusing(
                itemHelper.ingotOrDust(HTVanillaMaterialType.NETHERITE, 1),
                chemicalHelper.from(RagiumMaterialType.AZURE, 160),
                RagiumItems.getIngot(RagiumMaterialType.DEEP_STEEL).toStack(),
                false,
            ).build(output, id("metallurgic_infusing/deep_steel"))
    }

    //    Extensions    //

    @JvmStatic
    private val itemHelper: IItemStackIngredientCreator = IMekanismAccess.INSTANCE.itemStackIngredientCreator()

    @JvmStatic
    private val fluidHelper: IFluidStackIngredientCreator = IMekanismAccess.INSTANCE.fluidStackIngredientCreator()

    @JvmStatic
    private val chemicalHelper: IChemicalStackIngredientCreator =
        IMekanismAccess.INSTANCE.chemicalStackIngredientCreator()

    @JvmStatic
    private fun HTMaterialType.asStack(size: Long): ChemicalStack = RagiumMekanismAddon.getChemical(this).asStack(size)

    @JvmStatic
    private fun IItemStackIngredientCreator.from(
        variant: HTMaterialVariant.ItemTag,
        material: HTMaterialType,
        count: Int = 1,
    ): ItemStackIngredient = from(variant.itemTagKey(material), count)

    @JvmStatic
    private fun IItemStackIngredientCreator.gemOrDust(material: HTMaterialType, count: Int = 1): ItemStackIngredient =
        from(this@RagiumMekanismRecipeProvider.gemOrDust(material), count)

    @JvmStatic
    private fun IItemStackIngredientCreator.ingotOrDust(material: HTMaterialType, count: Int = 1): ItemStackIngredient =
        from(this@RagiumMekanismRecipeProvider.ingotOrDust(material), count)

    @JvmStatic
    private fun IChemicalStackIngredientCreator.from(material: HTMaterialType, amount: Number): ChemicalStackIngredient =
        fromHolder(RagiumMekanismAddon.getChemical(material), amount.toLong())

    @JvmStatic
    private fun oreToGem(material: HTMaterialType) {
        ItemStackToItemStackRecipeBuilder
            .enriching(
                itemHelper.from(HTBlockMaterialVariant.ORE, material),
                RagiumItems.getGem(material).toStack(2),
            ).build(output, id("processing/${material.serializedName}/from_ore"))
    }
}
