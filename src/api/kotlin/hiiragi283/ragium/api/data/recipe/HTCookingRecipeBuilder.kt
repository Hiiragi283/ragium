package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.advancements.Criterion
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.ItemLike
import java.util.function.IntUnaryOperator

class HTCookingRecipeBuilder private constructor(
    private val types: Set<Type>,
    private val cookingCategory: CookingBookCategory,
    private val input: Ingredient,
    private val output: Item,
    private val time: Int,
    private val exp: Float,
) : RecipeBuilder {
    companion object {
        @JvmStatic
        fun create(
            input: Ingredient,
            output: ItemLike,
            category: CookingBookCategory = CookingBookCategory.MISC,
            time: Int = 200,
            exp: Float = 0f,
            types: Collection<Type> = setOf(Type.SMELTING),
        ): HTCookingRecipeBuilder = HTCookingRecipeBuilder(
            types.toSet(),
            CookingBookCategory.MISC,
            input,
            output.asItem(),
            time,
            exp,
        )

        @JvmField
        val BLASTING_TYPES: Set<Type> = setOf(Type.SMELTING, Type.BLASTING)

        @JvmField
        val SMOKING_TYPES: Set<Type> = setOf(Type.SMELTING, Type.SMOKING)
    }

    //    RecipeBuilder    //

    private var groupName: String? = null

    @Deprecated("Advancements not supported")
    override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder = throw UnsupportedOperationException()

    override fun group(groupName: String?): HTCookingRecipeBuilder = apply {
        this.groupName = groupName
    }

    override fun getResult(): Item = output

    fun savePrefix(recipeOutput: RecipeOutput, prefix: String) {
        save(recipeOutput, RecipeBuilder.getDefaultRecipeId(result).withPrefix(prefix))
    }

    fun saveSuffix(recipeOutput: RecipeOutput, suffix: String) {
        save(recipeOutput, RecipeBuilder.getDefaultRecipeId(result).withSuffix(suffix))
    }

    override fun save(recipeOutput: RecipeOutput, id: ResourceLocation) {
        types.forEach { type: Type ->
            saveInternal(recipeOutput, RagiumAPI.wrapId(id), type)
        }
    }

    private fun saveInternal(recipeOutput: RecipeOutput, id: ResourceLocation, type: Type) {
        val fixedId: ResourceLocation = id.withPrefix(type.name.lowercase() + '/')
        val recipe: AbstractCookingRecipe = type.factory.create(
            groupName ?: "",
            cookingCategory,
            input,
            ItemStack(output),
            exp,
            type.timeModifier.applyAsInt(time),
        )
        recipeOutput.accept(
            fixedId,
            recipe,
            null,
        )
    }

    //    Type    //

    enum class Type(val factory: AbstractCookingRecipe.Factory<out AbstractCookingRecipe>, val timeModifier: IntUnaryOperator) {
        SMELTING(::SmeltingRecipe, IntUnaryOperator.identity()),
        BLASTING(::BlastingRecipe, { it / 2 }),
        SMOKING(::SmokingRecipe, { it / 2 }),
    }
}
