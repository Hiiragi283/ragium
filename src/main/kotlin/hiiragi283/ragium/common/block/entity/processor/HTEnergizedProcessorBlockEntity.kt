package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTRecipeFinder
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.recipe.manager.HTFinderRecipeCache
import hiiragi283.ragium.common.storage.energy.battery.HTMachineEnergyBattery
import hiiragi283.ragium.common.storage.holder.HTBasicEnergyBatteryHolder
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

abstract class HTEnergizedProcessorBlockEntity<INPUT : Any, RECIPE : Any>(blockHolder: Holder<Block>, pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<INPUT, RECIPE>(blockHolder, pos, state) {
    lateinit var battery: HTMachineEnergyBattery<*>
        protected set

    final override fun initializeEnergyBattery(builder: HTBasicEnergyBatteryHolder.Builder, listener: HTContentListener) {
        battery = builder.addSlot(HTSlotInfo.INPUT, HTMachineEnergyBattery.input(listener, this))
    }

    protected fun getModifiedEnergy(base: Int): Int = getComponentTier()?.modifyProcessorRate(base) ?: base

    //    Ticking    //

    final override fun getRequiredEnergy(recipe: RECIPE): Int = getModifiedEnergy(battery.currentEnergyPerTick * getRecipeTime(recipe))

    final override fun gatherEnergy(level: ServerLevel, pos: BlockPos): Int = battery.consume()

    //    Cached    //

    /**
     * レシピのキャッシュを保持する[HTEnergizedProcessorBlockEntity]の拡張クラス
     */
    abstract class Cached<INPUT : RecipeInput, RECIPE : Recipe<INPUT>>(
        private val recipeCache: HTRecipeCache<INPUT, RECIPE>,
        blockHolder: Holder<Block>,
        pos: BlockPos,
        state: BlockState,
    ) : HTEnergizedProcessorBlockEntity<INPUT, RECIPE>(blockHolder, pos, state) {
        constructor(
            finder: HTRecipeFinder<INPUT, RECIPE>,
            blockHolder: Holder<Block>,
            pos: BlockPos,
            state: BlockState,
        ) : this(HTFinderRecipeCache(finder), blockHolder, pos, state)

        final override fun getMatchedRecipe(input: INPUT, level: ServerLevel): RECIPE? = recipeCache.getFirstRecipe(input, level)
    }
}
