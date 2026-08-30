package hiiragi283.ragium.internal.data

import com.mojang.serialization.JsonOps
import hiiragi283.lib.data.pack.HTDynamicDataRegister
import hiiragi283.lib.data.recipe.HTRecipeExporter
import hiiragi283.lib.data.recipe.HTRecipeProviderContext
import hiiragi283.lib.recipe.RecipeKey
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.Recipe

data object RagiumDynamicRecipeProvider : HTRecipeProviderContext() {
    override val exporter: HTRecipeExporter = HTRecipeExporter { id: RecipeKey, recipe: Recipe<*>, _ ->
        HTDynamicDataRegister.addToData(id, Recipe.CODEC, recipe, registries.createSerializationContext(JsonOps.INSTANCE))
    }

    override lateinit var registries: HolderLookup.Provider
        private set

    fun initialize(registries: HolderLookup.Provider) {
        this.registries = registries
    }
}
