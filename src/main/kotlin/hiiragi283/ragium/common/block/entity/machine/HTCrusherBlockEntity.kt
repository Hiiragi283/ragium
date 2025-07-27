package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTUniversalRecipeInput
import hiiragi283.ragium.api.storage.item.HTFilteredItemHandler
import hiiragi283.ragium.api.storage.item.HTItemFilter
import hiiragi283.ragium.common.inventory.HTDecomposeProcessMenu
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumMenuTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.items.IItemHandler

class HTCrusherBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<HTUniversalRecipeInput, HTCrushingRecipe>(
        RagiumRecipeTypes.CRUSHING.get(),
        RagiumBlockEntityTypes.CRUSHER,
        pos,
        state,
    ) {
    override val inventory = HTItemStackHandler(5, this::setChanged)
    override val energyUsage: Int get() = RagiumConfig.COMMON.basicMachineEnergyUsage.get()

    //    Ticking    //

    override fun createRecipeInput(): HTUniversalRecipeInput = HTUniversalRecipeInput.fromItems(inventory.getStackInSlot(0))

    override fun completeProcess(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: IEnergyStorage,
        input: HTUniversalRecipeInput,
        recipe: HTCrushingRecipe,
    ): TriState {
        // アウトプットに搬出できるか判定する
        for (output: HTItemOutput in recipe.outputs) {
            if (!insertToOutput(1..4, output.get(), true).isEmpty) {
                return TriState.FALSE
            }
        }
        // 実際にアウトプットに搬出する
        for (output: HTItemOutput in recipe.outputs) {
            insertToOutput(1..4, output.getChancedStack(level.random), false)
        }
        // インプットを減らす
        inventory.consumeStackInSlot(0, recipe.ingredient.count(), false)
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.STONE_BREAK, SoundSource.BLOCKS)
        return TriState.TRUE
    }

    override fun getItemHandler(direction: Direction?): HTFilteredItemHandler = HTFilteredItemHandler(
        inventory,
        object : HTItemFilter {
            override fun canInsert(handler: IItemHandler, slot: Int, stack: ItemStack): Boolean = slot == 0

            override fun canExtract(handler: IItemHandler, slot: Int, amount: Int): Boolean = slot != 0
        },
    )

    //    Menu    //

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTDecomposeProcessMenu = HTDecomposeProcessMenu(
        RagiumMenuTypes.CRUSHER,
        containerId,
        playerInventory,
        blockPos,
        createDefinition(inventory),
    )
}
