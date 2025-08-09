package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTItemWithCatalystToItemRecipe
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

class HTFormingPressBlockEntity(pos: BlockPos, state: BlockState) :
    HTDoubleInputBlockEntity<HTItemWithCatalystToItemRecipe>(
        RagiumRecipeTypes.PRESSING.get(),
        RagiumBlockEntityTypes.FORMING_PRESS,
        pos,
        state,
    ) {
    override val energyUsage: Int get() = RagiumAPI.getConfig().getAdvancedMachineEnergyUsage()

    //    Ticking    //

    // アウトプットに搬出できるか判定する
    override fun canProgressRecipe(level: ServerLevel, input: HTDoubleRecipeInput, recipe: HTItemWithCatalystToItemRecipe): Boolean =
        insertToOutput(2..2, recipe.assemble(input, level.registryAccess()), true).isEmpty

    override fun serverTickPost(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTDoubleRecipeInput,
        recipe: HTItemWithCatalystToItemRecipe,
    ) {
        // 実際にアウトプットに搬出する
        insertToOutput(2..2, recipe.assemble(input, level.registryAccess()), false)
        // インプットを減らす
        inventory.extractItem(0, recipe.ingredient, false)
        inventory.extractItem(1, recipe.catalyst, false)
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS)
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTItemWithItemToItemMenu =
        HTItemWithItemToItemMenu(
            RagiumMenuTypes.FORMING_PRESS,
            containerId,
            playerInventory,
            blockPos,
            createDefinition(inventory),
            HTItemWithItemToItemMenu.PRESS_POS,
        )
}
