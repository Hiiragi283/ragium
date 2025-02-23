package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.conditions.ModLoadedCondition
import java.util.*

abstract class HTRecipeProvider {
    protected lateinit var lookup: HolderGetter<Item>
        private set

    fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        lookup = holderLookup.lookupOrThrow(Registries.ITEM)
        buildRecipeInternal(output, holderLookup)
    }

    protected abstract fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider)

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

    //    Modded    //

    abstract class Modded(val modId: String) : HTRecipeProvider() {
        fun id(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(modId, path)

        final override fun buildRecipeInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
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
}
