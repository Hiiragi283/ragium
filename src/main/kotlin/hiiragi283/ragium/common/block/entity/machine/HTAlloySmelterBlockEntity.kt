package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTUniversalRecipeInput
import hiiragi283.ragium.api.storage.item.HTFilteredItemHandler
import hiiragi283.ragium.api.storage.item.HTItemFilter
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.inventory.HTAlloySmelterMenu
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
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

class HTAlloySmelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.ALLOY_SMELTER, pos, state) {
    override val inventory: HTItemHandler = HTItemStackHandler(6, this::setChanged)
    override val energyUsage: Int get() = RagiumConfig.COMMON.advancedMachineEnergyUsage.get()

    //    Ticking    //

    private val recipeCache: HTRecipeCache<HTUniversalRecipeInput, HTAlloyingRecipe> =
        HTRecipeCache.simple(RagiumRecipeTypes.ALLOYING.get())

    override fun onServerTick(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: IEnergyStorage,
    ): TriState {
        // インプットに一致するレシピを探索する
        val input: HTUniversalRecipeInput = HTUniversalRecipeInput.fromItems(inventory.getStackInSlot(0), inventory.getStackInSlot(1))
        val recipe: HTAlloyingRecipe = recipeCache.getFirstRecipe(input, level) ?: return TriState.FALSE
        // エネルギーを消費できるか判定する
        if (network.extractEnergy(requiredEnergy, true) != requiredEnergy) return TriState.FALSE
        // アウトプットに搬出できるか判定する
        for (output: HTItemOutput in recipe.outputs) {
            if (!ItemHandlerHelper.insertItem(inventory, output.get(), true).isEmpty) {
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
            ItemHandlerHelper.insertItem(inventory, output.getChancedStack(level.random), false)
        }
        // エネルギーを減らす
        network.extractEnergy(requiredEnergy, false)
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

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTAlloySmelterMenu = HTAlloySmelterMenu(
        containerId,
        playerInventory,
        blockPos,
        createDefinition(inventory),
    )
}
