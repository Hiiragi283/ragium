package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.recipe.HTRecipeFinder
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.recipe.HTFinderRecipeCache
import hiiragi283.core.common.recipe.handler.HTItemOutputHandler
import hiiragi283.core.common.recipe.handler.HTSlotInputHandler
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.block.entity.component.HTRecipeComponent
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import kotlin.jvm.optionals.getOrNull

class HTFurnaceBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.ELECTRIC_FURNACE, pos, state) {
    private lateinit var inputSlot: HTBasicItemSlot
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    //    Processing    //

    override fun createRecipeComponent(): HTRecipeComponent<*, *> =
        object : HTRecipeComponent<SingleRecipeInput, AbstractCookingRecipe>(this) {
            private val cache: HTFinderRecipeCache<SingleRecipeInput, SmeltingRecipe> = HTFinderRecipeCache(
                HTRecipeFinder.Vanilla {
                    input: SingleRecipeInput,
                    level: Level,
                    holder: RecipeHolder<SmeltingRecipe>?,
                    ->
                    level.recipeManager.getRecipeFor(RecipeType.SMELTING, input, level, holder).getOrNull()
                },
            )
            private val inputHandler: HTSlotInputHandler<HTItemResourceType> by lazy { HTSlotInputHandler(inputSlot) }
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

            override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput =
                SingleRecipeInput(inputHandler.getItemStack())

            override fun getMatchedRecipe(input: SingleRecipeInput, level: ServerLevel): AbstractCookingRecipe? =
                cache.getFirstRecipe(input, level)

            override fun getMaxProgress(recipe: AbstractCookingRecipe): Int =
                this@HTFurnaceBlockEntity.updateAndGetProgress(recipe.cookingTime)

            override fun getProgress(level: ServerLevel, pos: BlockPos): Int = this@HTFurnaceBlockEntity.battery.consume()

            override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: AbstractCookingRecipe): Boolean =
                outputHandler.canInsert(recipe.assemble(input, level.registryAccess()))
        }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.processor.electricFurnace
}
