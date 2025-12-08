package hiiragi283.ragium.api.recipe.input

import hiiragi283.ragium.api.recipe.ingredient.HTIngredient
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.ImmutableStack
import hiiragi283.ragium.api.tag.RagiumModTags
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

@ConsistentCopyVisibility
@JvmRecord
data class HTMultiRecipeInput private constructor(val items: List<ImmutableItemStack?>, val fluids: List<ImmutableFluidStack?>) :
    HTFluidRecipeInput {
        companion object {
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
                                val count: Int = ingredient.getRequiredAmount(stack)
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
            fun <STACK : ImmutableStack<*, STACK>> hasMatchingSlots(
                ingredients: List<HTIngredient<*, STACK>>,
                stacks: List<STACK?>,
            ): Boolean {
                val slots: IntArray = getMatchingSlots(ingredients, stacks)
                return slots.isNotEmpty() && slots.size == ingredients.size
            }

            fun <STACK : ImmutableStack<*, STACK>> isEmpty(stacks: List<STACK?>): Boolean =
                stacks.isEmpty() || stacks.filterNotNull().isEmpty()

            @JvmStatic
            inline fun create(builderAction: Builder.() -> Unit): HTMultiRecipeInput? = Builder().apply(builderAction).build()
        }

        constructor(item: ImmutableItemStack?, fluid: ImmutableFluidStack?) : this(listOfNotNull(item), listOfNotNull(fluid))

        private fun validateItem(index: Int): ItemStack = items
            .getOrNull(index)
            ?.takeUnless { stack: ImmutableItemStack -> stack.isOf(RagiumModTags.Items.IGNORED_IN_RECIPES) }
            ?.unwrap()
            ?: ItemStack.EMPTY

        override fun getItem(index: Int): ItemStack = validateItem(index)

        override fun getFluid(index: Int): FluidStack = fluids.getOrNull(index)?.unwrap() ?: FluidStack.EMPTY

        override fun size(): Int = items.size

        override fun isEmpty(): Boolean = isEmpty(items) && isEmpty(fluids)

        class Builder {
            val items: MutableList<ImmutableItemStack?> = mutableListOf()
            val fluids: MutableList<ImmutableFluidStack?> = mutableListOf()

            fun build(): HTMultiRecipeInput? = HTMultiRecipeInput(items, fluids).takeUnless(HTMultiRecipeInput::isEmpty)
        }
    }
