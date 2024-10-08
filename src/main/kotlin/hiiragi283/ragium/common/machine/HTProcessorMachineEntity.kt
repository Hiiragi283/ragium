package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.inventory.HTSidedStorageBuilder
import hiiragi283.ragium.api.inventory.HTSimpleInventory
import hiiragi283.ragium.api.inventory.HTStorageIO
import hiiragi283.ragium.api.inventory.HTStorageSide
import hiiragi283.ragium.api.machine.HTMachineEntity
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.machine.HTMachineRecipe
import hiiragi283.ragium.api.recipe.machine.HTMachineRecipeProcessor
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import hiiragi283.ragium.common.screen.HTProcessorScreenHandler
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

open class HTProcessorMachineEntity(machineType: HTMachineType, tier: HTMachineTier) : HTMachineEntity(machineType, tier) {
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

    override val parent: HTSimpleInventory = HTSidedStorageBuilder(7)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(2, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(3, HTStorageIO.INTERNAL, HTStorageSide.NONE)
        .set(4, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .set(5, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .set(6, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildSimple()

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTProcessorScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(parentBE.world, parentBE.pos))
}
