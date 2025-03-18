package hiiragi283.ragium.api.data.recipe

import net.minecraft.advancements.Criterion
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Recipe

interface HTRecipeBuilder<T : Recipe<*>> : RecipeBuilder {
    @Deprecated("Advancements not supported")
    override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder = throw UnsupportedOperationException()

    override fun group(groupName: String?): RecipeBuilder = throw UnsupportedOperationException()

    override fun getResult(): Item = throw UnsupportedOperationException()

    fun getPrimalId(): ResourceLocation

    fun savePrefixed(recipeOutput: RecipeOutput, prefix: String) {
        save(recipeOutput, getPrimalId().withPrefix(prefix))
    }

    fun saveSuffixed(recipeOutput: RecipeOutput, suffix: String) {
        save(recipeOutput, getPrimalId().withSuffix(suffix))
    }

    override fun save(recipeOutput: RecipeOutput) {
        save(recipeOutput, getPrimalId())
    }

    fun getIdPrefix(): String

    fun createRecipe(): T

    override fun save(recipeOutput: RecipeOutput, id: ResourceLocation) {
        recipeOutput.accept(
            id.withPrefix("${getIdPrefix()}/"),
            createRecipe(),
            null,
        )
    }
}
