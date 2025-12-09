package hiiragi283.ragium.api.recipe.ingredient

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTDeferredHolder
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.common.crafting.IngredientType
import net.neoforged.neoforge.registries.NeoForgeRegistries

object RagiumIngredientTypes {
    @JvmField
    val POTION: HTDeferredHolder<IngredientType<*>, IngredientType<HTPotionIngredient>> = create("potion")

    @JvmStatic
    private fun <T : ICustomIngredient> create(path: String): HTDeferredHolder<IngredientType<*>, IngredientType<T>> =
        HTDeferredHolder(NeoForgeRegistries.Keys.INGREDIENT_TYPES, RagiumAPI.id(path))
}
