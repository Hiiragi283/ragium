package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTUniversalRecipeInput
import hiiragi283.ragium.api.storage.item.HTFilteredItemHandler
import hiiragi283.ragium.api.storage.item.HTItemFilter
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.inventory.HTFormingPressMenu
import hiiragi283.ragium.common.recipe.HTPressingRecipe
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

class HTFormingPressBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.FORMING_PRESS, pos, state) {
    override val inventory: HTItemHandler = HTItemStackHandler(6, this::setChanged)
    override val energyUsage: Int get() = RagiumConfig.COMMON.advancedMachineEnergyUsage.get()

    //    Ticking    //

    private val recipeCache: HTRecipeCache<HTUniversalRecipeInput, HTPressingRecipe> =
        HTRecipeCache.simple(RagiumRecipeTypes.PRESSING.get())

    override fun onServerTick(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: IEnergyStorage,
    ): TriState {
        // インプットに一致するレシピを探索する
        val input: HTUniversalRecipeInput =
            HTUniversalRecipeInput.fromItems(inventory.getStackInSlot(0), inventory.getStackInSlot(1))
        val recipe: HTPressingRecipe = recipeCache.getFirstRecipe(input, level) ?: return TriState.FALSE
        // エネルギーを消費できるか判定する
        if (network.extractEnergy(requiredEnergy, true) != requiredEnergy) return TriState.DEFAULT
        // アウトプットに搬出できるか判定する
        val fixedInventory = HTFilteredItemHandler(inventory, recipeFilter)
        if (!ItemHandlerHelper.insertItem(fixedInventory, recipe.output.get(), true).isEmpty) {
            return TriState.FALSE
        }
        // 実際にアウトプットに搬出する
        ItemHandlerHelper.insertItem(fixedInventory, recipe.output.getChancedStack(level.random), false)
        // インプットを減らす
        inventory.consumeStackInSlot(0, 1)
        inventory.consumeStackInSlot(1, 1)
        // エネルギーを減らす
        network.extractEnergy(requiredEnergy, false)
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS)
        return TriState.DEFAULT
    }

    private val recipeFilter: HTItemFilter = object : HTItemFilter {
        override fun canInsert(handler: IItemHandler, slot: Int, stack: ItemStack): Boolean = slot >= 2

        override fun canExtract(handler: IItemHandler, slot: Int, amount: Int): Boolean = false
    }

    override fun getItemHandler(direction: Direction?): IItemHandler? = HTFilteredItemHandler(
        inventory,
        object : HTItemFilter {
            override fun canInsert(handler: IItemHandler, slot: Int, stack: ItemStack): Boolean = slot <= 1

            override fun canExtract(handler: IItemHandler, slot: Int, amount: Int): Boolean = slot >= 2
        },
    )

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTFormingPressMenu? = HTFormingPressMenu(
        containerId,
        playerInventory,
        blockPos,
        createDefinition(inventory),
    )
}
