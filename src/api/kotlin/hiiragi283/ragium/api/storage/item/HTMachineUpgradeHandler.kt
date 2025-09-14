package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.storage.value.HTValueSerializable
import hiiragi283.ragium.api.tier.HTBaseTier

interface HTMachineUpgradeHandler :
    HTItemHandler,
    HTValueSerializable {
    fun getTier(): HTBaseTier?
}
