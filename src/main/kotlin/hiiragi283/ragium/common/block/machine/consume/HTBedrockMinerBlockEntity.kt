package hiiragi283.ragium.common.block.machine.consume

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.extension.createContext
import hiiragi283.ragium.api.extension.useTransaction
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockManager
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockProvider
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.util.HTMachineException
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.screen.HTSmallMachineScreenHandler
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.Item
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTBedrockMinerBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(RagiumBlockEntityTypes.BEDROCK_MINER, pos, state),
    HTMultiblockProvider.Machine {
    override var machineKey: HTMachineKey = RagiumMachineKeys.BEDROCK_MINER

    override fun process(world: World, pos: BlockPos) {
        val aboveStorage: Storage<ItemVariant> =
            ItemStorage.SIDED.find(world, pos.up(), Direction.DOWN)
                ?: throw HTMachineException.Custom(true, "Failed to find above storage!")
        val chosenOre: Item = RagiumAPI
            .getInstance()
            .materialRegistry
            .entryMap
            .mapNotNull { (_: HTMaterialKey, entry: HTMaterialRegistry.Entry) ->
                val prefix: HTTagPrefix = entry.type.getRawPrefix() ?: return@mapNotNull null
                entry.getFirstItemOrNull(prefix)
            }.randomOrNull() ?: throw HTMachineException.Custom(false, "Failed to find mineable raw material!")
        useTransaction { transaction: Transaction ->
            if (aboveStorage.insert(ItemVariant.of(chosenOre), 1, transaction) > 0) {
                transaction.commit()
            } else {
                throw HTMachineException.Custom(true, "Failed to insert ores into the above storage!")
            }
        }
    }

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean = false

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTSmallMachineScreenHandler(syncId, playerInventory, createContext())

    //    HTMultiblockPatternProvider    //

    override val multiblockManager: HTMultiblockManager = HTMultiblockManager(::getWorld, pos, this)
}
