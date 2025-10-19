package hiiragi283.ragium.common.integration.mekanism.storage

import hiiragi283.ragium.api.storage.HTStackSlot
import mekanism.api.chemical.ChemicalStack

/**
 * [ImmutableChemicalStack]向けの[HTStackSlot]の拡張インターフェース
 */
interface HTChemicalTank : HTStackSlot<ImmutableChemicalStack> {
    override fun isSameStack(first: ImmutableChemicalStack, second: ImmutableChemicalStack): Boolean =
        ChemicalStack.isSameChemical(first.stack, second.stack)

    //    Mutable    //

    /**
     * [ImmutableChemicalStack]向けの[HTStackSlot.Mutable]の拡張クラス
     */
    abstract class Mutable :
        HTStackSlot.Mutable<ImmutableChemicalStack>(),
        HTChemicalTank {
        final override fun getEmptyStack(): ImmutableChemicalStack = ImmutableChemicalStack.EMPTY
    }
}
