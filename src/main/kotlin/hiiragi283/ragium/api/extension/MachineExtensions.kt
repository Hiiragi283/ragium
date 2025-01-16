package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineRegistry
import hiiragi283.ragium.api.machine.HTMachineTier

//    validTiers    //

val HTMachineKey.validTiers: List<HTMachineTier>
    get() = getEntry().validTiers

val HTMachineKey.firstTier: HTMachineTier
    get() = getEntry().firstTier

val HTMachineKey.lastTier: HTMachineTier
    get() = getEntry().lastTier

val HTMachineRegistry.Entry.validTiers: List<HTMachineTier>
    get() = getOrDefault(HTMachinePropertyKeys.VALID_TIERS)

val HTMachineRegistry.Entry.firstTier: HTMachineTier
    get() = validTiers.first()

val HTMachineRegistry.Entry.lastTier: HTMachineTier
    get() = validTiers.last()
