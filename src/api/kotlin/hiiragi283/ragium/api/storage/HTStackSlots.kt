package hiiragi283.ragium.api.storage

import hiiragi283.ragium.api.RagiumAPI

fun <STACK : HTStorageStack<*, STACK>, SLOT : HTStackSlot<STACK>> moveStack(from: SLOT?, to: SLOT?, amount: Int): STACK? {
    if (from == null || to == null || amount <= 0) return null
    val simulatedExtract: STACK = from.extract(amount, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
    val simulatedRemain: STACK = to.insert(simulatedExtract, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)
    val simulatedAccepted: Int = amount - simulatedRemain.amountAsInt()

    val extracted: STACK = from.extract(simulatedAccepted, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
    val remainder: STACK = to.insert(extracted, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)

    if (!remainder.isEmpty()) {
        val leftover: STACK = from.insert(remainder, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        if (!leftover.isEmpty()) {
            RagiumAPI.LOGGER.error("Stack slot $from did not accept leftover stack from $to! Voiding it.")
        }
    }
    return remainder
}

fun <STACK : HTStorageStack<*, STACK>, SLOT : HTStackSlot<STACK>> moveStack(from: SLOT?, to: SLOT?): STACK? =
    moveStack(from, to, from?.getAmountAsInt() ?: 0)
