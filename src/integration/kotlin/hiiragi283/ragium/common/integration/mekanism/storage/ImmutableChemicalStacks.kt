package hiiragi283.ragium.common.integration.mekanism.storage

import mekanism.api.chemical.ChemicalStack

fun ChemicalStack.toImmutable(): ImmutableChemicalStack = ImmutableChemicalStack.of(this)
