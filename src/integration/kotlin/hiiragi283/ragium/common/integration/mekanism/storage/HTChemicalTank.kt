package hiiragi283.ragium.common.integration.mekanism.storage

import hiiragi283.ragium.api.storage.HTStackSlot
import mekanism.api.chemical.ChemicalStack

/**
 * [HTChemicalStorageStack]向けの[HTStackSlot]の拡張インターフェース
 */
interface HTChemicalTank : HTStackSlot<HTChemicalStorageStack> {
    //    Mutable    //

    /**
     * [HTChemicalStorageStack]向けの[HTStackSlot.Mutable]の拡張クラス
     */
    abstract class Mutable :
        HTStackSlot.Mutable<HTChemicalStorageStack>(),
        HTChemicalTank {
        final override fun getEmptyStack(): HTChemicalStorageStack = HTChemicalStorageStack.EMPTY

        final override fun isSameStack(first: HTChemicalStorageStack, second: HTChemicalStorageStack): Boolean =
            ChemicalStack.isSameChemical(first.stack, second.stack)
    }
}
