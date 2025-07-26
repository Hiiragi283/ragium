package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTUniversalRecipeInput
import hiiragi283.ragium.api.storage.item.HTFilteredItemHandler
import hiiragi283.ragium.api.storage.item.HTItemFilter
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.inventory.HTCombineProcessMenu
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
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

class HTAlloySmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<HTUniversalRecipeInput, HTAlloyingRecipe>(
        RagiumRecipeTypes.ALLOYING.get(),
        RagiumBlockEntityTypes.ALLOY_SMELTER,
        pos,
        state,
    ) {
    override val inventory: HTItemHandler = HTItemStackHandler(6, this::setChanged)
    override val energyUsage: Int get() = RagiumConfig.COMMON.advancedMachineEnergyUsage.get()

    //    Ticking    //

    override fun createRecipeInput(): HTUniversalRecipeInput =
        HTUniversalRecipeInput.fromItems(inventory.getStackInSlot(0), inventory.getStackInSlot(1))

    override fun completeProcess(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: IEnergyStorage,
        input: HTUniversalRecipeInput,
        recipe: HTAlloyingRecipe,
    ): TriState {
        // アウトプットに搬出できるか判定する
        for (output: HTItemOutput in recipe.outputs) {
            if (!insertToOutput(2..5, output.get(), true).isEmpty) {
                return TriState.FALSE
            }
        }
        // インプットから正確な個数を引けるか判定する
        if (!consumeItem(input, recipe, 0, 1)) {
            if (!consumeItem(input, recipe, 1, 0)) {
                return TriState.FALSE
            }
        }
        // 実際にアウトプットに搬出する
        for (output: HTItemOutput in recipe.outputs) {
            insertToOutput(2..5, output.getChancedStack(level.random), false)
        }
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS)
        return TriState.TRUE
    }

    private fun consumeItem(
        input: HTUniversalRecipeInput,
        recipe: HTAlloyingRecipe,
        first: Int,
        second: Int,
    ): Boolean = if (recipe.ingredients[0].test(input.getItem(first)) && recipe.ingredients[1].test(input.getItem(second))) {
        inventory.consumeStackInSlot(first, recipe.ingredients[0].count())
        inventory.consumeStackInSlot(second, recipe.ingredients[1].count())
        true
    } else {
        false
    }

    override fun getItemHandler(direction: Direction?): HTFilteredItemHandler = HTFilteredItemHandler(
        inventory,
        object : HTItemFilter {
            override fun canInsert(handler: IItemHandler, slot: Int, stack: ItemStack): Boolean = slot <= 1

            override fun canExtract(handler: IItemHandler, slot: Int, amount: Int): Boolean = slot >= 2
        },
    )

    //    Menu    //

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTCombineProcessMenu = HTCombineProcessMenu(
        RagiumMenuTypes.ALLOY_SMELTER,
        containerId,
        playerInventory,
        blockPos,
        createDefinition(inventory),
        HTCombineProcessMenu.ALLOY_POS,
    )
}
