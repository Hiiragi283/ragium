package hiiragi283.ragium.api.recipe.input

import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.ImmutableStack
import hiiragi283.ragium.api.tag.RagiumModTags
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput
import java.util.Optional

@ConsistentCopyVisibility
@JvmRecord
data class HTRecipeInput private constructor(val items: List<ImmutableItemStack?>, val fluids: List<ImmutableFluidStack?>) : RecipeInput {
    companion object {
        @JvmStatic
        inline fun create(builderAction: Builder.() -> Unit): HTRecipeInput? = Builder().apply(builderAction).build()

        @JvmStatic
        fun single(stack: ImmutableItemStack?): HTRecipeInput? = create {
            items += stack
        }

        @JvmStatic
        fun <STACK : ImmutableStack<*, STACK>> getMatchingSlots(
            ingredients: List<HTIngredient<*, STACK>>,
            stacks: List<STACK?>,
        ): IntArray {
            if (ingredients.isEmpty() || (stacks.isEmpty() || stacks.filterNotNull().isEmpty())) return intArrayOf()
            if (ingredients.size > stacks.size) return intArrayOf()

            val stacks1: MutableList<STACK?> = stacks.toMutableList()
            val result: MutableList<Int> = MutableList(ingredients.size) { -1 }

            ingredients.forEachIndexed { index: Int, ingredient: HTIngredient<*, STACK> ->
                stacks1.forEachIndexed stack@{ index1: Int, stack: STACK? ->
                    if (stack != null) {
                        if (ingredient.test(stack)) {
                            result[index] = index1
                            val count: Int = ingredient.getRequiredAmount()
                            stacks1[index1] = stack.copyWithAmount(stack.amount() - count)
                            return@stack
                        }
                    }
                }
            }
            result.removeIf { it < 0 }
            return when {
                result.size != ingredients.size -> intArrayOf()
                else -> result.toIntArray()
            }
        }

        @JvmStatic
        fun <STACK : ImmutableStack<*, STACK>> hasMatchingSlots(ingredients: List<HTIngredient<*, STACK>>, stacks: List<STACK?>): Boolean {
            val slots: IntArray = getMatchingSlots(ingredients, stacks)
            return slots.isNotEmpty() && slots.size == ingredients.size
        }

        @JvmStatic
        fun <STACK : ImmutableStack<*, STACK>> isEmpty(stacks: List<STACK?>): Boolean = stacks.isEmpty() || stacks.filterNotNull().isEmpty()
    }

    private fun validateItem(index: Int): ImmutableItemStack? = items
        .getOrNull(index)
        ?.takeUnless { stack: ImmutableItemStack -> stack.isOf(RagiumModTags.Items.IGNORED_IN_RECIPES) }

    fun toSingleItem(): SingleRecipeInput? = item(0)?.unwrap()?.let(::SingleRecipeInput)

    fun item(index: Int): ImmutableItemStack? = validateItem(index)

    fun fluid(index: Int): ImmutableFluidStack? = fluids.getOrNull(index)

    fun testItem(index: Int, ingredient: HTItemIngredient): Boolean = testItem(index, ingredient::test)

    inline fun testItem(index: Int, predicate: (ImmutableItemStack) -> Boolean): Boolean = item(index)?.let(predicate) ?: false

    fun testItem(index: Int, ingredient: Optional<HTItemIngredient>): Boolean {
        val stack: ImmutableItemStack = item(index) ?: return ingredient.isEmpty
        return when {
            ingredient.isPresent -> ingredient.get().test(stack)
            else -> false
        }
    }

    fun testFluid(index: Int, ingredient: HTFluidIngredient): Boolean = testFluid(index, ingredient::test)

    inline fun testFluid(index: Int, predicate: (ImmutableFluidStack) -> Boolean): Boolean = fluid(index)?.let(predicate) ?: false

    fun testCatalyst(index: Int, ingredient: HTItemIngredient): Boolean = item(index)?.let(ingredient::testOnlyType) ?: false

    fun testCatalyst(index: Int, ingredient: Optional<HTItemIngredient>): Boolean {
        val stack: ImmutableItemStack = item(index) ?: return ingredient.isEmpty
        return when {
            ingredient.isPresent -> ingredient.get().testOnlyType(stack)
            else -> false
        }
    }

    //    RecipeInput    //

    @Deprecated("Use `item(Int)` instead", ReplaceWith("this.item(Int)"), DeprecationLevel.ERROR)
    override fun getItem(index: Int): ItemStack = item(index)?.unwrap() ?: ItemStack.EMPTY

    override fun size(): Int = items.size

    override fun isEmpty(): Boolean = isEmpty(items) && isEmpty(fluids)

    //    Builder    //

    class Builder {
        val items: MutableList<ImmutableItemStack?> = mutableListOf()
        val fluids: MutableList<ImmutableFluidStack?> = mutableListOf()

        fun build(): HTRecipeInput? = HTRecipeInput(items, fluids).takeUnless(HTRecipeInput::isEmpty)
    }
}
