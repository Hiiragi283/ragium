package hiiragi283.ragium.common.block.entity.processing

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.recipe.HTRecipeFinder
import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.fluid.getFluidStack
import hiiragi283.core.api.storage.fluid.insert
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.api.storage.item.insert
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.recipe.HTComplexRecipe
import hiiragi283.ragium.common.storge.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.RagiumFluidConfigType
import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState

abstract class HTAbstractComplexBlockEntity<RECIPE : HTComplexRecipe>(
    finder: HTRecipeFinder<HTRecipeInput, RECIPE>,
    type: HTDeferredBlockEntityType<*>,
    pos: BlockPos,
    state: BlockState,
) : HTProcessorBlockEntity.Cached<RECIPE>(finder, type, pos, state) {
    lateinit var inputTank: HTBasicFluidTank
        private set
    lateinit var outputTank: HTBasicFluidTank
        private set

    final override fun initializeFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        inputTank =
            builder.addSlot(HTSlotInfo.INPUT, HTVariableFluidTank.input(listener, getTankCapacity(RagiumFluidConfigType.FIRST_INPUT)))
        outputTank =
            builder.addSlot(HTSlotInfo.OUTPUT, HTVariableFluidTank.output(listener, getTankCapacity(RagiumFluidConfigType.FIRST_OUTPUT)))
    }

    lateinit var inputSlot: HTBasicItemSlot
        private set
    lateinit var outputSlot: HTBasicItemSlot
        private set

    final override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    //    Processing    //

    final override fun buildRecipeInput(builder: HTRecipeInput.Builder) {
        builder.items += inputSlot.getItemStack()
        builder.fluids += inputTank.getFluidStack()
    }

    final override fun shouldCheckRecipe(level: ServerLevel, pos: BlockPos): Boolean =
        outputSlot.getNeeded() > 0 || outputTank.getNeeded() > 0

    override fun getRecipeTime(recipe: RECIPE): Int = recipe.time

    final override fun canProgressRecipe(level: ServerLevel, input: HTRecipeInput, recipe: RECIPE): Boolean {
        val access: RegistryAccess = level.registryAccess()
        val bool1: Boolean = outputTank
            .insert(
                recipe.assembleFluid(input, access),
                HTStorageAction.SIMULATE,
                HTStorageAccess.INTERNAL,
            ).isEmpty
        val bool2: Boolean = outputSlot
            .insert(
                recipe.assemble(input, access),
                HTStorageAction.SIMULATE,
                HTStorageAccess.INTERNAL,
            ).isEmpty
        return bool1 && bool2
    }

    final override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTRecipeInput,
        recipe: RECIPE,
    ) {
        val access: RegistryAccess = level.registryAccess()
        // 実際にアウトプットに搬出する
        outputTank.insert(recipe.assembleFluid(input, access), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        outputSlot.insert(recipe.assemble(input, access), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // インプットを減らす
        recipe.ingredient.map(
            { inputSlot.extract(it.getRequiredAmount(), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL) },
            { inputTank.extract(it.getRequiredAmount(), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL) },
        )
        // SEを鳴らす
        playSound()
    }

    protected abstract fun playSound()
}
