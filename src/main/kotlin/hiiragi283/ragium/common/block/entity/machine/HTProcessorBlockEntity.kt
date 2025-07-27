package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.item.HTFilteredItemHandler
import hiiragi283.ragium.api.storage.item.HTItemFilter
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemHandlerHelper

abstract class HTProcessorBlockEntity<I : RecipeInput, R : Recipe<I>>(
    recipeType: RecipeType<R>,
    type: HTDeferredBlockEntityType<*>,
    pos: BlockPos,
    state: BlockState,
) : HTMachineBlockEntity(type, pos, state) {
    private val recipeCache: HTRecipeCache<I, R> = HTRecipeCache(recipeType)

    final override fun onServerTick(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: IEnergyStorage,
    ): TriState {
        // インプットに一致するレシピを探索する
        val input: I = createRecipeInput()
        val lastRecipe: ResourceLocation? = recipeCache.lastRecipe
        val recipe: R = recipeCache.getFirstRecipe(input, level) ?: return resetProgress()
        // レシピの進行度を確認する
        if (recipeCache.lastRecipe != lastRecipe) {
            resetProgress()
        }
        // エネルギーを消費しようとする
        if (network.extractEnergy(energyUsage, true) != energyUsage) return TriState.DEFAULT
        network.extractEnergy(energyUsage, false)
        // 進行度が最大でなければスキップ
        currentTicks++
        if (currentTicks < maxTicks) return TriState.DEFAULT
        currentTicks = 0
        return completeProcess(level, pos, state, network, input, recipe)
    }

    protected abstract fun createRecipeInput(): I

    protected abstract fun completeProcess(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: IEnergyStorage,
        input: I,
        recipe: R,
    ): TriState

    protected fun resetProgress(): TriState {
        this.currentTicks = 0
        return TriState.FALSE
    }

    protected fun insertToOutput(range: IntRange, output: ItemStack, simulate: Boolean): ItemStack {
        val filter: HTItemFilter = object : HTItemFilter {
            override fun canInsert(handler: IItemHandler, slot: Int, stack: ItemStack): Boolean = slot in range

            override fun canExtract(handler: IItemHandler, slot: Int, amount: Int): Boolean = false
        }
        val fixedInventory = HTFilteredItemHandler(inventory, filter)
        return ItemHandlerHelper.insertItem(fixedInventory, output, simulate)
    }
}
