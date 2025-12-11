package hiiragi283.ragium.api.stack

import net.minecraft.core.component.DataComponentHolder
import net.minecraft.core.component.DataComponentPatch

interface ImmutableComponentStack<TYPE : Any, STACK : ImmutableComponentStack<TYPE, STACK>> :
    ImmutableStack<TYPE, STACK>,
    DataComponentHolder {
    companion object {
        @JvmStatic
        fun hashValueAndComponents(stack: ImmutableComponentStack<*, *>?): Int = when {
            stack == null -> 0
            else -> {
                val i: Int = 31 + stack.value().hashCode()
                31 * i + stack.components.hashCode()
            }
        }

        /**
         * @see net.minecraft.world.item.ItemStack.hashStackList
         */
        @JvmStatic
        fun hashStackList(stacks: List<ImmutableComponentStack<*, *>?>): Int {
            var i = 0
            for (stack: ImmutableComponentStack<*, *>? in stacks) {
                i = i * 31 + hashValueAndComponents(stack)
            }
            return i
        }

        @JvmStatic
        fun <STACK : ImmutableComponentStack<*, STACK>> isSameValueAndComponents(first: STACK?, second: STACK?): Boolean = when {
            first == null && second == null -> true
            first?.value() != second?.value() -> false
            else -> first?.components == second?.components
        }

        @JvmStatic
        fun <STACK : ImmutableComponentStack<*, STACK>> matches(first: STACK?, second: STACK?): Boolean = when {
            first == second -> true
            first?.amount() != second?.amount() -> false
            else -> isSameValueAndComponents(first, second)
        }

        @JvmStatic
        fun <STACK : ImmutableComponentStack<*, STACK>> listMatches(first: List<STACK?>, second: List<STACK?>): Boolean = when {
            first.size != second.size -> false
            else -> first.indices.all { i: Int -> matches(first[i], second[i]) }
        }
    }

    /**
     * このスタックの[DataComponentPatch]を返します。
     */
    fun componentsPatch(): DataComponentPatch
}
