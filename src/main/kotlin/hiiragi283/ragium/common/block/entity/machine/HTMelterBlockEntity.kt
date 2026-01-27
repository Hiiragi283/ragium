package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.recipe.handler.HTFluidOutputHandler
import hiiragi283.core.common.recipe.handler.HTSlotInputHandler
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.block.entity.component.HTEnergizedRecipeComponent
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.storge.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.HTMachineConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.config.RagiumFluidConfigType
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTMelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.MELTER, pos, state) {
    private lateinit var outputTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        outputTank =
            builder.addSlot(HTSlotInfo.OUTPUT, HTVariableFluidTank.output(listener, getTankCapacity(RagiumFluidConfigType.FIRST_OUTPUT)))
    }

    private lateinit var inputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.create(listener))
    }

    //    Processing    //

    override fun createRecipeComponent(): HTEnergizedRecipeComponent.Cached<SingleRecipeInput, HTMeltingRecipe> =
        object : HTEnergizedRecipeComponent.Cached<SingleRecipeInput, HTMeltingRecipe>(RagiumRecipeTypes.MELTING, this) {
            private val inputHandler: HTSlotInputHandler<HTItemResourceType> by lazy { HTSlotInputHandler(inputSlot) }
            private val outputHandler: HTFluidOutputHandler by lazy { HTFluidOutputHandler.single(outputTank) }

            override fun insertOutput(
                level: ServerLevel,
                pos: BlockPos,
                input: SingleRecipeInput,
                recipe: HTMeltingRecipe,
            ) {
                outputHandler.insert(recipe.getResultFluid(level.registryAccess()))
            }

            override fun extractInput(
                level: ServerLevel,
                pos: BlockPos,
                input: SingleRecipeInput,
                recipe: HTMeltingRecipe,
            ) {
                inputHandler.consume(recipe.ingredient.getRequiredAmount())
            }

            override fun applyEffect() {
                playSound(SoundEvents.BUCKET_EMPTY_LAVA)
            }

            override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput =
                SingleRecipeInput(inputHandler.getItemStack())

            override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: HTMeltingRecipe): Boolean =
                outputHandler.canInsert(recipe.getResultFluid(level.registryAccess()))
        }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.processor.melter
}
