package hiiragi283.ragium.api.storage

interface HTStackStorage<STACK : Any> {
    val stack: STACK

    fun useStack(action: (STACK) -> STACK) {
        replace(action(stack), true)
    }

    fun replace(stack: STACK, shouldUpdate: Boolean)

    fun canInsert(stack: STACK): Boolean

    fun insert(stack: STACK, simulate: Boolean): Int
}
