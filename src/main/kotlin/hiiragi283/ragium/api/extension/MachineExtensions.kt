package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineRegistry
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.property.getOrDefault

//    validTiers    //

val HTMachineRegistry.Entry.validTiers: List<HTMachineTier>
    get() = getOrDefault(HTMachinePropertyKeys.VALID_TIERS)

val HTMachineRegistry.Entry.firstTier: HTMachineTier
    get() = validTiers.first()

val HTMachineRegistry.Entry.lastTier: HTMachineTier
    get() = validTiers.last()
