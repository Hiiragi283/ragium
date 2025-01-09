package hiiragi283.ragium.common.block.machine.process

import hiiragi283.ragium.api.block.entity.HTRecipeProcessorBlockEntityBase
import hiiragi283.ragium.api.extension.createContext
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTMachineInventory
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.recipe.HTFurnaceRecipeProcessor
import hiiragi283.ragium.common.screen.HTSmallMachineScreenHandler
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.SmeltingRecipe
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos

class HTMultiSmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTRecipeProcessorBlockEntityBase(RagiumBlockEntityTypes.MULTI_SMELTER, pos, state) {
    override var machineKey: HTMachineKey = RagiumMachineKeys.MULTI_SMELTER

    override fun onTierUpdated(oldTier: HTMachineTier, newTier: HTMachineTier) {
        fluidStorage.update(tier)
    }

    override val inventory: HTMachineInventory = HTMachineInventory.ofSmall()

    override val fluidStorage: HTMachineFluidStorage = HTMachineFluidStorage.EMPTY

    override val processor: HTFurnaceRecipeProcessor<SmeltingRecipe> =
        HTFurnaceRecipeProcessor(RecipeType.SMELTING, inventory, 0, 1)

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler? =
        HTSmallMachineScreenHandler(syncId, playerInventory, createContext())
}
