package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.data.server.integration.HTAARecipeProvider
import hiiragi283.ragium.data.server.integration.HTDelightRecipeProvider
import hiiragi283.ragium.data.server.integration.HTIERecipeProvider
import hiiragi283.ragium.data.server.integration.HTMekanismRecipeProvider
import hiiragi283.ragium.data.server.recipe.*
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.conditions.ModLoadedCondition
import java.util.*
import java.util.concurrent.CompletableFuture

class RagiumRecipeProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) :
    RecipeProvider(output, registries) {
    fun interface Child {
        fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider)

        fun has(item: ItemLike): Criterion<InventoryChangeTrigger.TriggerInstance> = inventoryTrigger(ItemPredicate.Builder.item().of(item))

        fun has(prefix: HTTagPrefix, material: HTMaterialKey): Criterion<InventoryChangeTrigger.TriggerInstance> =
            has(prefix.createTag(material))

        fun has(tagKey: TagKey<Item>): Criterion<InventoryChangeTrigger.TriggerInstance> =
            inventoryTrigger(ItemPredicate.Builder.item().of(tagKey))

        private fun inventoryTrigger(builder: ItemPredicate.Builder): Criterion<InventoryChangeTrigger.TriggerInstance> =
            CriteriaTriggers.INVENTORY_CHANGED.createCriterion(
                InventoryChangeTrigger.TriggerInstance(
                    Optional.empty(),
                    InventoryChangeTrigger.TriggerInstance.Slots.ANY,
                    listOf(builder.build()),
                ),
            )
    }

    abstract class ModChild(val modId: String) : Child {
        fun id(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(modId, path)

        final override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
            /*val fixedOutput: RecipeOutput = object : RecipeOutput {
                override fun accept(
                    id: ResourceLocation,
                    recipe: Recipe<*>,
                    advancement: AdvancementHolder?,
                    vararg conditions: ICondition,
                ) {
                    val splitPath: List<String> = id.path.split("/", limit = 2)
                    if (splitPath.size < 2) {
                        LOGGER.warn("Given recipe: {} is not valid for registration", id)
                        return
                    }
                    val fixedId: ResourceLocation =
                        id.withPath { path: String -> splitPath[0] + "/$modId/" + splitPath[1] }
                    output.accept(fixedId, recipe, advancement, *conditions)
                }

                override fun advancement(): Advancement.Builder = output.advancement()
            }*/
            buildModRecipes(output.withConditions(ModLoadedCondition(modId)), holderLookup)
        }

        protected abstract fun buildModRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider)
    }

    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        HTAlternativeRecipeProvider.buildRecipes(output, holderLookup)
        HTBlockRecipeProvider.buildRecipes(output, holderLookup)
        HTChemicalRecipeProvider.buildRecipes(output, holderLookup)
        HTChemicalRecipeProviderNew.buildRecipes(output, holderLookup)
        HTFoodRecipeProvider.buildRecipes(output, holderLookup)
        HTCommonRecipeProvider.buildRecipes(output, holderLookup)
        HTMachineRecipeProvider.buildRecipes(output, holderLookup)
        HTMaterialRecipeProvider.buildRecipes(output, holderLookup)

        HTAARecipeProvider.buildRecipes(output, holderLookup)
        HTDelightRecipeProvider.buildRecipes(output, holderLookup)
        HTIERecipeProvider.buildRecipes(output, holderLookup)
        HTMekanismRecipeProvider.buildRecipes(output, holderLookup)
    }
}
