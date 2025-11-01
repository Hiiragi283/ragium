package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.RagiumPlatform
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
import net.minecraft.world.level.Level
import kotlin.jvm.optionals.getOrNull

class HTDeferredRecipeType<INPUT : RecipeInput, RECIPE : Recipe<INPUT>> :
    HTDeferredHolder<RecipeType<*>, RecipeType<RECIPE>>,
    HTRecipeType.Findable<INPUT, RECIPE>,
    HTHasTranslationKey {
    constructor(key: ResourceKey<RecipeType<*>>) : super(key)

    constructor(id: ResourceLocation) : super(Registries.RECIPE_TYPE, id)

    override val translationKey: String = id.toLanguageKey("recipe_type")

    override fun getText(): Component = Component.translatable(translationKey)

    override fun getRecipeFor(
        manager: RecipeManager,
        input: INPUT,
        level: Level,
        lastRecipe: ResourceLocation?,
    ): RecipeHolder<RECIPE>? = manager.getRecipeFor(get(), input, level, lastRecipe).getOrNull()
        ?: RagiumPlatform.INSTANCE.getMaterialRecipeManager().getRecipeFor(get(), input, level, lastRecipe)

    override fun getAllHolders(manager: RecipeManager): Sequence<RecipeHolder<out RECIPE>> = buildList {
        addAll(manager.getAllRecipesFor(get()))
        addAll(RagiumPlatform.INSTANCE.getMaterialRecipeManager().getAllRecipes(get()))
    }.asSequence()
}
