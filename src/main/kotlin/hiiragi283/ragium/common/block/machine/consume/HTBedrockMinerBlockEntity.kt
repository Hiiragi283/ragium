package hiiragi283.ragium.common.block.machine.consume

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.extension.useTransaction
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockBuilder
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockManager
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPattern
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPatternProvider
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.util.HTUnitResult
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.screen.HTSmallMachineScreenHandler
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.Item
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTBedrockMinerBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(RagiumBlockEntityTypes.BEDROCK_MINER, pos, state),
    HTMultiblockPatternProvider {
    override var key: HTMachineKey = RagiumMachineKeys.BEDROCK_MINER

    override fun process(world: World, pos: BlockPos): HTUnitResult {
        val aboveStorage: Storage<ItemVariant> = ItemStorage.SIDED.find(world, pos.up(), Direction.DOWN)
            ?: return HTUnitResult.errorString { "Failed to find above storage!" }
        val chosenOre: Item = RagiumAPI
            .getInstance()
            .materialRegistry
            .entryMap
            .mapNotNull { it.value.getFirstItem(HTTagPrefix.ORE) }
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

    override fun buildMultiblock(builder: HTMultiblockBuilder) {
        // drill
        builder.add(0, -3, 0, HTMultiblockPattern.of(Blocks.BEDROCK))
        // builder.add(0, -2, 0, HTMultiblockPattern.of(RagiumBlocks.SHAFT))
        // builder.add(0, -1, 0, HTMultiblockPattern.of(RagiumBlocks.SHAFT))
        builder.add(-1, 0, 0, HTMultiblockPattern.of(RagiumBlocks.SHAFT))
        builder.add(0, 0, -1, HTMultiblockPattern.of(RagiumBlocks.SHAFT))
        builder.add(0, 0, 1, HTMultiblockPattern.of(RagiumBlocks.SHAFT))
        builder.add(1, 0, 0, HTMultiblockPattern.of(RagiumBlocks.SHAFT))
        // frame
        builder.add(-2, -1, 0, HTMultiblockPattern.of(tier.getCasing()))
        builder.add(0, -1, -2, HTMultiblockPattern.of(tier.getCasing()))
        builder.add(0, -1, 2, HTMultiblockPattern.of(tier.getCasing()))
        builder.add(2, -1, 0, HTMultiblockPattern.of(tier.getCasing()))

        builder.add(-2, 0, 0, HTMultiblockPattern.of(tier.getHull()))
        builder.add(0, 0, -2, HTMultiblockPattern.of(tier.getHull()))
        builder.add(0, 0, 2, HTMultiblockPattern.of(tier.getHull()))
        builder.add(2, 0, 0, HTMultiblockPattern.of(tier.getHull()))

        builder.add(-2, 0, -2, HTMultiblockPattern.of(tier.getGrate()))
        builder.add(-2, 0, -1, HTMultiblockPattern.of(tier.getGrate()))
        builder.add(-2, 0, 1, HTMultiblockPattern.of(tier.getGrate()))
        builder.add(-2, 0, -2, HTMultiblockPattern.of(tier.getGrate()))
        builder.add(-1, 0, -2, HTMultiblockPattern.of(tier.getGrate()))
        builder.add(-1, 0, 2, HTMultiblockPattern.of(tier.getGrate()))
        builder.add(1, 0, -2, HTMultiblockPattern.of(tier.getGrate()))
        builder.add(1, 0, 2, HTMultiblockPattern.of(tier.getGrate()))
        builder.add(2, 0, -2, HTMultiblockPattern.of(tier.getGrate()))
        builder.add(2, 0, -1, HTMultiblockPattern.of(tier.getGrate()))
        builder.add(2, 0, 1, HTMultiblockPattern.of(tier.getGrate()))
        builder.add(2, 0, -2, HTMultiblockPattern.of(tier.getGrate()))

        builder.add(-2, 1, 0, HTMultiblockPattern.of(tier.getStorageBlock()))
        builder.add(0, 1, -2, HTMultiblockPattern.of(tier.getStorageBlock()))
        builder.add(0, 1, 2, HTMultiblockPattern.of(tier.getStorageBlock()))
        builder.add(2, 1, 0, HTMultiblockPattern.of(tier.getStorageBlock()))
    }
}
