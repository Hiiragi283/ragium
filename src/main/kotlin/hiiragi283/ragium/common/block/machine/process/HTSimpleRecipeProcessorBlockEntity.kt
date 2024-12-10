package hiiragi283.ragium.common.block.machine.process

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.block.HTRecipeProcessorBlockEntityBase
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTStorageBuilder
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.HTStorageSide
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.recipe.HTMachineRecipeProcessor
import hiiragi283.ragium.common.screen.HTSimpleMachineScreenHandler
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos

class HTSimpleRecipeProcessorBlockEntity(pos: BlockPos, state: BlockState) :
    HTRecipeProcessorBlockEntityBase(RagiumBlockEntityTypes.SIMPLE_PROCESSOR, pos, state) {
    override var key: HTMachineKey = RagiumMachineKeys.ALLOY_FURNACE

    constructor(pos: BlockPos, state: BlockState, key: HTMachineKey) : this(pos, state) {
        this.key = key
    }

    override val inventory: SidedInventory = HTStorageBuilder(5)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(2, HTStorageIO.INTERNAL, HTStorageSide.NONE)
        .set(3, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .set(4, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildInventory()

    override val fluidStorage: HTMachineFluidStorage = HTStorageBuilder(2)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildMachineFluidStorage(tier)
        .setCallback(this@HTSimpleRecipeProcessorBlockEntity::markDirty)

    override val processor = HTMachineRecipeProcessor(
        inventory,
        intArrayOf(0, 1),
        intArrayOf(3, 4),
        2,
        fluidStorage,
        intArrayOf(0),
        intArrayOf(1),
    )

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTSimpleMachineScreenHandler(syncId, playerInventory, createContext())
}
