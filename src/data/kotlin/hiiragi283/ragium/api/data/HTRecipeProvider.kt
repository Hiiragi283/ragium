package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.IntegrationMods
import hiiragi283.ragium.api.data.recipe.HTMachineRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.extension.asItemHolder
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.extension.itemLookup
import hiiragi283.ragium.api.extension.toStack
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.init.RagiumRecipeFactories
import hiiragi283.ragium.common.recipe.HTCentrifugingRecipe
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTExtractingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.StonecutterRecipe
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.conditions.ModLoadedCondition
import java.util.*

abstract class HTRecipeProvider {
    protected lateinit var lookup: HolderGetter<Item>
        private set

    fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        lookup = holderLookup.itemLookup()
        buildRecipeInternal(output, holderLookup)
    }

    protected abstract fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider)

    //    Extensions    //

    fun addSlab(output: RecipeOutput, base: ItemLike, slab: ItemLike) {
        // Shaped
        HTShapedRecipeBuilder(slab, 6)
            .pattern("AAA")
            .define('A', base)
            .save(output)
        // Stonecutter
        output.save(
            slab.asItemHolder().idOrThrow.withPrefix("stonecutting/"),
            StonecutterRecipe(
                "",
                Ingredient.of(base),
                slab.toStack(2),
            ),
        )
    }

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

    fun RecipeOutput.save(recipeId: ResourceLocation, recipe: Recipe<*>) {
        accept(recipeId, recipe, null)
    }

    protected fun centrifuging(): HTMachineRecipeBuilder<HTCentrifugingRecipe> = HTMachineRecipeBuilder(RagiumRecipeFactories::centrifuging)

    protected fun crush(): HTMachineRecipeBuilder<HTCrushingRecipe> = HTMachineRecipeBuilder(RagiumRecipeFactories::crushing)

    protected fun extract(): HTMachineRecipeBuilder<HTExtractingRecipe> = HTMachineRecipeBuilder(RagiumRecipeFactories::extracting)

    protected fun refining(): HTMachineRecipeBuilder<HTRefiningRecipe> = HTMachineRecipeBuilder(RagiumRecipeFactories::refining)

    //    Modded    //

    abstract class Modded(val mod: IntegrationMods) : HTRecipeProvider() {
        fun id(path: String): ResourceLocation = mod.id(path)

        final override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
            buildModRecipes(output.withConditions(ModLoadedCondition(mod.modId)), holderLookup)
        }

        protected abstract fun buildModRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider)
    }
}
