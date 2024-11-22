package hiiragi283.ragium.common.machine.consume

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockBuilder
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockComponent
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockController
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.screen.HTSmallMachineScreenHandler
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTBedrockMinerBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(RagiumBlockEntityTypes.BEDROCK_MINER, pos, state),
    HTMultiblockController {
    override var key: HTMachineKey = RagiumMachineKeys.BEDROCK_MINER

    constructor(pos: BlockPos, state: BlockState, tier: HTMachineTier) : this(pos, state) {
        this.tier = tier
    }

    override fun getRequiredEnergy(world: World, pos: BlockPos): DataResult<Pair<HTEnergyNetwork.Flag, Long>> =
        tier.createEnergyResult(HTEnergyNetwork.Flag.CONSUME)

    override fun process(world: World, pos: BlockPos): DataResult<Unit> = DataResult.error { "WIP" }

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean = false

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTSmallMachineScreenHandler(syncId, playerInventory, packet, createContext())

    //    HTMultiblockController    //

    override var showPreview: Boolean = false
    override var isValid: Boolean = false

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
