package hiiragi283.ragium.common.util

import hiiragi283.ragium.api.stack.ImmutableStack

/**
 * @see net.neoforged.neoforge.fluids.FluidActionResult
 */
@ConsistentCopyVisibility
@JvmRecord
data class HTStackMoveResult<STACK : ImmutableStack<*, STACK>> private constructor(val succeeded: Boolean, val remainder: STACK?) {
    companion object {
        @JvmStatic
        fun <STACK : ImmutableStack<*, STACK>> failed(): HTStackMoveResult<STACK> = HTStackMoveResult(false, null)

        @JvmStatic
        fun <STACK : ImmutableStack<*, STACK>> succeeded(remainder: STACK?): HTStackMoveResult<STACK> = HTStackMoveResult(true, remainder)
    }
}
