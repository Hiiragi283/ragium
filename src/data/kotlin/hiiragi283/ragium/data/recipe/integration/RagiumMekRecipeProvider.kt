package hiiragi283.ragium.data.recipe.integration

import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.common.integration.HCIConstants
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.integration.mek.RagiumChemicals
import hiiragi283.ragium.common.integration.mek.RagiumMekItems
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import java.util.concurrent.CompletableFuture
import mekanism.api.chemical.ChemicalStack
import mekanism.api.datagen.recipe.builder.ItemStackChemicalToItemStackRecipeBuilder
import mekanism.api.datagen.recipe.builder.ItemStackToChemicalRecipeBuilder
import mekanism.api.datagen.recipe.builder.ItemStackToItemStackRecipeBuilder
import mekanism.api.recipes.ingredients.ItemStackIngredient
import mekanism.api.recipes.ingredients.creator.IChemicalStackIngredientCreator
import mekanism.api.recipes.ingredients.creator.IItemStackIngredientCreator
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

class RagiumMekRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider.Integration(packOutput, future, RagiumAPI.MOD_ID, HCIConstants.MEKANISM) {
    companion object {
        @JvmField
        val itemAccess: IItemStackIngredientCreator = IngredientCreatorAccess.item()

        @JvmField
        val chemicalAccess: IChemicalStackIngredientCreator = IngredientCreatorAccess.chemicalStack()

        // Material
        private const val RAGINITE = "raginite"

        // Prefix
        private const val ENRICHING = "enriching"
        private const val METALLURGIC_INFUSING = "metallurgic_infusing"
    }

    private val output: RecipeOutput by lazy { exporter.asOutput().withConditions(condition) }

    override fun buildRecipes() {
        raginite()
    }

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
        useItem(CommonParts.INGOT, RagiumMaterialKeys.RAGI_ALLOY) {
            ItemStackChemicalToItemStackRecipeBuilder.metallurgicInfusing(
                itemAccess.from(baseOrDust(VanillaMaterialKeys.COPPER)),
                chemicalAccess.fromHolder(RagiumChemicals.RAGINITE, 20),
                it.toStack(),
                false,
            ).build(output, id(METALLURGIC_INFUSING, "ragi_alloy"))
        }
        // Gold -> Advanced Ragi-Alloy
        useItem(CommonParts.INGOT, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY) {
            ItemStackChemicalToItemStackRecipeBuilder.metallurgicInfusing(
                itemAccess.from(baseOrDust(VanillaMaterialKeys.GOLD)),
                chemicalAccess.fromHolder(RagiumChemicals.RAGINITE, 40),
                it.toStack(),
                false,
            ).build(output, id(METALLURGIC_INFUSING, "advanced_ragi_alloy"))
        }
        // Diamond -> Ragi-Crystal
        useItem(CommonParts.GEM, RagiumMaterialKeys.RAGI_CRYSTAL) {
            ItemStackChemicalToItemStackRecipeBuilder.metallurgicInfusing(
                itemAccess.from(baseOrDust(VanillaMaterialKeys.DIAMOND)),
                chemicalAccess.fromHolder(RagiumChemicals.RAGINITE, 60),
                it.toStack(),
                false,
            ).build(output, id(METALLURGIC_INFUSING, "ragi_crystal"))
        }
        // Antimatter -> Ragi-Matter
    }

    override fun getName(): String = "Mekanism Recipes"

    //    Extension    //

    fun IItemStackIngredientCreator.from(prefix: HTTagPrefix, key: HTMaterialKey, amount: Int = 1): ItemStackIngredient = this.from(prefix.itemTagKey(key), amount)

    fun IItemStackIngredientCreator.from(tagKeys: Iterable<TagKey<Item>>, amount: Int = 1): ItemStackIngredient = this.from(amount, tagKeys.toList())
}
