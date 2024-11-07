package hiiragi283.ragium.api.machine

import hiiragi283.ragium.api.machine.block.HTMachineBlock
import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.api.util.HTTable

class HTMachineRegistry(
    private val types: Map<HTMachineKey, HTMachineType>,
    private val blockTables: HTTable<HTMachineKey, HTMachineTier, HTMachineBlock>,
    private val properties: Map<HTMachineKey, HTPropertyHolder>,
) {
    val keys: Set<HTMachineKey>
        get() = types.keys
    val entryMap: Map<HTMachineKey, Entry>
        get() = types.keys.associateWith(::getEntry)
    val blocks: Collection<HTMachineBlock>
        get() = blockTables.values

    operator fun contains(key: HTMachineKey): Boolean = key in types

    fun getBlock(key: HTMachineKey, tier: HTMachineTier): HTMachineBlock? = blockTables.get(key, tier)

    fun getEntry(key: HTMachineKey): Entry = Entry(
        checkNotNull(types[key]) { "Invalid machine key; $key!" },
        blockTables.row(key),
        properties.getOrDefault(key, HTPropertyHolder.Empty),
    )

    //    Entry    //

    data class Entry(val type: HTMachineType, val blockMap: Map<HTMachineTier, HTMachineBlock>, val property: HTPropertyHolder) :
        HTPropertyHolder by property {
        val blocks: Collection<HTMachineBlock>
            get() = blockMap.values

        fun getBlock(tier: HTMachineTier): HTMachineBlock = blockMap[tier]!!
    }
}
