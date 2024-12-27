package hiiragi283.ragium.common.block.machine.consume

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.extension.createContext
import hiiragi283.ragium.api.extension.useTransaction
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockManager
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockProvider
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.util.HTUnitResult
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
    override var key: HTMachineKey = RagiumMachineKeys.BEDROCK_MINER

    override fun process(world: World, pos: BlockPos): HTUnitResult {
        val aboveStorage: Storage<ItemVariant> = ItemStorage.SIDED.find(world, pos.up(), Direction.DOWN)
            ?: return HTUnitResult.errorString { "Failed to find above storage!" }
        val chosenOre: Item = RagiumAPI
            .getInstance()
            .materialRegistry
            .entryMap
            .mapNotNull { it.value.getFirstItemOrNull(HTTagPrefix.ORE) }
            .randomOrNull() ?: return HTUnitResult.errorString { "Failed to find mineable ore!" }
        return useTransaction { transaction: Transaction ->
            if (aboveStorage.insert(ItemVariant.of(chosenOre), 1, transaction) > 0) {
                transaction.commit()
                HTUnitResult.success()
            } else {
                HTUnitResult.errorString { "Failed to insert ores into the above storage!" }
            }
        }
    }

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean = false

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTSmallMachineScreenHandler(syncId, playerInventory, createContext())

    //    HTMultiblockPatternProvider    //

    override val multiblockManager: HTMultiblockManager = HTMultiblockManager(::getWorld, pos, this)
}
