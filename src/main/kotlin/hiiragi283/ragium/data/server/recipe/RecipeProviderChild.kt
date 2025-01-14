package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import java.util.*

fun interface RecipeProviderChild {
    fun buildRecipes(output: RecipeOutput)

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
