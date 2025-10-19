package hiiragi283.ragium.api.stack

import mekanism.api.chemical.ChemicalStack

fun ChemicalStack.toImmutable(): ImmutableChemicalStack = ImmutableChemicalStack.of(this)
