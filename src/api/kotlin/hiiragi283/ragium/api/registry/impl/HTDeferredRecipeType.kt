package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.recipe.manager.HTRecipeAccess
import hiiragi283.ragium.api.recipe.manager.HTRecipeGetter
import hiiragi283.ragium.api.recipe.manager.HTRecipeHolder
import hiiragi283.ragium.api.registry.HTDeferredHolder
import hiiragi283.ragium.api.text.HTHasTranslationKey
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType

class HTDeferredRecipeType<INPUT : RecipeInput, RECIPE : Recipe<INPUT>> :
    HTDeferredHolder<RecipeType<*>, RecipeType<RECIPE>>,
    HTRecipeGetter<INPUT, RECIPE>,
    HTHasTranslationKey {
    constructor(key: ResourceKey<RecipeType<*>>) : super(key)

    constructor(id: ResourceLocation) : super(Registries.RECIPE_TYPE, id)

    override val translationKey: String = id.toLanguageKey("recipe_type")

    override fun getAllHolders(access: HTRecipeAccess): Sequence<HTRecipeHolder<RECIPE>> = access.getAllRecipesFor(get())
}
