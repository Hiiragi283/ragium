package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.common.recipe.HTVanillaRecipeTypes
import hiiragi283.core.common.recipe.handler.HTItemInputHandler
import hiiragi283.core.common.recipe.handler.HTItemOutputHandler
import hiiragi283.ragium.common.block.entity.component.HTEnergizedRecipeComponent
import hiiragi283.ragium.common.block.entity.component.HTRecipeComponent
import hiiragi283.ragium.common.block.entity.machine.base.HTItemToItemBlockEntity
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.level.block.state.BlockState

class HTFurnaceBlockEntity(pos: BlockPos, state: BlockState) :
    HTItemToItemBlockEntity(RagiumBlockEntityTypes.ELECTRIC_FURNACE, pos, state) {
    override fun createRecipeComponent(): HTRecipeComponent<*, *> = SmeltingComponent()

    private inner class SmeltingComponent :
        HTEnergizedRecipeComponent<SingleRecipeInput, AbstractCookingRecipe>(this, AbstractCookingRecipe::getCookingTime) {
        private val cache: HTRecipeCache<SingleRecipeInput, SmeltingRecipe> = HTVanillaRecipeTypes.SMELTING.createCache()
        private val inputHandler: HTItemInputHandler by lazy { HTItemInputHandler(inputSlot) }
        private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }

        override fun insertOutput(
            level: ServerLevel,
            pos: BlockPos,
            input: SingleRecipeInput,
            recipe: AbstractCookingRecipe,
        ) {
            outputHandler.insert(recipe.assemble(input, level.registryAccess()))
        }

        override fun extractInput(
            level: ServerLevel,
            pos: BlockPos,
            input: SingleRecipeInput,
            recipe: AbstractCookingRecipe,
        ) {
            inputHandler.consume(1)
        }

        override fun applyEffect() {
            playSound(SoundEvents.FIRE_EXTINGUISH)
        }

        override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput? = createInput(inputHandler)

        override fun getMatchedRecipe(input: SingleRecipeInput, level: ServerLevel): AbstractCookingRecipe? =
            cache.getFirstRecipe(input, level)

        override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: AbstractCookingRecipe): Boolean =
            outputHandler.canInsert(recipe.assemble(input, level.registryAccess()))
    }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.machine.electricFurnace
}
