package hiiragi283.ragium.api.machine

import hiiragi283.ragium.api.property.HTPropertyHolder

class HTMachinePropertyRegistry(private val map: Map<HTMachineTypeKey, HTPropertyHolder>) {
    operator fun contains(key: HTMachineTypeKey): Boolean = key in map

    operator fun get(key: HTMachineTypeKey): HTPropertyHolder? = map[key]

    fun getOrEmpty(key: HTMachineTypeKey): HTPropertyHolder = map.getOrDefault(key, HTPropertyHolder.EMPTY)
}
