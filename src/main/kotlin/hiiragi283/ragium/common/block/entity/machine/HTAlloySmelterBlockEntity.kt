package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTAlloyingRecipe
import hiiragi283.ragium.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.ragium.api.storage.item.HTFilteredItemHandler
import hiiragi283.ragium.api.storage.item.HTItemFilter
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.inventory.HTItemWithItemToItemMenu
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumMenuTypes
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

class HTAlloySmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<HTDoubleRecipeInput, HTAlloyingRecipe>(
        RagiumRecipeTypes.ALLOYING.get(),
        RagiumBlockEntityTypes.ALLOY_SMELTER,
        pos,
        state,
    ) {
    override val inventory: HTItemHandler = HTItemStackHandler(3, this::setChanged)
    override val energyUsage: Int get() = RagiumAPI.getConfig().getAdvancedMachineEnergyUsage()

    //    Ticking    //

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTDoubleRecipeInput =
        HTDoubleRecipeInput(inventory.getStackInSlot(0), inventory.getStackInSlot(1))

    override fun canProgressRecipe(level: ServerLevel, input: HTDoubleRecipeInput, recipe: HTAlloyingRecipe): Boolean {
        // アウトプットに搬出できるか判定する
        if (!insertToOutput(2..2, recipe.assemble(input, level.registryAccess()), true).isEmpty) {
            return false
        }
        // インプットから正確な個数を引けるか判定する
        if (!consumeItem(input, recipe, 0, 1)) {
            if (!consumeItem(input, recipe, 1, 0)) {
                return false
            }
        }
        return true
    }

    override fun serverTickPost(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTDoubleRecipeInput,
        recipe: HTAlloyingRecipe,
    ) {
        // 実際にアウトプットに搬出する
        insertToOutput(2..2, recipe.assemble(input, level.registryAccess()), false)
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS)
    }

    private fun consumeItem(
        input: HTDoubleRecipeInput,
        recipe: HTAlloyingRecipe,
        first: Int,
        second: Int,
    ): Boolean = if (recipe.ingredients[0].test(input.getItem(first)) && recipe.ingredients[1].test(input.getItem(second))) {
        inventory.consumeStackInSlot(first, recipe.ingredients[0], false)
        inventory.consumeStackInSlot(second, recipe.ingredients[1], false)
        true
    } else {
        false
    }

    override fun getItemHandler(direction: Direction?): HTFilteredItemHandler = HTFilteredItemHandler(
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

    //    Menu    //

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTItemWithItemToItemMenu =
        HTItemWithItemToItemMenu(
            RagiumMenuTypes.ALLOY_SMELTER,
            containerId,
            playerInventory,
            blockPos,
            createDefinition(inventory),
            HTItemWithItemToItemMenu.ALLOY_POS,
        )
}
