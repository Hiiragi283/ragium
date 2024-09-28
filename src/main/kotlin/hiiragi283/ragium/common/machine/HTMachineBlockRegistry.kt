package hiiragi283.ragium.common.machine

import com.google.common.collect.HashBasedTable
import com.google.common.collect.ImmutableTable
import com.google.common.collect.Table
import hiiragi283.ragium.common.block.HTMachineBlockBase

object HTMachineBlockRegistry {
    @JvmStatic
    val registry: ImmutableTable<HTMachineType<*>, HTMachineTier, HTMachineBlockBase>
        get() = ImmutableTable.copyOf(registry1)
    private val registry1: Table<HTMachineType<*>, HTMachineTier, HTMachineBlockBase> = HashBasedTable.create()

    @JvmStatic
    fun register(block: HTMachineBlockBase) {
        val type: HTMachineType<*> = block.machineType
        val tier: HTMachineTier = block.tier
        check(registry1.put(type, tier, block) == null) {
            "Machine Block with type;$type and tier;$tier is already registered!"
        }
    }

    @JvmStatic
    fun get(type: HTMachineType<*>, tier: HTMachineTier): HTMachineBlockBase? = registry.get(type, tier)

    @JvmStatic
    fun getOrThrow(type: HTMachineType<*>, tier: HTMachineTier): HTMachineBlockBase =
        checkNotNull(get(type, tier)) { "Machine Block with type;$type and tier;$tier is not registered!" }
}
