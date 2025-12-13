package hiiragi283.ragium.common

import com.almostreliable.unified.api.plugin.AlmostUnifiedNeoPlugin
import com.almostreliable.unified.api.plugin.AlmostUnifiedPlugin
import com.almostreliable.unified.api.unification.recipe.RecipeJson
import com.almostreliable.unified.api.unification.recipe.RecipeUnifier
import com.almostreliable.unified.api.unification.recipe.RecipeUnifierRegistry
import com.almostreliable.unified.api.unification.recipe.UnificationHelper
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

@AlmostUnifiedNeoPlugin
class RagiumAlmostUnifiedPlugin : AlmostUnifiedPlugin {
    override fun getPluginId(): ResourceLocation = RagiumAPI.id("common")

    override fun registerRecipeUnifiers(registry: RecipeUnifierRegistry) {
        registry.registerForModId(RagiumAPI.MOD_ID, RagiumRecipeUnifier)
    }

    private object RagiumRecipeUnifier : RecipeUnifier {
        @JvmStatic
        private val TAG_CODEC: Codec<TagKey<Item>> = TagKey.hashedCodec(Registries.ITEM)

        override fun unify(helper: UnificationHelper, recipe: RecipeJson) {
            // Outputs
            val keys: Array<String> = arrayOf(RagiumConst.RESULT, RagiumConst.RESULTS, RagiumConst.ITEM_RESULT, "extra")
            for (key: String in keys) {
                val json: JsonElement = recipe.getProperty(key) ?: continue
                RagiumAPI.LOGGER.debug("Current target key: {} for {}", key, recipe.id)
                if (json is JsonObject) {
                    unifyOutput(helper, json)
                } else if (json is JsonArray) {
                    for (jsonIn: JsonObject in json.filterIsInstance<JsonObject>()) {
                        unifyOutput(helper, jsonIn)
                    }
                }
            }
        }

        private fun unifyOutput(helper: UnificationHelper, json: JsonObject) {
            if (json.has(RagiumConst.ID)) {
                helper.unifyOutputItem(json)
                return
            }
            val primitive: JsonElement = json.get(RagiumConst.TAG) ?: return
            TAG_CODEC
                .parse(JsonOps.INSTANCE, primitive)
                .ifSuccess { tagKey: TagKey<Item> ->
                    helper.handleTagToItemReplacement(json, RagiumConst.ID, tagKey)
                }
        }
    }
}
