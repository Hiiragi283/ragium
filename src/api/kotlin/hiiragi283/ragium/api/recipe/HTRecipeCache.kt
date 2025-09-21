package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.codec.BiCodecs
import hiiragi283.ragium.api.storage.value.HTValueInput
import hiiragi283.ragium.api.storage.value.HTValueOutput
import hiiragi283.ragium.api.storage.value.HTValueSerializable
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.Level

/**
 * レシピを保持するキャッシュのクラス
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE レシピのクラス
 */
interface HTRecipeCache<INPUT : RecipeInput, RECIPE : Recipe<INPUT>> {
    /**
     * 指定された[input], [level]から最初に一致するレシピを返します。
     * @param input レシピの入力
     * @param level レシピを取得するレベル
     * @return 見つからなかった場合は`null`
     */
    fun getFirstRecipe(input: INPUT, level: Level): RECIPE? = getFirstHolder(input, level)?.recipe

    /**
     * 指定された[input], [level]から最初に一致する[HTRecipeHolder]を返します。
     * @param input レシピの入力
     * @param level レシピを取得するレベル
     * @return 見つからなかった場合は`null`
     */
    fun getFirstHolder(input: INPUT, level: Level): HTRecipeHolder<RECIPE>?

    abstract class Serializable<INPUT : RecipeInput, RECIPE : Recipe<INPUT>> :
        HTRecipeCache<INPUT, RECIPE>,
        HTValueSerializable {
        protected var lastRecipe: ResourceLocation? = null

        protected fun <R : Recipe<*>> updateCache(holder: HTRecipeHolder<R>?): HTRecipeHolder<R>? = holder.apply {
            lastRecipe = this?.id
        }

        override fun deserialize(input: HTValueInput) {
            lastRecipe = input.read(RagiumConst.LAST_RECIPE, BiCodecs.RL)
        }

        override fun serialize(output: HTValueOutput) {
            output.store(RagiumConst.LAST_RECIPE, BiCodecs.RL, lastRecipe)
        }
    }
}
