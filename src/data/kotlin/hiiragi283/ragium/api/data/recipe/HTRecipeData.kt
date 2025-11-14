package hiiragi283.ragium.api.data.recipe

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.data.recipe.ingredient.HTItemIngredientCreator
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.recipe.chance.HTItemResultWithChance
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.registry.toHolderLike
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.crafting.SizedIngredient
import java.util.function.UnaryOperator

@ConsistentCopyVisibility
@JvmRecord
data class HTRecipeData private constructor(
    val inputs: List<InputEntry>,
    val outputs: List<OutputEntry>,
    val operator: UnaryOperator<String>,
) : HTHolderLike {
    companion object {
        @JvmStatic
        inline fun create(builderAction: Builder.() -> Unit): HTRecipeData = Builder().apply(builderAction).build()
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

    inline fun <T> getOutputs(onTagKey: (TagKey<Item>, Int, Float) -> T, onItem: (Item, Int, Float) -> T): List<T> =
        this.outputs.mapNotNull { (item: Item?, tagKey: TagKey<Item>?, count: Int, chance: Float) ->
            when {
                tagKey != null -> onTagKey(tagKey, count, chance)
                item != null -> onItem(item, count, chance)
                else -> null
            }
        }

    fun getOutputStacks(): List<ItemStack> = getOutputs { (item: Item?, _, count: Int) ->
        when (item != null) {
            true -> ItemStack(item, count)
            false -> null
        }
    }.filterNotNull()

    fun getChancedResults(helper: HTResultHelper): List<HTItemResultWithChance> =
        getOutputs { (item: Item?, tagKey: TagKey<Item>?, count: Int, chance: Float) ->
            val result: HTItemResult = when {
                tagKey != null -> helper.item(tagKey, count)
                item != null -> helper.item(item, count)
                else -> return@getOutputs null
            }
            HTItemResultWithChance(result, chance)
        }.filterNotNull()

    fun getChancedResult(helper: HTResultHelper, index: Int): HTItemResultWithChance = getChancedResults(helper)[index]

    fun getResult(helper: HTResultHelper, index: Int): HTItemResult = getChancedResult(helper, index).base

    inline fun forEachOutput(action: (Int, OutputEntry) -> Unit) {
        for (i: Int in this.outputs.indices) {
            action(i, this.outputs[i])
        }
    }

    override fun getId(): ResourceLocation {
        for ((item: Item?, tagKey: TagKey<Item>?) in outputs) {
            when {
                tagKey != null -> return tagKey.location
                item != null -> return item.toHolderLike().getId()
            }
        }
        error("Cannot create id from empty or invalid outputs")
    }

    fun getModifiedId(): ResourceLocation = getId().withPath(operator)

    @JvmRecord
    data class InputEntry(val entry: Either<List<Item>, List<TagKey<Item>>>, val count: Int)

    @JvmRecord
    data class OutputEntry(
        val item: Item?,
        val tagKey: TagKey<Item>?,
        val count: Int,
        val chance: Float,
    ) {
        fun toImmutable(): ImmutableItemStack = ImmutableItemStack.of(checkNotNull(item), count)
    }

    //    Builder    //

    class Builder {
        private val inputs: MutableList<InputEntry> = mutableListOf()
        private val outputs: MutableList<OutputEntry> = mutableListOf()
        private var operator: UnaryOperator<String> = UnaryOperator.identity()

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

        // Operator
        fun setPrefix(prefix: String): Builder = setModifier { "$prefix$it" }

        fun setSuffix(suffix: String): Builder = setModifier { "$it$suffix" }

        fun setModifier(operator: UnaryOperator<String>): Builder = apply {
            this.operator = operator
        }

        fun build(): HTRecipeData = HTRecipeData(inputs, outputs, operator)
    }
}
