package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.recipe.HTUniversalRecipeInput
import hiiragi283.ragium.api.storage.item.HTFilteredItemHandler
import hiiragi283.ragium.api.storage.item.HTItemFilter
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.inventory.HTSingleProcessMenu
import hiiragi283.ragium.common.recipe.HTInfusingRecipeOld
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
import net.minecraft.world.level.block.EnchantingTableBlock
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandler

class HTInfuserBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<HTUniversalRecipeInput, HTInfusingRecipeOld>(
        TODO(),
        RagiumBlockEntityTypes.INFUSER,
        pos,
        state,
    ) {
    override val inventory: HTItemHandler = HTItemStackHandler(2, this::setChanged)
    override val energyUsage: Int = 0

    //    Ticking    //

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTUniversalRecipeInput =
        HTUniversalRecipeInput.fromItems(inventory.getStackInSlot(0))

    override fun canProgressRecipe(level: ServerLevel, input: HTUniversalRecipeInput, recipe: HTInfusingRecipeOld): Boolean {
        // 周囲のエンチャントパワーを計算する
        val aroundCost: Float = EnchantingTableBlock.BOOKSHELF_OFFSETS
            .map { posIn: BlockPos ->
                if (EnchantingTableBlock.isValidBookShelf(level, blockPos, posIn)) {
                    val posTo: BlockPos = blockPos.offset(posIn)
                    level.getBlockState(posTo).getEnchantPowerBonus(level, posTo)
                } else {
                    0f
                }
            }.sum()
        if (aroundCost < recipe.cost) return false
        // アウトプットに搬出できるか判定する
        val output: ItemStack = recipe.assemble(input, level.registryAccess())
        return insertToOutput(1..1, output, true).isEmpty
    }

    override fun serverTickPost(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTUniversalRecipeInput,
        recipe: HTInfusingRecipeOld,
    ) {
        // 実際にアウトプットに搬出する
        val output: ItemStack = recipe.assemble(input, level.registryAccess())
        insertToOutput(1..1, output, false)
        // インプットを減らす
        inventory.consumeStackInSlot(0, 1, false)
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS)
    }

    override fun getItemHandler(direction: Direction?): HTFilteredItemHandler = HTFilteredItemHandler(
        inventory,
        object : HTItemFilter {
            override fun canInsert(handler: IItemHandler, slot: Int, stack: ItemStack): Boolean = slot == 0

            override fun canExtract(handler: IItemHandler, slot: Int, amount: Int): Boolean = slot != 0
        },
    )

    //    Menu    //

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTSingleProcessMenu = HTSingleProcessMenu(
        RagiumMenuTypes.INFUSER,
        containerId,
        playerInventory,
        blockPos,
        createDefinition(inventory),
    )
}
