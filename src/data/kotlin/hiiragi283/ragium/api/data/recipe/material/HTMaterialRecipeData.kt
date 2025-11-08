package hiiragi283.ragium.api.data.recipe.material

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.ingredient.HTItemIngredientCreator
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTChancedItemResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.crafting.SizedIngredient

@ConsistentCopyVisibility
@JvmRecord
data class HTMaterialRecipeData private constructor(val inputs: List<InputEntry>, val outputs: List<OutputEntry>) {
    companion object {
        @JvmStatic
        inline fun create(builderAction: Builder.() -> Unit): HTMaterialRecipeData = Builder().apply(builderAction).build()
    }

    // Input
    inline fun <T> getInputs(factory: (InputEntry) -> T): List<T> = inputs.map(factory)

    fun getIngredients(): List<Pair<Ingredient, Int>> = getInputs { (either: Either<List<Item>, List<TagKey<Item>>>, count: Int) ->
        either.map(
            { items: List<Item> -> Ingredient.of(items.stream().map(::ItemStack)) },
            { tagKeys: List<TagKey<Item>> ->
                when (tagKeys.size) {
                    1 -> Ingredient.of(tagKeys[0])
                    else -> Ingredient.fromValues(tagKeys.map(Ingredient::TagValue).stream())
                }
            },
        ) to count
    }

    fun getSizedIngredients(): List<SizedIngredient> = getIngredients().map { (ingredient: Ingredient, count: Int) ->
        SizedIngredient(ingredient, count)
    }

    fun getItemIngredients(creator: HTItemIngredientCreator): List<HTItemIngredient> =
        getInputs { (either: Either<List<Item>, List<TagKey<Item>>>, count: Int) ->
            either.map(
                { items: List<Item> -> creator.fromItems(items, count) },
                { tagKeys: List<TagKey<Item>> ->
                    when (tagKeys.size) {
                        1 -> creator.fromTagKey(tagKeys[0], count)
                        else -> creator.fromTagKeys(tagKeys, count)
                    }
                },
            )
        }

    fun <T> getInput(index: Int, factory: (InputEntry) -> T): T = getInputs(factory)[index]

    fun getIngredient(index: Int): Ingredient = getIngredients()[index].first

    fun getSizedIngredient(index: Int): SizedIngredient = getSizedIngredients()[index]

    fun getItemIngredient(index: Int, creator: HTItemIngredientCreator): HTItemIngredient = getItemIngredients(creator)[index]

    inline fun forIngredients(action: (Int, Pair<Ingredient, Int>) -> Unit) {
        val ingredients: List<Pair<Ingredient, Int>> = getIngredients()
        for (i: Int in ingredients.indices) {
            action(i, ingredients[i])
        }
    }

    // Output
    inline fun <T> getOutputs(factory: (OutputEntry) -> T): List<T> = this.outputs.map(factory)

    fun getOutputStacks(): List<ItemStack> = getOutputs { (item: Item?, _, count: Int) ->
        when (item != null) {
            true -> ItemStack(item, count)
            false -> null
        }
    }.filterNotNull()

    fun getChancedResults(helper: HTResultHelper): List<HTChancedItemResult> =
        getOutputs { (item: Item?, tagKey: TagKey<Item>?, count: Int, chance: Float) ->
            val result: HTItemResult = when {
                tagKey != null -> helper.item(tagKey, count)
                item != null -> helper.item(item, count)
                else -> return@getOutputs null
            }
            HTChancedItemResult(result, chance)
        }.filterNotNull()

    fun getChancedResult(helper: HTResultHelper, index: Int): HTChancedItemResult = getChancedResults(helper)[index]

    fun getResult(helper: HTResultHelper, index: Int): HTItemResult = getChancedResult(helper, index).base

    inline fun forEachOutput(action: (Int, OutputEntry) -> Unit) {
        for (i: Int in this.outputs.indices) {
            action(i, this.outputs[i])
        }
    }

    @JvmRecord
    data class InputEntry(val entry: Either<List<Item>, List<TagKey<Item>>>, val count: Int)

    @JvmRecord
    data class OutputEntry(
        val item: Item?,
        val tagKey: TagKey<Item>?,
        val count: Int,
        val chance: Float,
    )

    //    Builder    //

    class Builder {
        val inputs: MutableList<InputEntry> = mutableListOf()
        val outputs: MutableList<OutputEntry> = mutableListOf()

        // Input
        fun addInput(vararg items: ItemLike, count: Int = 1): Builder = apply {
            this.inputs.add(InputEntry(Either.left(items.map(ItemLike::asItem)), count))
        }

        fun addInput(prefix: HTPrefixLike, material: HTMaterialLike, count: Int = 1): Builder =
            addInput(listOf(prefix.itemTagKey(material)), count)

        fun addInput(vararg tagKeys: TagKey<Item>, count: Int = 1): Builder = addInput(tagKeys.toList(), count)

        fun addInput(tagKeys: List<TagKey<Item>>, count: Int = 1): Builder = apply {
            this.inputs.add(InputEntry(Either.right(tagKeys), count))
        }

        fun fuelOrDust(material: HTMaterialLike, count: Int = 1): Builder = addInput(
            listOf(
                CommonMaterialPrefixes.DUST.itemTagKey(material),
                CommonMaterialPrefixes.FUEL.itemTagKey(material),
            ),
            count,
        )

        fun gemOrDust(material: HTMaterialLike, count: Int = 1): Builder = addInput(
            listOf(
                CommonMaterialPrefixes.DUST.itemTagKey(material),
                CommonMaterialPrefixes.GEM.itemTagKey(material),
            ),
            count,
        )

        fun ingotOrDust(material: HTMaterialLike, count: Int = 1): Builder = addInput(
            listOf(
                CommonMaterialPrefixes.DUST.itemTagKey(material),
                CommonMaterialPrefixes.INGOT.itemTagKey(material),
            ),
            count,
        )

        // Output
        fun addOutput(
            item: ItemLike,
            prefix: HTPrefixLike,
            material: HTMaterialLike,
            count: Int = 1,
            chance: Float = 1f,
        ): Builder = addOutput(item.asItem(), prefix.itemTagKey(material), count, chance)

        fun addOutput(
            prefix: HTPrefixLike,
            material: HTMaterialLike,
            count: Int = 1,
            chance: Float = 1f,
        ): Builder = addOutput(prefix.itemTagKey(material), count, chance)

        fun addOutput(tagKey: TagKey<Item>, count: Int = 1, chance: Float = 1f): Builder = addOutput(null, tagKey, count, chance)

        fun addOutput(
            item: ItemLike?,
            tagKey: TagKey<Item>?,
            count: Int = 1,
            chance: Float = 1f,
        ): Builder = apply {
            this.outputs.add(OutputEntry(item?.asItem(), tagKey, count, chance))
        }

        fun build(): HTMaterialRecipeData = HTMaterialRecipeData(inputs, outputs)
    }
}
