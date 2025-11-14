package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.recipe.manager.HTRecipeType
import hiiragi283.ragium.api.text.HTHasText
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import kotlin.jvm.optionals.getOrNull

object VanillaRecipeTypes {
    @JvmField
    val SMELTING: HTRecipeType.Findable<SingleRecipeInput, HTVanillaCookingRecipe> =
        VanillaTypeWrapper(RecipeType.SMELTING, ::HTVanillaCookingRecipe, Blocks.FURNACE::getName)

    @JvmField
    val BLASTING: HTRecipeType.Findable<SingleRecipeInput, HTVanillaCookingRecipe> =
        VanillaTypeWrapper(RecipeType.BLASTING, ::HTVanillaCookingRecipe, Blocks.BLAST_FURNACE::getName)

    @JvmField
    val SMOKING: HTRecipeType.Findable<SingleRecipeInput, HTVanillaCookingRecipe> =
        VanillaTypeWrapper(RecipeType.SMOKING, ::HTVanillaCookingRecipe, Blocks.SMOKER::getName)

    //    Recipe Type    //

    private class VanillaTypeWrapper<INPUT : RecipeInput, BASE : Recipe<INPUT>, RECIPE : Recipe<INPUT>>(
        private val baseType: RecipeType<BASE>,
        private val factory: (BASE) -> RECIPE,
        private val text: HTHasText,
    ) : HTRecipeType.Findable<INPUT, RECIPE> {
        override fun getRecipeFor(
            manager: RecipeManager,
            input: INPUT,
            level: Level,
            lastRecipe: ResourceLocation?,
        ): RecipeHolder<RECIPE>? = manager
            .getRecipeFor(baseType, input, level, lastRecipe)
            .map(::mapHolder)
            .getOrNull()

        override fun getAllHolders(manager: RecipeManager): Sequence<RecipeHolder<out RECIPE>> = manager
            .getAllRecipesFor(baseType)
            .asSequence()
            .map(::mapHolder)

        override fun getText(): Component = this.text.getText()

        private fun mapHolder(holder: RecipeHolder<BASE>): RecipeHolder<RECIPE> = RecipeHolder(holder.id, factory(holder.value))
    }
}
