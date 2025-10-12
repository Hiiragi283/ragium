package hiiragi283.ragium.common.integration.mekanism.storage

import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import mekanism.api.chemical.ChemicalStack

fun HTChemicalTank.getChemicalStack(): ChemicalStack = this.getStack().stack

fun HTChemicalTank.getCapacityAsLong(stack: ChemicalStack): Long = this.getCapacityAsLong(HTChemicalStorageStack.of(stack))

fun HTChemicalTank.getCapacityAsInt(stack: ChemicalStack): Int = this.getCapacityAsInt(HTChemicalStorageStack.of(stack))

fun HTChemicalTank.isValid(stack: ChemicalStack): Boolean = this.isValid(HTChemicalStorageStack.of(stack))

fun HTChemicalTank.insertChemical(stack: ChemicalStack, action: HTStorageAction, access: HTStorageAccess): ChemicalStack =
    this.insert(HTChemicalStorageStack.of(stack), action, access).stack

fun HTChemicalTank.extractChemical(amount: Int, action: HTStorageAction, access: HTStorageAccess): ChemicalStack =
    this.extract(amount, action, access).stack

fun HTChemicalTank.Mutable.setChemicalStack(stack: ChemicalStack) {
    setStack(HTChemicalStorageStack.of(stack))
}
