package hiiragi283.ragium.common.block.machine.process

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTRecipeProcessorBlockEntityBase
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockBuilder
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockComponent
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockController
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTStorageBuilder
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.HTStorageSide
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.recipe.HTMachineRecipeProcessor
import hiiragi283.ragium.common.screen.HTDistillationTowerScreenHandler
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos

class HTDistillationTowerBlockEntity(pos: BlockPos, state: BlockState) :
    HTRecipeProcessorBlockEntityBase(RagiumBlockEntityTypes.DISTILLATION_TOWER, pos, state),
    HTMultiblockController {
    override var key: HTMachineKey = RagiumMachineKeys.DISTILLATION_TOWER

    constructor(pos: BlockPos, state: BlockState, tier: HTMachineTier) : this(pos, state) {
        this.tier = tier
    }

    override val inventory: SidedInventory = HTStorageBuilder(2)
        .set(0, HTStorageIO.INTERNAL, HTStorageSide.NONE)
        .set(1, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildInventory()

    override val fluidStorage: HTMachineFluidStorage = HTStorageBuilder(4)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .set(2, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .set(3, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildMachineFluidStorage()

    override val processor = HTMachineRecipeProcessor(
        inventory,
        intArrayOf(),
        intArrayOf(1),
        0,
        fluidStorage,
        intArrayOf(0),
        intArrayOf(1, 2, 3),
    )

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler? =
        HTDistillationTowerScreenHandler(syncId, playerInventory, packet, createContext())

    //    HTMultiblockController    //

    override var showPreview: Boolean = false

    override fun buildMultiblock(builder: HTMultiblockBuilder) {
        builder
            .addLayer(
                -1..1,
                -1,
                1..3,
                HTMultiblockComponent.Simple(tier.getCasing()),
            ).addHollow(
                -1..1,
                0,
                1..3,
                HTMultiblockComponent.Simple(tier.getHull()),
            ).addCross4(
                -1..1,
                1,
                1..3,
                HTMultiblockComponent.Simple(Blocks.RED_CONCRETE),
            ).addCross4(
                -1..1,
                2,
                1..3,
                HTMultiblockComponent.Simple(Blocks.WHITE_CONCRETE),
            ).addCross4(
                -1..1,
                3,
                1..3,
                HTMultiblockComponent.Simple(Blocks.RED_CONCRETE),
            )
        builder.add(
            0,
            4,
            2,
            HTMultiblockComponent.Simple(Blocks.WHITE_CONCRETE),
        )
    }
}
