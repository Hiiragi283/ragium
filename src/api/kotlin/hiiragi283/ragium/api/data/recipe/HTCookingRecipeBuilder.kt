package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.asItemHolder
import hiiragi283.ragium.api.extension.idOrThrow
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
) : HTRecipeBuilder<AbstractCookingRecipe> {
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
            category,
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

    override fun getPrimalId(): ResourceLocation = output.asItemHolder().idOrThrow

    private var groupName: String? = null

    override fun group(groupName: String?): HTCookingRecipeBuilder = apply {
        this.groupName = groupName
    }

    override fun getIdPrefix(): String = throw UnsupportedOperationException()

    override fun createRecipe(): AbstractCookingRecipe = throw UnsupportedOperationException()

    override fun save(recipeOutput: RecipeOutput, id: ResourceLocation) {
        for (type: Type in types) {
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
