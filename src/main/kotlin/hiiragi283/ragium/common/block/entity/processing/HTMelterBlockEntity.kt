package hiiragi283.ragium.common.block.entity.processing

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.fluid.getFluidStack
import hiiragi283.core.api.storage.fluid.insert
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.api.storage.item.insert
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.common.storage.item.HTOutputItemSlot
import hiiragi283.core.util.HTItemDropHelper
import hiiragi283.core.util.HTStackSlotHelper
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
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack

class HTMelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity.Cached<HTMeltingRecipe>(RagiumRecipeTypes.MELTING, RagiumBlockEntityTypes.MELTER, pos, state) {
    lateinit var outputTank: HTBasicFluidTank
        private set

    override fun initializeFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        // output
        outputTank = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTVariableFluidTank.output(listener, getTankCapacity(RagiumFluidConfigType.FIRST_OUTPUT)),
        )
    }

    lateinit var inputSlot: HTBasicItemSlot
        private set
    lateinit var remainderSlot: HTBasicItemSlot
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.create(listener))
        // output
        remainderSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTOutputItemSlot(listener))
    }

    //    Save & Load    //

    override fun initReducedUpdateTag(output: HTValueOutput) {
        super.initReducedUpdateTag(output)
        output.store(HTConst.FLUID, VanillaBiCodecs.FLUID_STACK, outputTank.getFluidStack())
    }

    override fun handleUpdateTag(input: HTValueInput) {
        super.handleUpdateTag(input)
        (input.read(HTConst.FLUID, VanillaBiCodecs.FLUID_STACK) ?: FluidStack.EMPTY).let(outputTank::setStack)
    }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.processor.melter

    //    Ticking    //

    override fun shouldCheckRecipe(level: ServerLevel, pos: BlockPos): Boolean = outputTank.getNeeded() > 0

    override fun getRecipeTime(recipe: HTMeltingRecipe): Int = recipe.time

    override fun buildRecipeInput(builder: HTRecipeInput.Builder) {
        builder.items += inputSlot.getItemStack()
    }

    // アウトプットに搬出できるか判定する
    override fun canProgressRecipe(level: ServerLevel, input: HTRecipeInput, recipe: HTMeltingRecipe): Boolean =
        outputTank.insert(recipe.assembleFluid(input, level.registryAccess()), HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL).isEmpty

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTRecipeInput,
        recipe: HTMeltingRecipe,
    ) {
        // 実際にアウトプットに搬出する
        outputTank.insert(recipe.assembleFluid(input, level.registryAccess()), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // インプットを減らす, 返却物がある場合は移動
        HTStackSlotHelper.shrinkItemStack(
            inputSlot,
            { resource: HTItemResourceType -> resource.toStack().craftingRemainingItem },
            { stack: ItemStack ->
                val remainder: ItemStack = remainderSlot.insert(stack, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                HTItemDropHelper.dropStackAt(level, pos, remainder)
            },
            recipe.ingredient.getRequiredAmount(),
            HTStorageAction.EXECUTE,
        )
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.WITCH_DRINK, SoundSource.BLOCKS, 1f, 0.5f)
    }
}
