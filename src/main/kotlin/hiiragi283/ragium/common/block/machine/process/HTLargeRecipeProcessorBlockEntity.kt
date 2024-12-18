package hiiragi283.ragium.common.block.machine.process

import hiiragi283.ragium.api.block.HTRecipeProcessorBlockEntityBase
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockBuilder
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockManager
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockProvider
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTMachineInventory
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.recipe.HTMachineRecipeProcessor
import hiiragi283.ragium.common.screen.HTLargeMachineScreenHandler
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos

class HTLargeRecipeProcessorBlockEntity(pos: BlockPos, state: BlockState) :
    HTRecipeProcessorBlockEntityBase(RagiumBlockEntityTypes.LARGE_PROCESSOR, pos, state),
    HTMultiblockProvider {
    override var key: HTMachineKey = RagiumMachineKeys.BLAST_FURNACE

    constructor(pos: BlockPos, state: BlockState, key: HTMachineKey) : this(pos, state) {
        this.key = key
    }

    override val inventory: HTMachineInventory = HTMachineInventory.ofLarge()

    override val fluidStorage: HTMachineFluidStorage = HTMachineFluidStorage.ofLarge(this)

    override val processor = HTMachineRecipeProcessor(
        inventory,
        intArrayOf(0, 1, 2),
        intArrayOf(4, 5, 6),
        3,
        fluidStorage,
        intArrayOf(0, 1),
        intArrayOf(2, 3),
    )

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTLargeMachineScreenHandler(syncId, playerInventory, createContext())

    //    HTMultiblockProvider    //

    override val multiblockManager = HTMultiblockManager(::getWorld, pos, this)

    override fun buildMultiblock(builder: HTMultiblockBuilder) {
        key.entry[HTMachinePropertyKeys.MULTIBLOCK_PATTERN]?.invoke(builder)
    }
}
