package hiiragi283.ragium.common.block.machine.process

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTRecipeProcessorBlockEntityBase
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTStorageBuilder
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.HTStorageSide
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.recipe.HTMachineRecipeProcessor
import hiiragi283.ragium.common.screen.HTChemicalMachineScreenHandler
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos

class HTChemicalRecipeProcessorBlockEntity(pos: BlockPos, state: BlockState) :
    HTRecipeProcessorBlockEntityBase(RagiumBlockEntityTypes.CHEMICAL_PROCESSOR, pos, state) {
    override var key: HTMachineKey = RagiumMachineKeys.CHEMICAL_REACTOR

    constructor(pos: BlockPos, state: BlockState, key: HTMachineKey, tier: HTMachineTier) : this(pos, state) {
        this.key = key
        this.tier = tier
    }

    override val inventory: SidedInventory = HTStorageBuilder(5)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(2, HTStorageIO.INTERNAL, HTStorageSide.NONE)
        .set(3, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .set(4, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildInventory()

    override val fluidStorage: HTMachineFluidStorage = HTStorageBuilder(4)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(2, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .set(3, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildMachineFluidStorage()
        .setCallback(this@HTChemicalRecipeProcessorBlockEntity::markDirty)

    override val processor = HTMachineRecipeProcessor(
        inventory,
        intArrayOf(0, 1),
        intArrayOf(3, 4),
        2,
        fluidStorage,
        intArrayOf(0, 1),
        intArrayOf(2, 3),
    )

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTChemicalMachineScreenHandler(syncId, playerInventory, packet, createContext())
}
