package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.storage.item.HTFilteredItemHandler
import hiiragi283.ragium.api.storage.item.HTItemFilter
import hiiragi283.ragium.common.inventory.HTCrusherMenu
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandler

class HTCrusherBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<SingleRecipeInput, HTItemToChancedItemRecipe>(
        RagiumRecipeTypes.CRUSHING.get(),
        RagiumBlockEntityTypes.CRUSHER,
        pos,
        state,
    ) {
    override val inventory = HTItemStackHandler(5, this::setChanged)
    override val energyUsage: Int get() = RagiumAPI.getConfig().getBasicMachineEnergyUsage()

    //    Ticking    //

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput = SingleRecipeInput(inventory.getStackInSlot(0))

    override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: HTItemToChancedItemRecipe): Boolean {
        // アウトプットに搬出できるか判定する
        for (result: HTItemResult in recipe.results) {
            if (!insertToOutput(1..4, result.get(), true).isEmpty) {
                return false
            }
        }
        return true
    }

    override fun serverTickPost(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: HTItemToChancedItemRecipe,
    ) {
        // 実際にアウトプットに搬出する
        for (index: Int in recipe.results.indices) {
            insertToOutput(1..4, recipe.getChancedResult(index), false)
        }
        // インプットを減らす
        inventory.consumeStackInSlot(0, recipe.ingredient, false)
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.STONE_BREAK, SoundSource.BLOCKS)
    }

    override fun getItemHandler(direction: Direction?): HTFilteredItemHandler = HTFilteredItemHandler(
        inventory,
        object : HTItemFilter {
            override fun canInsert(handler: IItemHandler, slot: Int, stack: ItemStack): Boolean = slot == 0

            override fun canExtract(handler: IItemHandler, slot: Int, amount: Int): Boolean = slot != 0
        },
    )

    //    Menu    //

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTCrusherMenu = HTCrusherMenu(
        containerId,
        playerInventory,
        blockPos,
        createDefinition(inventory),
    )
}
