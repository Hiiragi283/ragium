@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe

import hiiragi283.lib.HTConstants
import it.unimi.dsi.fastutil.chars.Char2ObjectLinkedOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.ShapedRecipe
import net.minecraft.world.item.crafting.ShapedRecipePattern
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * 定形レシピ向けの[HTRecipeBuilder]の実装クラスです。
 *
 * 参照 : [Minecraft - ShapedRecipeBuilder][net.minecraft.data.recipes.ShapedRecipeBuilder]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTShapedRecipeBuilder : HTCraftingRecipeBuilder<ShapedRecipe>(HTConstants.SHAPED) {
    companion object {
        @JvmStatic
        inline fun create(builderAction: HTShapedRecipeBuilder.() -> Unit): HTShapedRecipeBuilder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTShapedRecipeBuilder().apply(builderAction)
        }
    }

    @PublishedApi internal val patterns: MutableList<String> = ObjectArrayList()

    @PublishedApi internal val keys: MutableMap<Char, Ingredient> = Char2ObjectLinkedOpenHashMap()

    operator fun String.unaryPlus() {
        patterns.firstOrNull()?.let {
            if (it.length != this.length) error("Pattern must be the same width on every line")
        }
        patterns += this
    }

    fun pattern(patterns: Iterable<String>) {
        patterns.forEach { +it }
    }

    /**
     * 2x2のパターンを指定します。
     */
    fun storage4() {
        +"AA"
        +"AA"
    }

    /**
     * 3x3のパターンを指定します。
     */
    fun storage9() {
        +"AAA"
        +"AAA"
        +"AAA"
    }

    /**
     * 中央が空の3x3のパターンを指定します。
     */
    fun hollow() {
        +"AAA"
        +"A A"
        +"AAA"
    }

    /**
     * 中央の材料を，一種類の4つの材料で取り囲むパターンを指定します。
     */
    fun hollow4() {
        +" A "
        +"ABA"
        +" A "
    }

    /**
     * 中央の材料を，一種類の8つの材料で取り囲むパターンを指定します。
     */
    fun hollow8() {
        +"AAA"
        +"ABA"
        +"AAA"
    }

    /**
     * 中央の材料を，二種類の2つずつの材料で取り囲むパターンを指定します。
     */
    fun cross4() {
        +" A "
        +"BCB"
        +" A "
    }

    /**
     * 中央の材料を，二種類の4つずつの材料で取り囲むパターンを指定します。
     */
    fun cross8() {
        +"ABA"
        +"BCB"
        +"ABA"
    }

    fun crossLayered() {
        +"ABA"
        +"CDC"
        +"ABA"
    }

    /**
     * 二種類の材料を交互に配置する2x2のパターンを指定します。
     */
    fun mosaic4() {
        +"AB"
        +"BA"
    }

    /**
     * 二種類の材料を交互に配置する3x3のパターンを指定します。
     */
    fun mosaic9() {
        +"ABA"
        +"BAB"
        +"ABA"
    }

    infix fun Char.define(ingredient: Ingredient) {
        check(this !in keys) { "Symbol $this is already defined" }
        check(this != ' ') { "Symbol $this (whitespace) is reserved and cannot be defined" }
        keys[this] = ingredient
    }

    inline fun define(key: Char, builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        key define IngredientBuilder().apply(builderAction).build()
    }

    override fun createRecipe(): ShapedRecipe = ShapedRecipe(
        commonInfo(true),
        bookInfo(),
        ShapedRecipePattern.of(keys, patterns),
        result
    )
}
