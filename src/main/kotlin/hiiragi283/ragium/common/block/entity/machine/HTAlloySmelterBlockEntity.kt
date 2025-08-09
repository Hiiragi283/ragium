package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTCombineItemToItemRecipe
import hiiragi283.ragium.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.ragium.common.inventory.HTItemWithItemToItemMenu
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.state.BlockState

class HTAlloySmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTDoubleInputBlockEntity<HTCombineItemToItemRecipe>(
        RagiumRecipeTypes.ALLOYING.get(),
        RagiumBlockEntityTypes.ALLOY_SMELTER,
        pos,
        state,
    ) {
    override val energyUsage: Int get() = RagiumAPI.getConfig().getAdvancedMachineEnergyUsage()

    //    Ticking    //

    override fun canProgressRecipe(level: ServerLevel, input: HTDoubleRecipeInput, recipe: HTCombineItemToItemRecipe): Boolean {
        // アウトプットに搬出できるか判定する
        if (!insertToOutput(2..2, recipe.assemble(input, level.registryAccess()), true).isEmpty) {
            return false
        }
        // インプットから正確な個数を引けるか判定する
        if (!canConsumeInput(input, recipe, 0, 1)) {
            if (!canConsumeInput(input, recipe, 1, 0)) {
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
        recipe: HTCombineItemToItemRecipe,
    ) {
        // 実際にアウトプットに搬出する
        insertToOutput(2..2, recipe.assemble(input, level.registryAccess()), false)
        // インプットを減らす
        consumeItem(input, recipe, 0, 1)
        consumeItem(input, recipe, 1, 0)
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS)
    }

    private fun canConsumeInput(
        input: HTDoubleRecipeInput,
        recipe: HTCombineItemToItemRecipe,
        first: Int,
        second: Int,
    ): Boolean = recipe.ingredients[0].test(input.getItem(first)) && recipe.ingredients[1].test(input.getItem(second))

    private fun consumeItem(
        input: HTDoubleRecipeInput,
        recipe: HTCombineItemToItemRecipe,
        first: Int,
        second: Int,
    ) {
        if (recipe.ingredients[0].test(input.getItem(first)) && recipe.ingredients[1].test(input.getItem(second))) {
            inventory.consumeStackInSlot(first, recipe.ingredients[0], false)
            inventory.consumeStackInSlot(second, recipe.ingredients[1], false)
        }
    }

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
