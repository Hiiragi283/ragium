package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTUniversalRecipeInput
import hiiragi283.ragium.api.storage.item.HTFilteredItemHandler
import hiiragi283.ragium.api.storage.item.HTItemFilter
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.inventory.HTSingleProcessMenu
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
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemHandlerHelper

class HTExtractorBlockEntity(pos: BlockPos, state: BlockState) : HTMachineBlockEntity(RagiumBlockEntityTypes.EXTRACTOR, pos, state) {
    override val inventory = HTItemStackHandler(5, this::setChanged)
    override val energyUsage: Int get() = RagiumConfig.COMMON.basicMachineEnergyUsage.get()

    //    Ticking    //

    private val recipeCache: HTRecipeCache<HTUniversalRecipeInput, HTExtractingRecipe> =
        HTRecipeCache.simple(RagiumRecipeTypes.EXTRACTING.get())

    override fun onServerTick(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: IEnergyStorage,
    ): TriState {
        // インプットに一致するレシピを探索する
        val input: HTUniversalRecipeInput = HTUniversalRecipeInput.fromItems(inventory.getStackInSlot(0))
        val recipe: HTExtractingRecipe = recipeCache.getFirstRecipe(input, level) ?: return TriState.FALSE
        // エネルギーを消費できるか判定する
        if (network.extractEnergy(requiredEnergy, true) != requiredEnergy) return TriState.DEFAULT
        // アウトプットに搬出できるか判定する
        if (!ItemHandlerHelper.insertItem(inventory, recipe.output.get(), true).isEmpty) {
            return TriState.FALSE
        }
        // 実際にアウトプットに搬出する
        ItemHandlerHelper.insertItem(inventory, recipe.output.getChancedStack(level.random), false)
        // インプットを減らす
        inventory.consumeStackInSlot(0, recipe.ingredient.count())
        // エネルギーを減らす
        network.extractEnergy(requiredEnergy, false)
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.SLIME_BLOCK_BREAK, SoundSource.BLOCKS)
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

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTSingleProcessMenu = HTSingleProcessMenu(
        RagiumMenuTypes.EXTRACTOR,
        containerId,
        playerInventory,
        blockPos,
        createDefinition(inventory),
    )
}
