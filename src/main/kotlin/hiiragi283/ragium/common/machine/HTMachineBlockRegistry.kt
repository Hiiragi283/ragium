package hiiragi283.ragium.common.machine

import com.google.common.collect.ImmutableTable
import com.google.common.collect.Table
import hiiragi283.ragium.common.block.HTMachineBlockBase
import hiiragi283.ragium.common.util.forEach
import hiiragi283.ragium.common.util.hashTableOf

object HTMachineBlockRegistry {
    @JvmStatic
    val registry: ImmutableTable<HTMachineType, HTMachineTier, HTMachineBlockBase>
        get() = ImmutableTable.copyOf(registry1)
    private val registry1: Table<HTMachineType, HTMachineTier, HTMachineBlockBase> = hashTableOf()

    @JvmStatic
    fun register(block: HTMachineBlockBase) {
        val type: HTMachineType = block.machineType
        val tier: HTMachineTier = block.tier
        check(registry1.put(type, tier, block) == null) {
            "Machine Block with type;$type and tier;$tier is already registered!"
        }
    }

    @JvmStatic
    fun get(type: HTMachineConvertible, tier: HTMachineTier): HTMachineBlockBase? = registry.get(type.asMachine(), tier)

    @JvmStatic
    fun getOrThrow(type: HTMachineConvertible, tier: HTMachineTier): HTMachineBlockBase =
        checkNotNull(get(type, tier)) { "Machine Block with type;$type and tier;$tier is not registered!" }

    @JvmStatic
    val values: Collection<HTMachineBlockBase>
        get() = registry.values()

    @JvmStatic
    fun forEachBlock(action: (HTMachineBlockBase) -> Unit) {
        values.forEach(action)
    }

    @JvmStatic
    fun forEach(action: (HTMachineType, HTMachineTier, HTMachineBlockBase) -> Unit) {
        registry.forEach(action)
    }
}
