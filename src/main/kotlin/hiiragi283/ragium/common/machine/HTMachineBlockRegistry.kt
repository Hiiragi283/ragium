package hiiragi283.ragium.common.machine

import com.google.common.collect.HashBasedTable
import com.google.common.collect.ImmutableTable
import com.google.common.collect.Table
import hiiragi283.ragium.common.block.HTBaseMachineBlock

object HTMachineBlockRegistry {
    @JvmStatic
    val registry: ImmutableTable<HTMachineType<*>, HTMachineTier, HTBaseMachineBlock>
        get() = ImmutableTable.copyOf(registry1)
    private val registry1: Table<HTMachineType<*>, HTMachineTier, HTBaseMachineBlock> = HashBasedTable.create()

    @JvmStatic
    fun register(block: HTBaseMachineBlock) {
        val type: HTMachineType<*> = block.machineType
        val tier: HTMachineTier = block.tier
        check(registry1.put(type, tier, block) == null) {
            "Machine Block with type;$type and tier;$tier is already registered!"
        }
    }

    @JvmStatic
    fun get(type: HTMachineType<*>, tier: HTMachineTier): HTBaseMachineBlock? = registry.get(type, tier)

    @JvmStatic
    fun getOrThrow(type: HTMachineType<*>, tier: HTMachineTier): HTBaseMachineBlock =
        checkNotNull(get(type, tier)) { "Machine Block with type;$type and tier;$tier is not registered!" }
}
