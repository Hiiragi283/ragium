package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.recipe.HTUniversalRecipeInput
import hiiragi283.ragium.api.storage.item.HTFilteredItemHandler
import hiiragi283.ragium.api.storage.item.HTItemFilter
import hiiragi283.ragium.common.inventory.HTDecomposeProcessMenu
import hiiragi283.ragium.common.recipe.HTExtractingRecipe
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

class HTExtractorBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<HTUniversalRecipeInput, HTExtractingRecipe>(
        RagiumRecipeTypes.EXTRACTING.get(),
        RagiumBlockEntityTypes.EXTRACTOR,
        pos,
        state,
    ) {
    override val inventory = HTItemStackHandler(5, this::setChanged)
    override val energyUsage: Int get() = RagiumConfig.COMMON.basicMachineEnergyUsage.get()

    //    Ticking    //

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTUniversalRecipeInput =
        HTUniversalRecipeInput.fromItems(inventory.getStackInSlot(0))

    override fun canProgressRecipe(level: ServerLevel, input: HTUniversalRecipeInput, recipe: HTExtractingRecipe): Boolean {
        // アウトプットに搬出できるか判定する
        if (!insertToOutput(1..4, recipe.output.get(), true).isEmpty) {
            return false
        }
        return true
    }

    override fun serverTickPost(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        recipe: HTExtractingRecipe,
    ) {
        // 実際にアウトプットに搬出する
        insertToOutput(1..4, recipe.output.getChancedStack(level.random), false)
        // インプットを減らす
        inventory.consumeStackInSlot(0, recipe.ingredient.count(), false)
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.SLIME_BLOCK_BREAK, SoundSource.BLOCKS)
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
        RagiumMenuTypes.EXTRACTOR,
        containerId,
        playerInventory,
        blockPos,
        createDefinition(inventory),
    )
}
