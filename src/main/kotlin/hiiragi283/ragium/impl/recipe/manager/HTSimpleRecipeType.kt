package hiiragi283.ragium.impl.recipe.manager

import hiiragi283.ragium.api.recipe.manager.HTRecipeType
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import kotlin.jvm.optionals.getOrNull

@JvmInline
internal value class HTSimpleRecipeType<INPUT : RecipeInput, RECIPE : Recipe<INPUT>>(val recipeType: RecipeType<RECIPE>) :
    HTRecipeType.Findable<INPUT, RECIPE> {
    override fun getRecipeFor(
        manager: RecipeManager,
        input: INPUT,
        level: Level,
        lastRecipe: ResourceLocation?,
    ): RecipeHolder<RECIPE>? = manager.getRecipeFor(recipeType, input, level, lastRecipe).getOrNull()

    override fun getAllHolders(manager: RecipeManager): Sequence<RecipeHolder<out RECIPE>> =
        manager.getAllRecipesFor(recipeType).asSequence()

    override fun getText(): Component = Component.literal(recipeType.toString())
}
