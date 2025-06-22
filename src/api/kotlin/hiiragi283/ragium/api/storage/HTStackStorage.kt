package hiiragi283.ragium.api.storage

interface HTStackStorage<STACK : Any> {
    val stack: STACK

    fun useStack(action: (STACK) -> STACK): Boolean {
        val oldStack: STACK = stack
        val newStack: STACK = action(oldStack)
        replace(newStack, true)
        return newStack == oldStack
    }

    fun replace(stack: STACK, shouldUpdate: Boolean)

    fun canInsert(stack: STACK): Boolean

    fun insert(stack: STACK, simulate: Boolean): Int
}
