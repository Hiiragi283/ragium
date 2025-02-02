package hiiragi283.ragium.api.machine

import hiiragi283.ragium.api.block.entity.HTBlockEntityHandlerProvider
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.extension.asServerLevel
import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.HTControllerHolder
import hiiragi283.ragium.api.multiblock.HTMultiblockMap
import hiiragi283.ragium.api.property.get
import hiiragi283.ragium.api.world.energyNetwork
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.neoforged.neoforge.energy.IEnergyStorage

interface HTMachineAccess :
    HTBlockEntityHandlerProvider,
    HTControllerHolder {
    val enchantments: ItemEnchantments
    val front: Direction
    val isActive: Boolean
    val levelAccess: Level?
    val machineKey: HTMachineKey
    val pos: BlockPos
    val processCost: Int
    val tickRate: Int

    fun getEnchantmentLevel(key: ResourceKey<Enchantment>): Int {
        val lookup: HolderLookup.RegistryLookup<Enchantment> =
            levelAccess?.registryAccess()?.lookupOrThrow(Registries.ENCHANTMENT) ?: return 0
        return lookup.get(key).map(enchantments::getLevel).orElse(0)
    }

    //    HTBlockEntityHandlerProvider    //

    override fun getEnergyStorage(direction: Direction?): IEnergyStorage? =
        levelAccess?.asServerLevel()?.energyNetwork?.let(HTStorageIO.INPUT::wrapEnergyStorage)

    //    HTControllerHolder    //

    override fun getMultiblockMap(): HTMultiblockMap.Relative? = machineKey.getProperty()[HTMachinePropertyKeys.MULTIBLOCK_MAP]

    override fun getController(): HTControllerDefinition? = levelAccess?.let { HTControllerDefinition(it, pos, front) }
}
