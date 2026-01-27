package hiiragi283.ragium.common.block.entity.enchant

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.data.recipe.HTIngredientCreator
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.item.enchantment.buildEnchantments
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.registry.holderSetOrNull
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.getFluidStack
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.gui.sync.HTBoolSyncSlot
import hiiragi283.core.common.recipe.HTFinderRecipeCache
import hiiragi283.core.common.recipe.handler.HTItemOutputHandler
import hiiragi283.core.common.recipe.handler.HTSlotInputHandler
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.setup.HCFluids
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.block.entity.component.HTEnchantingRecipeComponent
import hiiragi283.ragium.common.block.entity.component.HTRecipeComponent
import hiiragi283.ragium.common.recipe.HTEnchantingRecipe
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
import net.minecraft.core.HolderSet
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.EnchantmentTags
import net.minecraft.util.RandomSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.EnchantmentInstance
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.Tags
import org.apache.commons.lang3.math.Fraction

class HTEnchanterBlockEntity(pos: BlockPos, state: BlockState) : HTProcessorBlockEntity(RagiumBlockEntityTypes.ENCHANTER, pos, state) {
    private lateinit var expTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        expTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidTank.input(
                listener,
                getTankCapacity(RagiumFluidConfigType.FIRST_INPUT),
                canInsert = HCFluids.EXPERIENCE::isOf,
            ),
        )
    }

    private lateinit var leftSlot: HTBasicItemSlot
    private lateinit var rightSlot: HTBasicItemSlot
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        leftSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTBasicItemSlot.input(
                listener,
                limit = 1,
                canInsert = { it.toStack().isEnchantable },
            ),
        )
        rightSlot = builder.addSlot(HTSlotInfo.EXTRA_INPUT, HTBasicItemSlot.input(listener))

        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    private var isRandom: Boolean = false

    override fun addMenuTrackers(holder: HTWidgetHolder) {
        super.addMenuTrackers(holder)
        holder.track(HTBoolSyncSlot.create(::isRandom))
    }

    //    Processing    //

    override fun createRecipeComponent(): HTRecipeComponent<*, *> = RecipeComponent()

    inner class RecipeComponent : HTEnchantingRecipeComponent<HTEnchantingRecipe.Input, HTEnchantingRecipe>(this) {
        private val cache: HTRecipeCache<HTEnchantingRecipe.Input, HTEnchantingRecipe> = HTFinderRecipeCache(RagiumRecipeTypes.ENCHANTING)
        private var currentEnch: List<EnchantmentInstance> = listOf()

        private val fluidInputHandler: HTSlotInputHandler<HTFluidResourceType> by lazy { HTSlotInputHandler(expTank) }
        private val leftInputHandler: HTSlotInputHandler<HTItemResourceType> by lazy { HTSlotInputHandler(leftSlot) }
        private val rightInputHandler: HTSlotInputHandler<HTItemResourceType> by lazy { HTSlotInputHandler(rightSlot) }
        private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }

        override fun insertOutput(
            level: ServerLevel,
            pos: BlockPos,
            input: HTEnchantingRecipe.Input,
            recipe: HTEnchantingRecipe,
        ) {
            outputHandler.insert(recipe.assemble(input, level.registryAccess()))
            currentEnch = listOf()
        }

        override fun extractInput(
            level: ServerLevel,
            pos: BlockPos,
            input: HTEnchantingRecipe.Input,
            recipe: HTEnchantingRecipe,
        ) {
            fluidInputHandler.consume(recipe.expIngredient)
            leftInputHandler.consume(1)
            rightInputHandler.consume(recipe.ingredient)
        }

        override fun applyEffect() {
            playSound(SoundEvents.ENCHANTMENT_TABLE_USE)
        }

        override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTEnchantingRecipe.Input =
            HTEnchantingRecipe.Input(fluidInputHandler.getFluidStack(), leftInputHandler.getItemStack(), rightInputHandler.getItemStack())

        /**
         * @see net.minecraft.world.inventory.EnchantmentMenu
         */
        override fun getMatchedRecipe(input: HTEnchantingRecipe.Input, level: ServerLevel): HTEnchantingRecipe? {
            if (isRandom) {
                val stack: ItemStack = input.left
                val cost: Int = EnchantmentHelper.getEnchantmentCost(
                    level.random,
                    2,
                    getProgress(level, blockPos),
                    stack,
                )
                if (currentEnch.isEmpty()) {
                    currentEnch = getEnchantmentList(level, stack, cost)
                }
                if (currentEnch.isEmpty()) return null
                val recipe = HTEnchantingRecipe(
                    HTIngredientCreator.create(Tags.Items.ENCHANTING_FUELS, 3),
                    buildEnchantments {
                        for (instance: EnchantmentInstance in currentEnch) {
                            set(instance.enchantment, instance.level)
                        }
                    },
                    20 * 5,
                    Fraction.ZERO,
                )
                if (!recipe.matches(input, level)) {
                    currentEnch = listOf()
                    return null
                }
                return recipe
            } else {
                return cache.getFirstRecipe(input, level)
            }
        }

        /**
         * @see net.minecraft.world.inventory.EnchantmentMenu.getEnchantmentList
         */
        private fun getEnchantmentList(level: ServerLevel, stack: ItemStack, cost: Int): List<EnchantmentInstance> {
            val holderSet: HolderSet<Enchantment> =
                level.registryAccess().holderSetOrNull(EnchantmentTags.IN_ENCHANTING_TABLE) ?: return emptyList()
            val random: RandomSource = level.random
            val enchantments: MutableList<EnchantmentInstance> = EnchantmentHelper.selectEnchantment(
                random,
                stack,
                cost,
                holderSet.stream(),
            )
            if (stack.`is`(Items.BOOK) && !enchantments.isEmpty()) {
                enchantments.removeAt(random.nextInt(enchantments.size))
            }
            return enchantments
        }

        override fun canProgressRecipe(level: ServerLevel, input: HTEnchantingRecipe.Input, recipe: HTEnchantingRecipe): Boolean =
            outputHandler.canInsert(recipe.assemble(input, level.registryAccess()))
    }

    override fun getConfig(): HTMachineConfig = RagiumConfig.COMMON.device.enchanter
}
