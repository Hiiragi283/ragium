package hiiragi283.ragium.api.storage.capability

import hiiragi283.ragium.api.storage.HTStorageAction
import mekanism.api.Action
import mekanism.api.chemical.IChemicalHandler

val IChemicalHandler.tankRange: IntRange get() = (0..<this.chemicalTanks)

fun Action.toStorage(): HTStorageAction = when (this.execute()) {
    true -> HTStorageAction.EXECUTE
    false -> HTStorageAction.SIMULATE
}

fun HTStorageAction.toMek(): Action = when (this.execute) {
    true -> Action.EXECUTE
    false -> Action.SIMULATE
}
