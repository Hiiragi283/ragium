package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.inventory.HTSidedStorageBuilder
import hiiragi283.ragium.api.inventory.HTSimpleInventory
import hiiragi283.ragium.api.inventory.HTStorageIO
import hiiragi283.ragium.api.inventory.HTStorageSide
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.machine.HTMachineRecipe
import hiiragi283.ragium.api.recipe.machine.HTMachineRecipeProcessor
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntityBase
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import hiiragi283.ragium.common.screen.HTProcessorScreenHandler
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class HTProcessorBlockEntityBase : HTMachineBlockEntityBase {
    @Deprecated("")
    constructor(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : super(type, pos, state)

    constructor(
        type: BlockEntityType<*>,
        pos: BlockPos,
        state: BlockState,
        machineType: HTMachineConvertible,
        tier: HTMachineTier,
    ) : super(
        type,
        pos,
        state,
        machineType,
        tier,
    )

    override fun validateMachineType(machineType: HTMachineType) {
        check(machineType.isProcessor())
    }

    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        HTMachineRecipeProcessor(
            parent,
            RagiumRecipeTypes.MACHINE,
        ) { machineType: HTMachineType, tier: HTMachineTier, inventory: HTSimpleInventory ->
            HTMachineRecipe.Input.create(
                machineType,
                tier,
                inventory.getStack(0),
                inventory.getStack(1),
                inventory.getStack(2),
                inventory.getStack(3),
            )
        }.process(world, pos, machineType, tier)
    }

    //    HTDelegatedInventory    //

    final override val parent: HTSimpleInventory = HTSidedStorageBuilder(7)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(2, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(3, HTStorageIO.INTERNAL, HTStorageSide.NONE)
        .set(4, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .set(5, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .set(6, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildSimple()

    //    NamedScreenHandlerFactory    //

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler? =
        HTProcessorScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(world, pos))

    //    Simple    //

    class Simple : HTProcessorBlockEntityBase {
        @Deprecated("")
        constructor(pos: BlockPos, state: BlockState) :
            super(RagiumBlockEntityTypes.PROCESSOR_MACHINE, pos, state)

        constructor(pos: BlockPos, state: BlockState, machineType: HTMachineType, tier: HTMachineTier) :
            super(RagiumBlockEntityTypes.PROCESSOR_MACHINE, pos, state, machineType, tier)
    }
}
