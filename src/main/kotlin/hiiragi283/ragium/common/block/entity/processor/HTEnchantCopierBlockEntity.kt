package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.function.andThen
import hiiragi283.ragium.api.function.compose
import hiiragi283.ragium.api.function.negate
import hiiragi283.ragium.api.item.component.filter
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.util.HTExperienceHelper
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponents
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.block.state.BlockState

class HTEnchantCopierBlockEntity(pos: BlockPos, state: BlockState) :
    HTEnergizedProcessorBlockEntity<HTMultiRecipeInput, HTEnchantCopierBlockEntity.EnchantingRecipe>(
        RagiumBlocks.ENCHANT_COPIER,
        pos,
        state,
    ) {
    lateinit var inputTank: HTVariableFluidStackTank
        private set

    override fun initializeFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        // input
        inputTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidStackTank.input(
                listener,
                RagiumConfig.COMMON.extractorTankCapacity,
                RagiumFluidContents.EXPERIENCE::isOf,
            ),
        )
    }

    lateinit var inputSlot: HTItemStackSlot
        private set
    lateinit var catalystSlot: HTItemStackSlot
        private set
    lateinit var outputSlot: HTItemStackSlot
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        // エンチャントされていないツールのみ対応する
        inputSlot = singleInput(
            builder,
            listener,
            EnchantmentHelper::getEnchantmentsForCrafting
                .compose(ImmutableItemStack::unwrap)
                .andThen(ItemEnchantments::isEmpty),
        )
        // catalyst
        catalystSlot = singleCatalyst(builder, listener, ::getStoredEnchantments.andThen(ItemEnchantments::isEmpty).negate())
        // output
        outputSlot = singleOutput(builder, listener)
    }

    private fun getStoredEnchantments(stack: ImmutableItemStack): ItemEnchantments =
        stack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY)

    override fun shouldCheckRecipe(level: ServerLevel, pos: BlockPos): Boolean = outputSlot.getNeeded() > 0

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTMultiRecipeInput? = HTMultiRecipeInput.create {
        items += inputSlot.getStack()
        items += catalystSlot.getStack()
    }

    override fun getMatchedRecipe(input: HTMultiRecipeInput, level: ServerLevel): EnchantingRecipe? {
        val tool: ImmutableItemStack = input.items[0] ?: return null
        val book: ImmutableItemStack = input.items[1] ?: return null
        val enchantments: ItemEnchantments = book
            .let(::getStoredEnchantments)
            .filter(tool.unwrap())
            .takeUnless(ItemEnchantments::isEmpty)
            ?: return null
        return EnchantingRecipe(tool, enchantments)
    }

    override fun canProgressRecipe(level: ServerLevel, input: HTMultiRecipeInput, recipe: EnchantingRecipe): Boolean {
        val bool1: Boolean = outputSlot.insert(recipe.applyEnchantment(), HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL) == null
        val required: Int = recipe.getRequiredAmount()
        val bool2: Boolean = inputTank.extract(required, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL)?.amount() == required
        return bool1 && bool2
    }

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTMultiRecipeInput,
        recipe: EnchantingRecipe,
    ) {
        // 実際にアウトプットに搬出する
        outputSlot.insert(recipe.applyEnchantment(), HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // 実際にインプットを減らす
        HTStackSlotHelper.shrinkStack(inputSlot, { 1 }, HTStorageAction.EXECUTE)
        HTStackSlotHelper.shrinkStack(inputTank, { recipe.getRequiredAmount() }, HTStorageAction.EXECUTE)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1f, 1f)
    }

    @JvmRecord
    data class EnchantingRecipe(val tool: ImmutableItemStack, val enchantments: ItemEnchantments) {
        fun applyEnchantment(): ImmutableItemStack = tool.plus(EnchantmentHelper.getComponentType(tool.unwrap()), enchantments)

        fun getExperienceCost(): Int = HTExperienceHelper.getTotalMaxCost(enchantments)

        fun getRequiredAmount(): Int = HTExperienceHelper.fluidAmountFromExp(getExperienceCost())
    }
}
