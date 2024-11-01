package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.extension.resourceAmount
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.machine.entity.HTProcessorMachineEntityBase
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.common.screen.HTSimpleMachineScreenHandler
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler

class HTSimpleProcessorMachineEntity(type: HTMachineConvertible, tier: HTMachineTier) :
    HTProcessorMachineEntityBase(HTMachineType.Size.SIMPLE, type, tier) {
    override fun createInput(): HTMachineInput = HTMachineInput.create(machineType, tier) {
        add(parent.getStack(0))
        add(parent.getStack(1))
        add(fluidStorage[0].resourceAmount)
    }

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTSimpleMachineScreenHandler(
            syncId,
            playerInventory,
            packet,
            createContext(),
        )
}
