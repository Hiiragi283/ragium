package hiiragi283.lib.recipe.lookup

import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.util.Either
import hiiragi283.lib.util.unwrap
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.common.util.ValueIOSerializable
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

/**
 * [RecipeType]に基づいて，取得したレシピをキャッシュするクラスです。
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE 提供するレシピのクラス
 * @param recipeType レシピの種類
 * @author Hiiragi Tsubasa
 * @since 26.1.6
 */
class HTVanillaRecipeCache<INPUT : RecipeInput, RECIPE : Recipe<INPUT>>(val recipeType: RecipeType<RECIPE>) :
    ValueIOSerializable {
    private var lastRecipe: Either<RecipeKey, RecipeHolder<RECIPE>>? = null

    /**
     * [RecipeHolder]を取得します。
     * @param input レシピの入力
     * @param level レシピ取得時のレベル
     * @return [input]に一致する最初のレシピ
     */
    fun findFirstHolder(input: INPUT, level: ServerLevel): RecipeHolder<RECIPE>? {
        if (input.isEmpty) return null
        val recipeManager: RecipeManager = level.recipeAccess()
        val foundRecipe: Optional<RecipeHolder<RECIPE>> = lastRecipe?.fold(
            { key: RecipeKey -> recipeManager.getRecipeFor(recipeType, input, level, key) },
            { holder: RecipeHolder<RECIPE> -> recipeManager.getRecipeFor(recipeType, input, level, holder) }
        ) ?: recipeManager.getRecipeFor(recipeType, input, level)
        foundRecipe.ifPresentOrElse({ lastRecipe = Either.Right(it) }, { lastRecipe = null })
        return foundRecipe.getOrNull()
    }

    override fun serialize(output: ValueOutput) {
        output.storeNullable(HTConstants.ID, Recipe.KEY_CODEC, getRecipeKey())
    }

    override fun deserialize(input: ValueInput) {
        input.read(HTConstants.ID, Recipe.KEY_CODEC).ifPresent { lastRecipe = Either.Left(it) }
    }

    private fun getRecipeKey(): RecipeKey? = lastRecipe?.map(RecipeHolder<*>::id)?.unwrap()

    override fun toString(): String = "HTVanillaRecipeCache(recipeType=$recipeType, lastRecipe=${getRecipeKey()})"
}
