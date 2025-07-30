package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.recipe.HTUniversalRecipeInput
import hiiragi283.ragium.api.storage.item.HTFilteredItemHandler
import hiiragi283.ragium.api.storage.item.HTItemFilter
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.inventory.HTCombineProcessMenu
import hiiragi283.ragium.common.recipe.HTPressingRecipe
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
import net.neoforged.neoforge.items.IItemHandler

class HTFormingPressBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<HTUniversalRecipeInput, HTPressingRecipe>(
        RagiumRecipeTypes.PRESSING.get(),
        RagiumBlockEntityTypes.FORMING_PRESS,
        pos,
        state,
    ) {
    override val inventory: HTItemHandler = HTItemStackHandler(3, this::setChanged)
    override val energyUsage: Int get() = RagiumConfig.COMMON.advancedMachineEnergyUsage.get()

    //    Ticking    //

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTUniversalRecipeInput =
        HTUniversalRecipeInput.fromItems(inventory.getStackInSlot(0), inventory.getStackInSlot(1))

    override fun canProgressRecipe(level: ServerLevel, input: HTUniversalRecipeInput, recipe: HTPressingRecipe): Boolean {
        // アウトプットに搬出できるか判定する
        if (!insertToOutput(2..2, recipe.output.get(), true).isEmpty) {
            return false
        }
        return true
    }

    override fun serverTickPost(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTUniversalRecipeInput,
        recipe: HTPressingRecipe,
    ) {
        // 実際にアウトプットに搬出する
        insertToOutput(2..2, recipe.output.getChancedStack(level.random), false)
        // インプットを減らす
        inventory.consumeStackInSlot(0, 1, false)
        inventory.consumeStackInSlot(1, 1, true)
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS)
    }

    override fun getItemHandler(direction: Direction?): IItemHandler? = HTFilteredItemHandler(
        inventory,
        object : HTItemFilter {
            override fun canInsert(handler: IItemHandler, slot: Int, stack: ItemStack): Boolean = when (slot) {
                0 -> !ItemStack.isSameItemSameComponents(stack, handler.getStackInSlot(1))
                1 -> !ItemStack.isSameItemSameComponents(stack, handler.getStackInSlot(0))
                else -> false
            }

            override fun canExtract(handler: IItemHandler, slot: Int, amount: Int): Boolean = slot == 2
        },
    )

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTCombineProcessMenu = HTCombineProcessMenu(
        RagiumMenuTypes.FORMING_PRESS,
        containerId,
        playerInventory,
        blockPos,
        createDefinition(inventory),
        HTCombineProcessMenu.PRESS_POS,
    )
}
