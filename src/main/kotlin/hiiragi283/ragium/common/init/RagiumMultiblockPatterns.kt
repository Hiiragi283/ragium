package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineTypeKey
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPattern
import net.minecraft.registry.RegistryKey

object RagiumMultiblockPatterns {
    @JvmField
    val ALCHEMICAL_INFUSER: RegistryKey<HTMultiblockPattern> = create("alchemical_infuser")

    @JvmField
    val BLAST_FURNACE: Map<HTMachineTier, RegistryKey<HTMultiblockPattern>> =
        createTiered(RagiumMachineTypes.BLAST_FURNACE)

    @JvmField
    val DISTILLATION_TOWER: Map<HTMachineTier, RegistryKey<HTMultiblockPattern>> =
        createTiered(RagiumMachineTypes.DISTILLATION_TOWER)

    @JvmField
    val SAW_MILL: Map<HTMachineTier, RegistryKey<HTMultiblockPattern>> =
        createTiered(RagiumMachineTypes.SAW_MILL)

    @JvmStatic
    private fun create(name: String): RegistryKey<HTMultiblockPattern> =
        RegistryKey.of(HTMultiblockPattern.REGISTRY_KEY, RagiumAPI.id(name))

    @JvmStatic
    private fun createTiered(key: HTMachineTypeKey): Map<HTMachineTier, RegistryKey<HTMultiblockPattern>> =
        HTMachineTier.entries.associateWith { create("${it.asString()}/${key.id.path}") }
}
