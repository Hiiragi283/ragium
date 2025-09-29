package hiiragi283.ragium.integration.almost

import com.almostreliable.unified.api.plugin.AlmostUnifiedNeoPlugin
import com.almostreliable.unified.api.plugin.AlmostUnifiedPlugin
import com.almostreliable.unified.api.unification.recipe.RecipeUnifierRegistry
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.resources.ResourceLocation

@AlmostUnifiedNeoPlugin
class RagiumAlmostUnifiedPlugin : AlmostUnifiedPlugin {
    override fun getPluginId(): ResourceLocation = RagiumAPI.id("common")

    override fun registerRecipeUnifiers(registry: RecipeUnifierRegistry) {
        registry.registerForModId(RagiumAPI.MOD_ID, RagiumRecipeUnifier)
    }
}
