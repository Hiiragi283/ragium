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
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTMachineRecipeProcessor
import hiiragi283.ragium.common.screen.HTLargeProcessorScreenHandler
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage
import net.fabricmc.fabric.api.transfer.v1.storage.base.FilteringStorage
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.Direction

open class HTLargeProcessorMachineEntity(type: HTMachineConvertible, tier: HTMachineTier) : HTProcessorMachineEntityBase(type, tier) {
    final override val parent: HTSimpleInventory = HTStorageBuilder(7)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(2, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(3, HTStorageIO.INTERNAL, HTStorageSide.NONE)
        .set(4, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .set(5, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .set(6, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildSimple()

    final override val fluidStorage: HTMachineFluidStorage =
        HTMachineFluidStorage.of(HTMachineRecipe.SizeType.LARGE) { index: Int, storage: SingleFluidStorage ->
            /*parentBE.sendPacket {
                RagiumNetworks.syncFluidStorage(
                    it,
                    pos,
                    index,
                    storage.resource,
                    storage.amount,
                )
            }*/
        }

    final override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = CombinedStorage<FluidVariant, Storage<FluidVariant>>(
        listOf(
            FilteringStorage.insertOnlyOf(fluidStorage[0]),
            FilteringStorage.insertOnlyOf(fluidStorage[1]),
            FilteringStorage.extractOnlyOf(fluidStorage[2]),
            FilteringStorage.extractOnlyOf(fluidStorage[3]),
        ),
    )

    final override val processor: HTMachineRecipeProcessor = HTMachineRecipeProcessor.ofLarge(parent, fluidStorage)

    override fun createInput(): HTMachineInput = HTMachineInput.Large(
        definition,
        parent.getStack(0),
        parent.getStack(1),
        parent.getStack(2),
        fluidStorage.getSlot(0).resourceAmount,
        fluidStorage.getSlot(1).resourceAmount,
    )

    final override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTLargeProcessorScreenHandler(
            syncId,
            playerInventory,
            packet,
            createContext(),
        )
}
