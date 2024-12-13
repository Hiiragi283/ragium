package hiiragi283.ragium.api.machine

import com.mojang.serialization.DynamicOps
import com.mojang.serialization.Keyable
import hiiragi283.ragium.api.machine.block.HTMachineBlock
import hiiragi283.ragium.api.property.HTPropertyHolder
import net.minecraft.item.ItemConvertible
import net.minecraft.util.Identifier
import java.util.stream.Stream

/**
 * A registry of [HTMachineKey]
 * @see hiiragi283.ragium.api.RagiumAPI.machineRegistry
 */
class HTMachineRegistry(
    private val types: Map<HTMachineKey, HTMachineType>,
    private val blockMap: Map<HTMachineKey, HTMachineBlock>,
    private val properties: Map<HTMachineKey, HTPropertyHolder>,
) : Keyable {
    val keys: Set<HTMachineKey>
        get() = types.keys
    val entryMap: Map<HTMachineKey, Entry>
        get() = types.keys.associateWith(::getEntry)
    val blocks: Collection<HTMachineBlock>
        get() = blockMap.values

    operator fun contains(key: HTMachineKey): Boolean = key in types

    fun getBlock(key: HTMachineKey): HTMachineBlock? = blockMap[key]

    fun getEntry(key: HTMachineKey): Entry = Entry(
        checkNotNull(types[key]) { "Invalid machine key; $key!" },
        checkNotNull(blockMap[key]) { "Invalid machine key; $key!" },
        properties.getOrDefault(key, HTPropertyHolder.Empty),
    )

    //    Keyable    //

    override fun <T : Any> keys(ops: DynamicOps<T>): Stream<T> = keys
        .stream()
        .map(HTMachineKey::id)
        .map(Identifier::toString)
        .map(ops::createString)

    //    Entry    //

    data class Entry(val type: HTMachineType, val block: HTMachineBlock, val property: HTPropertyHolder) :
        HTPropertyHolder by property,
        ItemConvertible by block
}
