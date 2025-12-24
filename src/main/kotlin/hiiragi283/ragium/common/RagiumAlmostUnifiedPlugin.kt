package hiiragi283.ragium.common

import com.almostreliable.unified.api.plugin.AlmostUnifiedNeoPlugin
import com.almostreliable.unified.api.plugin.AlmostUnifiedPlugin
import com.almostreliable.unified.api.unification.bundled.GenericRecipeUnifier
import com.almostreliable.unified.api.unification.recipe.RecipeJson
import com.almostreliable.unified.api.unification.recipe.RecipeUnifierRegistry
import com.almostreliable.unified.api.unification.recipe.UnificationHelper
import hiiragi283.core.api.HTConst
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import net.minecraft.resources.ResourceLocation

@AlmostUnifiedNeoPlugin
class RagiumAlmostUnifiedPlugin : AlmostUnifiedPlugin {
    override fun getPluginId(): ResourceLocation = RagiumAPI.id("common")

    override fun registerRecipeUnifiers(registry: RecipeUnifierRegistry) {
        registry.registerForModId(RagiumAPI.MOD_ID) { helper: UnificationHelper, recipe: RecipeJson ->
            GenericRecipeUnifier.INSTANCE.unify(helper, recipe)

            helper.unifyInputs(recipe, RagiumConst.ITEM_INGREDIENT, RagiumConst.FLUID_INGREDIENT)
            helper.unifyOutputs(recipe, HTConst.ITEM_RESULT, HTConst.FLUID_RESULT, RagiumConst.EXTRA_RESULT)
        }
    }
}
