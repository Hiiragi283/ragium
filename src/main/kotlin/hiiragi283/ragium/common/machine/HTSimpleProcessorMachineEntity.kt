package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.extension.resourceAmount
import hiiragi283.ragium.api.fluid.HTMachineFluidStorage
import hiiragi283.ragium.api.inventory.HTSimpleInventory
import hiiragi283.ragium.api.inventory.HTStorageBuilder
import hiiragi283.ragium.api.inventory.HTStorageIO
import hiiragi283.ragium.api.inventory.HTStorageSide
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.entity.HTProcessorMachineEntityBase
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTMachineRecipeProcessor
import hiiragi283.ragium.common.screen.HTSimpleMachineScreenHandler
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler

class HTSimpleProcessorMachineEntity(type: HTMachineConvertible, tier: HTMachineTier) : HTProcessorMachineEntityBase(type, tier) {
    override val parent: HTSimpleInventory = HTStorageBuilder(5)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(2, HTStorageIO.INTERNAL, HTStorageSide.NONE)
        .set(3, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .set(4, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildSimple()

    override val fluidStorage: HTMachineFluidStorage = HTMachineFluidStorage.Simple()

    override val processor: HTMachineRecipeProcessor = HTMachineRecipeProcessor.of(parent, fluidStorage)

    override fun createInput(): HTMachineInput = HTMachineInput.Simple(
        definition,
        parent.getStack(0),
        parent.getStack(1),
        fluidStorage.getSlot(0).resourceAmount,
    )

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTSimpleMachineScreenHandler(
            syncId,
            playerInventory,
            packet,
            createContext(),
        )
}
