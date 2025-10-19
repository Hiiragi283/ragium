package hiiragi283.ragium.common.integration

import com.almostreliable.unified.api.constant.RecipeConstants
import com.almostreliable.unified.api.unification.recipe.RecipeJson
import com.almostreliable.unified.api.unification.recipe.RecipeUnifier
import com.almostreliable.unified.api.unification.recipe.UnificationHelper
import com.google.gson.JsonObject
import com.mojang.serialization.JsonOps
import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import hiiragi283.ragium.api.registry.HTKeyOrTagHelper
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

object RagiumRecipeUnifier : RecipeUnifier {
    override fun unify(helper: UnificationHelper, recipe: RecipeJson) {
        // Outputs
        val keys: Array<String> = arrayOf(RecipeConstants.RESULT, RecipeConstants.RESULTS, "item_result")
        for (key: String in keys) {
            var changed = false
            val result: JsonObject = recipe.getProperty(key) as? JsonObject ?: continue
            if (result.has(RecipeConstants.ID)) {
                HTKeyOrTagHelper.INSTANCE
                    .codec(Registries.ITEM)
                    .decode(JsonOps.INSTANCE, result.getAsJsonPrimitive(RecipeConstants.ID))
                    .onSuccess { entry: HTKeyOrTagEntry<Item> ->
                        changed = entry.map(
                            { _: ResourceKey<Item> -> helper.unifyOutputItem(result) },
                            { tagKey: TagKey<Item> -> helper.handleTagToItemReplacement(result, RecipeConstants.ID, tagKey) },
                        )
                    }
                if (changed) break
            }
        }
    }
}
