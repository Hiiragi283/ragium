package hiiragi283.ragium.data.recipe.integration

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.getOrThrow
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.registry.HTSimpleItemHolderLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.common.integration.HCIConstants
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.integration.mek.RagiumChemicals
import hiiragi283.ragium.common.integration.mek.RagiumMekItems
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumItems
import mekanism.api.chemical.ChemicalStack
import mekanism.api.datagen.recipe.builder.ItemStackChemicalToItemStackRecipeBuilder
import mekanism.api.datagen.recipe.builder.ItemStackToChemicalRecipeBuilder
import mekanism.api.datagen.recipe.builder.ItemStackToItemStackRecipeBuilder
import mekanism.api.recipes.ingredients.ItemStackIngredient
import mekanism.api.recipes.ingredients.creator.IChemicalStackIngredientCreator
import mekanism.api.recipes.ingredients.creator.IItemStackIngredientCreator
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess
import mekanism.common.registries.MekanismItems
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

data object RagiumMekRecipeProvider : HTSubRecipeProvider.Integration(RagiumAPI.MOD_ID, HCIConstants.MEKANISM) {
    val itemAccess: IItemStackIngredientCreator = IngredientCreatorAccess.item()
    val chemicalAccess: IChemicalStackIngredientCreator = IngredientCreatorAccess.chemicalStack()

    // Material
    private const val RAGINITE = "raginite"

    // Prefix
    private const val ENRICHING = "enriching"
    private const val METALLURGIC_INFUSING = "metallurgic_infusing"

    override fun buildRecipeInternal() {
        raginite()
    }

    @JvmStatic
    private fun raginite() {
        // Enriched
        ItemStackToItemStackRecipeBuilder.enriching(
            itemAccess.from(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGINITE),
            RagiumMekItems.ENRICHED_RAGINITE.toStack(),
        ).build(output, id(ENRICHING, "enriched", RAGINITE))
        // Chemical
        fun convertToChemical(prefix: String, factory: (ItemStackIngredient, ChemicalStack) -> ItemStackToChemicalRecipeBuilder) {
            factory(
                itemAccess.from(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGINITE),
                RagiumChemicals.RAGINITE.asStack(80),
            ).build(output, id(prefix, RAGINITE, "from_dust"))
            factory(
                itemAccess.from(RagiumMekItems.ENRICHED_RAGINITE),
                RagiumChemicals.RAGINITE.asStack(80),
            ).build(output, id(prefix, RAGINITE, "from_enriched"))
        }

        convertToChemical("chemical_conversion", ItemStackToChemicalRecipeBuilder::chemicalConversion)
        convertToChemical("oxidizing", ItemStackToChemicalRecipeBuilder::oxidizing)
        // Copper -> Ragi-Alloy
        ItemStackChemicalToItemStackRecipeBuilder.metallurgicInfusing(
            itemAccess.from(baseOrDust(VanillaMaterialKeys.COPPER)),
            chemicalAccess.fromHolder(RagiumChemicals.RAGINITE, 20),
            getOrThrow(CommonParts.INGOT, RagiumMaterialKeys.RAGI_ALLOY).toStack(),
            false,
        ).build(output, id(METALLURGIC_INFUSING, "ragi_alloy"))
        // Gold -> Advanced Ragi-Alloy
        ItemStackChemicalToItemStackRecipeBuilder.metallurgicInfusing(
            itemAccess.from(baseOrDust(VanillaMaterialKeys.GOLD)),
            chemicalAccess.fromHolder(RagiumChemicals.RAGINITE, 40),
            getOrThrow(CommonParts.INGOT, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY).toStack(),
            false,
        ).build(output, id(METALLURGIC_INFUSING, "advanced_ragi_alloy"))
        // Diamond -> Ragi-Crystal
        ItemStackChemicalToItemStackRecipeBuilder.metallurgicInfusing(
            itemAccess.from(baseOrDust(VanillaMaterialKeys.DIAMOND)),
            chemicalAccess.fromHolder(RagiumChemicals.RAGINITE, 60),
            getOrThrow(CommonParts.GEM, RagiumMaterialKeys.RAGI_CRYSTAL).toStack(),
            false,
        ).build(output, id(METALLURGIC_INFUSING, "ragi_crystal"))
        // Antimatter -> Ragi-Matter
        ItemStackChemicalToItemStackRecipeBuilder.metallurgicInfusing(
            itemAccess.from(MekanismItems.ANTIMATTER_PELLET),
            chemicalAccess.fromHolder(RagiumChemicals.RAGINITE, 640),
            RagiumItems.RAGI_MATTER.toStack(),
            false,
        ).build(output, id(METALLURGIC_INFUSING, "ragi_matter"))
    }

    @JvmStatic
    private fun getOrThrow(part: HTPartLike, material: HTMaterialLike): HTSimpleItemHolderLike = HiiragiCoreAccess.INSTANCE
        .registeredContents
        .items
        .getOrThrow(part, material)

    //    Extension    //

    fun IItemStackIngredientCreator.from(prefix: HTTagPrefix, material: HTMaterialLike, amount: Int = 1): ItemStackIngredient = this.from(prefix.itemTagKey(material), amount)

    fun IItemStackIngredientCreator.from(tagKeys: Iterable<TagKey<Item>>, amount: Int = 1): ItemStackIngredient = this.from(itemCreator.create(tagKeys), amount)
}
