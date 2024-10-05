package hiiragi283.ragium.api.machine

import com.google.common.collect.ImmutableTable
import com.google.common.collect.Table
import hiiragi283.ragium.common.block.HTMachineBlock
import hiiragi283.ragium.common.util.forEach
import hiiragi283.ragium.common.util.hashTableOf

object HTMachineBlockRegistry {
    @JvmStatic
    val registry: ImmutableTable<HTMachineType, HTMachineTier, HTMachineBlock>
        get() = ImmutableTable.copyOf(registry1)
    private val registry1: Table<HTMachineType, HTMachineTier, HTMachineBlock> = hashTableOf()

    @JvmStatic
    fun register(block: HTMachineBlock) {
        val type: HTMachineType = block.machineType
        val tier: HTMachineTier = block.tier
        check(registry1.put(type, tier, block) == null) {
            "Machine Block with type;$type and tier;$tier is already registered!"
        }
    }

    @JvmStatic
    fun get(type: HTMachineConvertible, tier: HTMachineTier): HTMachineBlock? = registry.get(type.asMachine(), tier)

    @JvmStatic
    fun getOrThrow(type: HTMachineConvertible, tier: HTMachineTier): HTMachineBlock =
        checkNotNull(get(type, tier)) { "Machine Block with type;$type and tier;$tier is not registered!" }

    @JvmStatic
    fun getAllTier(type: HTMachineConvertible): Map<HTMachineTier, HTMachineBlock> = registry.row(type.asMachine())

    @JvmStatic
    fun getAllType(tier: HTMachineTier): Map<HTMachineType, HTMachineBlock> = registry.column(tier)

    @JvmStatic
    val values: Collection<HTMachineBlock>
        get() = registry.values()

    @JvmStatic
    fun forEachBlock(action: (HTMachineBlock) -> Unit) {
        values.forEach(action)
    }

    @JvmStatic
    fun forEach(action: (HTMachineType, HTMachineTier, HTMachineBlock) -> Unit) {
        registry.forEach(action)
    }
}
