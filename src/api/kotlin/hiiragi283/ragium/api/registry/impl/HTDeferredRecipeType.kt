package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.recipe.HTRecipeFinder
import hiiragi283.ragium.api.registry.HTDeferredHolder
import hiiragi283.ragium.api.text.HTHasText
import hiiragi283.ragium.api.text.HTHasTranslationKey
import hiiragi283.ragium.api.text.translatableText
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import kotlin.jvm.optionals.getOrNull

class HTDeferredRecipeType<INPUT : RecipeInput, RECIPE : Recipe<INPUT>> :
    HTDeferredHolder<RecipeType<*>, RecipeType<RECIPE>>,
    HTRecipeFinder<INPUT, RECIPE>,
    HTHasTranslationKey,
    HTHasText {
    constructor(key: ResourceKey<RecipeType<*>>) : super(key)

    constructor(id: ResourceLocation) : super(Registries.RECIPE_TYPE, id)

    override val translationKey: String = id.toLanguageKey("recipe_type")

    override fun getText(): Component = translatableText(translationKey)

    override fun getRecipeFor(
        manager: RecipeManager,
        input: INPUT,
        level: Level,
        lastRecipe: Pair<ResourceLocation, RECIPE>?,
    ): Pair<ResourceLocation, RECIPE>? {
        val lastHolder: RecipeHolder<RECIPE>? = lastRecipe
            ?.let { (id: ResourceLocation, recipe: RECIPE) -> RecipeHolder(id, recipe) }
        return manager
            .getRecipeFor(get(), input, level, lastHolder)
            .map { holder: RecipeHolder<RECIPE> -> holder.id to holder.value }
            .getOrNull()
    }
}
