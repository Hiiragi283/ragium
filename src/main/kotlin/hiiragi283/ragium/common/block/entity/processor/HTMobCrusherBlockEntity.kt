package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.block.attribute.getFluidAttribute
import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.registry.vanillaId
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.storage.item.getItemStack
import hiiragi283.ragium.api.storage.item.toRecipeInput
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.fluid.tank.HTFluidStackTank
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.storage.item.slot.HTOutputItemStackSlot
import hiiragi283.ragium.common.util.HTExperienceHelper
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SpawnEggItem
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.common.util.FakePlayerFactory
import net.neoforged.neoforge.event.EventHooks

class HTMobCrusherBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<SingleRecipeInput, HTMobCrusherBlockEntity.MobLootRecipe>(RagiumBlocks.MOB_CRUSHER, pos, state) {
    lateinit var outputTank: HTFluidStackTank
        private set

    override fun initializeFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        // output
        outputTank = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTVariableFluidStackTank.output(listener, blockHolder.getFluidAttribute().getOutputTank()),
        )
    }

    lateinit var inputSlot: HTItemStackSlot
        private set
    lateinit var toolSlot: HTItemStackSlot
        private set
    lateinit var outputSlots: List<HTItemStackSlot>
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        inputSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTItemStackSlot
                .input(
                    listener,
                    HTSlotHelper.getSlotPosX(1.5),
                    HTSlotHelper.getSlotPosY(2),
                    canInsert = { stack: ImmutableItemStack -> stack.value() is SpawnEggItem },
                ).setSlotBackground(RagiumConst.GUI_ATLAS, vanillaId("slot/spawn_egg")),
        )
        toolSlot = builder.addSlot(
            HTSlotInfo.CATALYST,
            HTItemStackSlot
                .create(
                    listener,
                    HTSlotHelper.getSlotPosX(1.5),
                    HTSlotHelper.getSlotPosY(0),
                    canExtract = HTPredicates.manualOnly(),
                    canInsert = HTPredicates.manualOnly(),
                ).setSlotBackground(RagiumConst.BLOCK_ATLAS, vanillaId("items/empty_slot_sword")),
        )
        // outputs
        outputSlots = (0..<9).map { i: Int ->
            builder.addSlot(
                HTSlotInfo.OUTPUT,
                HTOutputItemStackSlot.create(
                    listener,
                    HTSlotHelper.getSlotPosX(3 + i % 3),
                    HTSlotHelper.getSlotPosY(i / 3),
                ),
            )
        }
    }

    private var fakePlayer: ServerPlayer? = null

    override fun onUpdateLevel(level: Level, pos: BlockPos) {
        super.onUpdateLevel(level, pos)
        if (level is ServerLevel) {
            fakePlayer = FakePlayerFactory.get(level, getOwnerProfile())
        }
    }

    //    Ticking    //

    override fun shouldCheckRecipe(level: ServerLevel, pos: BlockPos): Boolean =
        outputSlots.any { slot: HTItemStackSlot -> slot.getNeeded() > 0 }

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput? = inputSlot.toRecipeInput()

    /**
     * @see net.minecraft.server.commands.LootCommand.dropKillLoot
     */
    override fun getMatchedRecipe(input: SingleRecipeInput, level: ServerLevel): MobLootRecipe? {
        val stack: ItemStack = input.item()
        val entityType: EntityType<*> = (stack.item as? SpawnEggItem)?.getType(stack) ?: return null
        val fakeEntity: LivingEntity = entityType.create(level) as? LivingEntity ?: return null

        fakePlayer?.setItemInHand(InteractionHand.MAIN_HAND, toolSlot.getItemStack())
        val damageSource: DamageSource =
            fakePlayer?.let(level.damageSources()::playerAttack) ?: level.damageSources().magic()
        val lootTable: LootTable = level.server.reloadableRegistries().getLootTable(fakeEntity.lootTable)
        val lootParams: LootParams = LootParams
            .Builder(level)
            .withParameter(LootContextParams.THIS_ENTITY, fakeEntity)
            .withParameter(LootContextParams.ORIGIN, Vec3.atLowerCornerOf(blockPos))
            .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
            .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, damageSource.directEntity)
            .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, damageSource.entity)
            .withOptionalParameter(LootContextParams.LAST_DAMAGE_PLAYER, fakePlayer)
            .withLuck(fakePlayer?.luck ?: 0f)
            .create(LootContextParamSets.ENTITY)
        return MobLootRecipe(
            fakeEntity,
            lootTable.getRandomItems(lootParams, fakeEntity.lootTableSeed).mapNotNull(ItemStack::toImmutable),
        )
    }

    override fun getRecipeTime(recipe: MobLootRecipe): Int = super.getRecipeTime(recipe) / 4

    override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: MobLootRecipe): Boolean {
        // アウトプットに搬出できるか判定する
        for (stackIn: ImmutableItemStack in recipe.drops) {
            if (HTStackSlotHelper.insertStacks(outputSlots, stackIn, HTStorageAction.SIMULATE) != null) {
                return false
            }
        }

        val expStack: ImmutableFluidStack = recipe.getExpStack(level, getOwnerPlayer(level)) ?: return true
        return outputTank.insert(expStack, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL) == null
    }

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: MobLootRecipe,
    ) {
        val (_, drops: List<ImmutableItemStack>) = recipe
        // 実際にアウトプットに搬出する
        for (stackIn: ImmutableItemStack in drops) {
            HTStackSlotHelper.insertStacks(outputSlots, stackIn, HTStorageAction.EXECUTE)
        }
        recipe.getExpStack(level, fakePlayer)?.let { stack: ImmutableFluidStack ->
            outputTank.insert(stack, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        }
        // インプットを減らす
        HTStackSlotHelper.shrinkStack(inputSlot, { 1 }, HTStorageAction.EXECUTE)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.GENERIC_DEATH, SoundSource.BLOCKS, 0.5f, 1f)
    }

    @JvmRecord
    data class MobLootRecipe(val entity: LivingEntity, val drops: List<ImmutableItemStack>) {
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        private fun getExperienceAmount(level: ServerLevel, killer: ServerPlayer?): Int {
            val rawAmount: Int = entity.getExperienceReward(level, killer)
            return EventHooks.getExperienceDrop(entity, killer, rawAmount)
        }

        fun getExpStack(level: ServerLevel, killer: ServerPlayer?): ImmutableFluidStack? = getExperienceAmount(level, killer)
            .let(HTExperienceHelper::fluidAmountFromExp)
            .let(RagiumFluidContents.EXPERIENCE::toImmutableStack)
    }
}
