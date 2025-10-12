package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.recipe.manager.HTRecipeType
import hiiragi283.ragium.api.registry.HTDeferredHolder
import hiiragi283.ragium.api.text.HTHasTranslationKey
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType

class HTDeferredRecipeType<INPUT : RecipeInput, RECIPE : Recipe<INPUT>> :
    HTDeferredHolder<RecipeType<*>, RecipeType<RECIPE>>,
    HTRecipeType<INPUT, RECIPE>,
    HTHasTranslationKey {
    constructor(key: ResourceKey<RecipeType<*>>) : super(key)

    constructor(id: ResourceLocation) : super(Registries.RECIPE_TYPE, id)

    override val translationKey: String = id.toLanguageKey("recipe_type")

    override fun getText(): Component = Component.translatable(translationKey)

    override fun getAllHolders(manager: RecipeManager): Sequence<RecipeHolder<RECIPE>> = manager.getAllRecipesFor(get()).asSequence()
}
