package hiiragi283.ragium.common.event

import hiiragi283.lib.data.recipe.HTRecipeExporter
import hiiragi283.lib.data.recipe.HTRecipeProviderContext
import net.minecraft.core.HolderLookup
import net.minecraft.tags.TagKey

data object RagiumRuntimeRecipeProvider : HTRecipeProviderContext() {
    override lateinit var exporter: HTRecipeExporter

    override lateinit var registries: HolderLookup.Provider

    private fun <T : Any> isPresent(tagKey: TagKey<T>): Boolean = registries.lookup(tagKey.registry()).flatMap { it.get(tagKey) }.isPresent

    @JvmStatic
    fun addRecipes(exporter: HTRecipeExporter, registries: HolderLookup.Provider) {
        this.exporter = exporter
        this.registries = registries
    }
}
