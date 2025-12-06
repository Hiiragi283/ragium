package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.recipe.HTRecipeFinder
import hiiragi283.ragium.api.recipe.HTRecipeType
import hiiragi283.ragium.api.registry.HTDeferredHolder
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

class HTDeferredRecipeType<INPUT : RecipeInput, RECIPE : Recipe<INPUT>> :
    HTDeferredHolder<RecipeType<*>, RecipeType<RECIPE>>,
    HTRecipeFinder<INPUT, RECIPE>,
    HTRecipeType<INPUT, RECIPE>,
    HTHasTranslationKey {
    constructor(key: ResourceKey<RecipeType<*>>) : super(key)

    constructor(id: ResourceLocation) : super(Registries.RECIPE_TYPE, id)

    override val translationKey: String = id.toLanguageKey("recipe_type")

    override fun getText(): Component = translatableText(translationKey)

    override fun getRecipeFor(
        manager: RecipeManager,
        input: INPUT,
        level: Level,
        lastRecipe: RecipeHolder<RECIPE>?,
    ): RecipeHolder<RECIPE>? {
        // 入力が空の場合は即座に抜ける
        if (input.isEmpty) return null
        // キャッシュから判定を行う
        if (lastRecipe != null && matches(lastRecipe.value, input, level)) {
            return lastRecipe
        }
        // 次にRecipeManagerから行う
        for (holder: RecipeHolder<RECIPE> in manager.getAllRecipesFor(get())) {
            if (matches(holder.value, input, level)) {
                return holder
            }
        }
        // 最後にHTMaterialRecipeManagerから行う
        return RagiumPlatform.INSTANCE.getMaterialRecipeManager().getRecipeFor(get(), input, level, lastRecipe)
    }

    private fun matches(recipe: RECIPE, input: INPUT, level: Level): Boolean = recipe.matches(input, level) && !recipe.isIncomplete

    override fun getAllHolders(manager: RecipeManager): Sequence<RecipeHolder<out RECIPE>> = buildList {
        addAll(manager.getAllRecipesFor(get()).filterNot { holder: RecipeHolder<RECIPE> -> holder.value.isIncomplete })
        addAll(RagiumPlatform.INSTANCE.getMaterialRecipeManager().getAllRecipes(manager, get()))
    }.asSequence()
}
