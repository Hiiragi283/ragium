package hiiragi283.ragium.api.data.recipe

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.data.recipe.ingredient.HTFluidIngredientCreator
import hiiragi283.ragium.api.data.recipe.ingredient.HTItemIngredientCreator
import hiiragi283.ragium.api.function.andThen
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.registry.toHolderLike
import hiiragi283.ragium.api.util.Ior
import hiiragi283.ragium.api.util.toIor
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.CompoundFluidIngredient
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.function.UnaryOperator

@ConsistentCopyVisibility
@JvmRecord
data class HTRecipeData private constructor(
    val itemInputs: List<InputEntry<Item>>,
    val fluidInputs: List<InputEntry<Fluid>>,
    val catalyst: Item?,
    val itemOutputs: List<OutputEntry<Item>>,
    val fluidOutputs: List<OutputEntry<Fluid>>,
    val operator: UnaryOperator<String>,
) : HTHolderLike {
    companion object {
        @JvmStatic
        inline fun create(builderAction: Builder.() -> Unit): HTRecipeData = Builder().apply(builderAction).build()
    }

    override fun getId(): ResourceLocation {
        // from items
        for ((entry: Ior<Item, TagKey<Item>>) in itemOutputs) {
            return entry.map(
                Item::toHolderLike.andThen(HTHolderLike::getId),
                TagKey<Item>::location,
            )
        }
        // from fluids
        for ((entry: Ior<Fluid, TagKey<Fluid>>) in fluidOutputs) {
            return entry.map(
                Fluid::toHolderLike.andThen(HTHolderLike::getId),
                TagKey<Fluid>::location,
            )
        }
        error("Cannot create id from empty or invalid outputs")
    }

    fun getModifiedId(): ResourceLocation = getId().withPath(operator)

    fun swap(): HTRecipeData = HTRecipeData(
        itemOutputs.map(OutputEntry<Item>::toInput),
        fluidOutputs.map(OutputEntry<Fluid>::toInput),
        catalyst,
        itemInputs.map(InputEntry<Item>::toOutput),
        fluidInputs.map(InputEntry<Fluid>::toOutput),
        operator,
    )

    // Inputs
    private fun toIngredient(entry: Either<List<Item>, List<TagKey<Item>>>): Ingredient = entry.map(
        { items: List<Item> -> Ingredient.of(items.stream().map(::ItemStack)) },
        { tagKeys: List<TagKey<Item>> ->
            when (tagKeys.size) {
                1 -> Ingredient.of(tagKeys[0])
                else -> Ingredient.fromValues(tagKeys.map(Ingredient::TagValue).stream())
            }
        },
    )

    fun getIngredients(): List<Ingredient> = itemInputs.map { (entry: Either<List<Item>, List<TagKey<Item>>>) -> toIngredient(entry) }

    fun getSizedItemIngredients(): List<SizedIngredient> = itemInputs
        .map { (entry: Either<List<Item>, List<TagKey<Item>>>, count: Int) -> toIngredient(entry) to count }
        .map { (ingredient: Ingredient, count: Int) -> SizedIngredient(ingredient, count) }

    fun getSizedFluidIngredients(): List<SizedFluidIngredient> = fluidInputs
        .map { (entry: Either<List<Fluid>, List<TagKey<Fluid>>>, count: Int) ->
            entry.map(
                { fluids: List<Fluid> -> CompoundFluidIngredient(fluids.map(FluidIngredient::single)) },
                { tagKeys: List<TagKey<Fluid>> ->
                    when (tagKeys.size) {
                        1 -> FluidIngredient.tag(tagKeys[0])
                        else -> CompoundFluidIngredient(tagKeys.map(FluidIngredient::tag))
                    }
                },
            ) to count
        }.map { (ingredient: FluidIngredient, count: Int) -> SizedFluidIngredient(ingredient, count) }

    fun getItemIngredients(creator: HTItemIngredientCreator): List<HTItemIngredient> =
        itemInputs.map { (entry: Either<List<Item>, List<TagKey<Item>>>, amount: Int) ->
            entry.map(
                { items: List<Item> -> creator.fromItems(items, amount) },
                { tagKeys: List<TagKey<Item>> ->
                    when (tagKeys.size) {
                        1 -> creator.fromTagKey(tagKeys[0], amount)
                        else -> creator.fromTagKeys(tagKeys, amount)
                    }
                },
            )
        }

    fun getFluidIngredients(creator: HTFluidIngredientCreator): List<HTFluidIngredient> =
        fluidInputs.map { (entry: Either<List<Fluid>, List<TagKey<Fluid>>>, amount: Int) ->
            entry.map(
                { fluids: List<Fluid> -> creator.from(fluids, amount) },
                { tagKeys: List<TagKey<Fluid>> ->
                    when (tagKeys.size) {
                        1 -> creator.fromTagKey(tagKeys[0], amount)
                        else -> creator.fromTagKeys(tagKeys, amount)
                    }
                },
            )
        }

    @JvmRecord
    data class InputEntry<T : Any>(val entry: Either<List<T>, List<TagKey<T>>>, val amount: Int) {
        companion object {
            @JvmStatic
            fun <T : Any> types(types: List<T>, amount: Int): InputEntry<T> = InputEntry(Either.left(types), amount)

            @JvmStatic
            fun <T : Any> tagKeys(tagKeys: List<TagKey<T>>, amount: Int): InputEntry<T> = InputEntry(Either.right(tagKeys), amount)
        }

        fun toOutput(): OutputEntry<T> = OutputEntry(
            entry
                .toIor()
                .mapLeft(List<T>::first)
                .mapRight(List<TagKey<T>>::first),
            amount,
            1f,
        )
    }

    // Outputs
    fun getItemStacks(): List<Pair<ItemStack, Float>> = itemOutputs.map { (entry: Ior<Item, TagKey<Item>>, amount: Int, chance: Float) ->
        val item: Item? = entry.getLeft()
        when {
            item != null -> ItemStack(item, amount) to chance
            else -> error("Cannot create ItemStack from no item output entry")
        }
    }

    fun getFluidStacks(): List<FluidStack> = fluidOutputs.map { (entry: Ior<Fluid, TagKey<Fluid>>, amount: Int, _) ->
        val fluid: Fluid? = entry.getLeft()
        when {
            fluid != null -> FluidStack(fluid, amount)
            else -> error("Cannot create FluidStack from no fluid output entry")
        }
    }

    fun getItemResults(): List<Pair<HTItemResult, Float>> =
        itemOutputs.map { (entry: Ior<Item, TagKey<Item>>, amount: Int, chance: Float) ->
            entry.map(
                { item: Item -> HTResultHelper.item(item, amount) },
                { tagKey: TagKey<Item> -> HTResultHelper.item(tagKey, amount) },
            ) to chance
        }

    fun getFluidResults(): List<HTFluidResult> = fluidOutputs.map { (entry: Ior<Fluid, TagKey<Fluid>>, amount: Int, _) ->
        entry.map(
            { fluid: Fluid -> HTResultHelper.fluid(fluid, amount) },
            { tagKey: TagKey<Fluid> -> HTResultHelper.fluid(tagKey, amount) },
        )
    }

    @JvmRecord
    data class OutputEntry<T : Any>(val entry: Ior<T, TagKey<T>>, val amount: Int, val chance: Float) {
        fun toInput(): InputEntry<T> = InputEntry(
            entry
                .mapLeft(::listOf)
                .mapRight(::listOf)
                .unwrap()
                .map({ it }, { (_, tagKeys: List<TagKey<T>>) -> Either.right(tagKeys) }),
            amount,
        )
    }

    //    Builder    //

    class Builder {
        private val itemInputs: MutableList<InputEntry<Item>> = mutableListOf()
        private val fluidInputs: MutableList<InputEntry<Fluid>> = mutableListOf()
        private var catalyst: Item? = null
        private val itemOutputs: MutableList<OutputEntry<Item>> = mutableListOf()
        private val fluidOutputs: MutableList<OutputEntry<Fluid>> = mutableListOf()
        private var operator: UnaryOperator<String> = UnaryOperator.identity()

        // Input
        fun addInput(vararg items: ItemLike, count: Int = 1): Builder = apply {
            itemInputs.add(InputEntry.types(items.map(ItemLike::asItem), amount = count))
        }

        fun addInput(prefix: HTPrefixLike, material: HTMaterialLike, count: Int = 1): Builder =
            addInput(prefix.itemTagKey(material), count = count)

        fun addInput(vararg tagKeys: TagKey<Item>, count: Int = 1): Builder = addInput(listOf(*tagKeys), count)

        fun addInput(tagKeys: List<TagKey<Item>>, count: Int = 1): Builder = apply {
            itemInputs.add(InputEntry.tagKeys(tagKeys, amount = count))
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

        fun addInput(fluid: Fluid, amount: Int): Builder = apply {
            fluidInputs.add(InputEntry.types(listOf(fluid), amount = amount))
        }

        fun addInput(content: HTFluidContent<*, *, *>, amount: Int): Builder = addInput(content.commonTag, amount)

        fun addInput(tagKey: TagKey<Fluid>, amount: Int): Builder = apply {
            fluidInputs.add(InputEntry.tagKeys(listOf(tagKey), amount = amount))
        }

        // Catalyst
        fun setCatalyst(catalyst: ItemLike?): Builder = apply {
            this.catalyst = catalyst?.asItem()
        }

        // Output
        fun addOutput(
            item: ItemLike?,
            prefix: HTPrefixLike,
            material: HTMaterialLike,
            count: Int = 1,
            chance: Float = 1f,
        ): Builder = addOutput(
            item,
            prefix.itemTagKey(material),
            count,
            chance,
        )

        fun addOutput(
            item: ItemLike?,
            tagKey: TagKey<Item>?,
            count: Int = 1,
            chance: Float = 1f,
        ): Builder = apply {
            val entry: Ior<Item, TagKey<Item>> = if (item != null) {
                if (tagKey != null) {
                    Ior.Both(item.asItem(), tagKey)
                } else {
                    Ior.Left(item.asItem())
                }
            } else {
                if (tagKey != null) {
                    Ior.Right(tagKey)
                } else {
                    error("Either item or tag key required for output!")
                }
            }
            itemOutputs.add(OutputEntry(entry, count, chance))
        }

        fun addOutput(content: HTFluidContent<*, *, *>, amount: Int): Builder = addOutput(content.get(), content.commonTag, amount)

        fun addOutput(fluid: Fluid?, tagKey: TagKey<Fluid>?, amount: Int): Builder = apply {
            val entry: Ior<Fluid, TagKey<Fluid>> = if (fluid != null) {
                if (tagKey != null) {
                    Ior.Both(fluid, tagKey)
                } else {
                    Ior.Left(fluid)
                }
            } else {
                if (tagKey != null) {
                    Ior.Right(tagKey)
                } else {
                    error("Either fluid or tag key required for output!")
                }
            }
            fluidOutputs.add(OutputEntry(entry, amount, 1f))
        }

        // Operator
        fun setPrefix(prefix: String): Builder = setModifier { "$prefix$it" }

        fun setSuffix(suffix: String): Builder = setModifier { "$it$suffix" }

        fun setModifier(operator: UnaryOperator<String>): Builder = apply {
            this.operator = operator
        }

        fun build(): HTRecipeData = HTRecipeData(itemInputs, fluidInputs, catalyst, itemOutputs, fluidOutputs, operator)
    }
}
