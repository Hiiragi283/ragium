package hiiragi283.ragium.api.machine

import hiiragi283.ragium.api.machine.block.HTMachineBlock
import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.api.util.HTTable

class HTMachineRegistry(
    val types: Map<HTMachineKey, HTMachineTypeNew>,
    val blocks: HTTable<HTMachineKey, HTMachineTier, HTMachineBlock>,
    val properties: Map<HTMachineKey, HTPropertyHolder>,
) {
    operator fun contains(key: HTMachineKey): Boolean = key in types

    fun getType(key: HTMachineKey): HTMachineTypeNew = checkNotNull(types[key]) { "Invalid machine key; $key found!" }

    fun getProperty(key: HTMachineKey): HTPropertyHolder = properties.getOrDefault(key, HTPropertyHolder.Empty)

    internal fun copy(map: Map<HTMachineKey, HTPropertyHolder>): HTMachineRegistry = HTMachineRegistry(types, blocks, map)
}
