package hiiragi283.ragium.common.machine.consume

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.useTransaction
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockBuilder
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockComponent
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockController
import hiiragi283.ragium.api.material.HTTagPrefix
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
    HTMultiblockController {
    override var key: HTMachineKey = RagiumMachineKeys.BEDROCK_MINER

    constructor(pos: BlockPos, state: BlockState, tier: HTMachineTier) : this(pos, state) {
        this.tier = tier
    }

    override fun process(world: World, pos: BlockPos): DataResult<Unit> {
        val aboveStorage: Storage<ItemVariant> = ItemStorage.SIDED.find(world, pos.up(), Direction.DOWN)
            ?: return DataResult.error { "Failed to find above storage!" }
        val chosenOre: Item = RagiumAPI
            .getInstance()
            .materialRegistry
            .entryMap
            .mapNotNull { it.value.getFirstItem(HTTagPrefix.ORE) }
            .randomOrNull() ?: return DataResult.error { "Failed to find mineable ore!" }
        return useTransaction { transaction: Transaction ->
            if (aboveStorage.insert(ItemVariant.of(chosenOre), 1, transaction) > 0) {
                transaction.commit()
                DataResult.success(Unit)
            } else {
                transaction.abort()
                DataResult.error { "Failed to insert ores into the above storage!" }
            }
        }
    }

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean = false

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTSmallMachineScreenHandler(syncId, playerInventory, packet, createContext())

    //    HTMultiblockController    //

    override var showPreview: Boolean = false

    override fun buildMultiblock(builder: HTMultiblockBuilder) {
        // drill
        builder.add(0, -3, 0, HTMultiblockComponent.Simple(Blocks.BEDROCK))
        // builder.add(0, -2, 0, HTMultiblockComponent.Simple(RagiumBlocks.SHAFT))
        // builder.add(0, -1, 0, HTMultiblockComponent.Simple(RagiumBlocks.SHAFT))
        builder.add(-1, 0, 0, HTMultiblockComponent.Simple(RagiumBlocks.SHAFT))
        builder.add(0, 0, -1, HTMultiblockComponent.Simple(RagiumBlocks.SHAFT))
        builder.add(0, 0, 1, HTMultiblockComponent.Simple(RagiumBlocks.SHAFT))
        builder.add(1, 0, 0, HTMultiblockComponent.Simple(RagiumBlocks.SHAFT))
        // frame
        builder.add(-2, -1, 0, HTMultiblockComponent.Simple(tier.getCasing()))
        builder.add(0, -1, -2, HTMultiblockComponent.Simple(tier.getCasing()))
        builder.add(0, -1, 2, HTMultiblockComponent.Simple(tier.getCasing()))
        builder.add(2, -1, 0, HTMultiblockComponent.Simple(tier.getCasing()))

        builder.add(-2, 0, 0, HTMultiblockComponent.Simple(tier.getHull()))
        builder.add(0, 0, -2, HTMultiblockComponent.Simple(tier.getHull()))
        builder.add(0, 0, 2, HTMultiblockComponent.Simple(tier.getHull()))
        builder.add(2, 0, 0, HTMultiblockComponent.Simple(tier.getHull()))

        builder.add(-2, 0, -2, HTMultiblockComponent.Simple(tier.getGrate()))
        builder.add(-2, 0, -1, HTMultiblockComponent.Simple(tier.getGrate()))
        builder.add(-2, 0, 1, HTMultiblockComponent.Simple(tier.getGrate()))
        builder.add(-2, 0, -2, HTMultiblockComponent.Simple(tier.getGrate()))
        builder.add(-1, 0, -2, HTMultiblockComponent.Simple(tier.getGrate()))
        builder.add(-1, 0, 2, HTMultiblockComponent.Simple(tier.getGrate()))
        builder.add(1, 0, -2, HTMultiblockComponent.Simple(tier.getGrate()))
        builder.add(1, 0, 2, HTMultiblockComponent.Simple(tier.getGrate()))
        builder.add(2, 0, -2, HTMultiblockComponent.Simple(tier.getGrate()))
        builder.add(2, 0, -1, HTMultiblockComponent.Simple(tier.getGrate()))
        builder.add(2, 0, 1, HTMultiblockComponent.Simple(tier.getGrate()))
        builder.add(2, 0, -2, HTMultiblockComponent.Simple(tier.getGrate()))

        builder.add(-2, 1, 0, HTMultiblockComponent.Simple(tier.getStorageBlock()))
        builder.add(0, 1, -2, HTMultiblockComponent.Simple(tier.getStorageBlock()))
        builder.add(0, 1, 2, HTMultiblockComponent.Simple(tier.getStorageBlock()))
        builder.add(2, 1, 0, HTMultiblockComponent.Simple(tier.getStorageBlock()))
    }
}
