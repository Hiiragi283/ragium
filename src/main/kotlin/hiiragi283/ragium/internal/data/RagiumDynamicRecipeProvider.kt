package hiiragi283.ragium.internal.data

import hiiragi283.lib.data.pack.HTDynamicDataRegister
import hiiragi283.lib.data.recipe.HTRecipeProviderContext

data object RagiumDynamicRecipeProvider : HTRecipeProviderContext.Delegated() {
    fun initialize() {
        this.delegate = HTDynamicDataRegister
    }
}
