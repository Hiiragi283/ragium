package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.block.attribute.getFluidAttribute
import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.registry.vanillaId
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTSlotInfo
import hiiragi283.ragium.api.storage.item.getItemStack
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.storage.fluid.tank.HTBasicFluidTank
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidTank
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTBasicItemSlot
import hiiragi283.ragium.common.storage.item.slot.HTOutputItemSlot
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
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.event.EventHooks

class HTMobCrusherBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<HTRecipeInput, HTMobCrusherBlockEntity.MobLootRecipe>(RagiumBlocks.MOB_CRUSHER, pos, state) {
    lateinit var outputTank: HTBasicFluidTank
        private set

    override fun initializeFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        // output
        outputTank = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTVariableFluidTank.output(listener, blockHolder.getFluidAttribute().getOutputTank()),
        )
    }

    lateinit var inputSlot: HTBasicItemSlot
        private set
    lateinit var toolSlot: HTBasicItemSlot
        private set
    lateinit var outputSlots: List<HTBasicItemSlot>
        private set

    override fun initializeItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        // input
        inputSlot = builder.addSlot(
            HTSlotInfo.INPUT,
            HTBasicItemSlot
                .input(
                    listener,
                    HTSlotHelper.getSlotPosX(1.5),
                    HTSlotHelper.getSlotPosY(2),
                    canInsert = { stack: ImmutableItemStack -> stack.value() is SpawnEggItem },
                ).setSlotBackground(RagiumConst.GUI_ATLAS, vanillaId("container/slot/spawn_egg.png")),
        )
        toolSlot = builder.addSlot(
            HTSlotInfo.CATALYST,
            HTBasicItemSlot
                .create(
                    listener,
                    HTSlotHelper.getSlotPosX(1.5),
                    HTSlotHelper.getSlotPosY(0),
                    canExtract = HTPredicates.manualOnly(),
                    canInsert = HTPredicates.manualOnly(),
                ).setSlotBackground(RagiumConst.BLOCK_ATLAS, vanillaId("item/empty_slot_sword")),
        )
        // outputs
        outputSlots = (0..<9).map { i: Int ->
            builder.addSlot(
                HTSlotInfo.OUTPUT,
                HTOutputItemSlot.create(
                    listener,
                    HTSlotHelper.getSlotPosX(3 + i % 3),
                    HTSlotHelper.getSlotPosY(i / 3),
                ),
            )
        }
    }

    //    Ticking    //

    override fun shouldCheckRecipe(level: ServerLevel, pos: BlockPos): Boolean =
        outputSlots.any { slot: HTBasicItemSlot -> slot.getNeeded() > 0 }

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTRecipeInput? = HTRecipeInput.single(inputSlot.getStack())

    /**
     * @see net.minecraft.server.commands.LootCommand.dropKillLoot
     */
    override fun getMatchedRecipe(input: HTRecipeInput, level: ServerLevel): MobLootRecipe? {
        val stack: ImmutableItemStack = input.item(0) ?: return null
        val entityType: EntityType<*> = (stack.value() as? SpawnEggItem)?.getType(stack.unwrap()) ?: return null
        val fakeEntity: LivingEntity = entityType.create(level) as? LivingEntity ?: return null

        val fakePlayer: ServerPlayer = getFakePlayer(level)
        fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, toolSlot.getItemStack())
        val damageSource: DamageSource = level.damageSources().playerAttack(fakePlayer)
        val lootTable: LootTable = level.server.reloadableRegistries().getLootTable(fakeEntity.lootTable)
        val lootParams: LootParams = LootParams
            .Builder(level)
            .withParameter(LootContextParams.THIS_ENTITY, fakeEntity)
            .withParameter(LootContextParams.ORIGIN, Vec3.atLowerCornerOf(blockPos))
            .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
            .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, damageSource.directEntity)
            .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, damageSource.entity)
            .withOptionalParameter(LootContextParams.LAST_DAMAGE_PLAYER, fakePlayer)
            .withLuck(fakePlayer.luck)
            .create(LootContextParamSets.ENTITY)
        return MobLootRecipe(
            fakeEntity,
            fakePlayer,
            lootTable.getRandomItems(lootParams, fakeEntity.lootTableSeed).mapNotNull(ItemStack::toImmutable),
        )
    }

    override fun getRecipeTime(recipe: MobLootRecipe): Int = super.getRecipeTime(recipe) / 4

    override fun canProgressRecipe(level: ServerLevel, input: HTRecipeInput, recipe: MobLootRecipe): Boolean {
        // アウトプットに搬出できるか判定する
        for (stackIn: ImmutableItemStack in recipe.drops) {
            if (HTStackSlotHelper.insertStacks(outputSlots, stackIn, HTStorageAction.SIMULATE) != null) {
                return false
            }
        }

        val expStack: ImmutableFluidStack = recipe.getExpStack(level) ?: return true
        return HTStackSlotHelper.canInsertStack(outputTank, expStack, true)
    }

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTRecipeInput,
        recipe: MobLootRecipe,
    ) {
        val (_, _, drops: List<ImmutableItemStack>) = recipe
        // 実際にアウトプットに搬出する
        for (stackIn: ImmutableItemStack in drops) {
            HTStackSlotHelper.insertStacks(outputSlots, stackIn, HTStorageAction.EXECUTE)
        }
        recipe.getExpStack(level)?.let { stack: ImmutableFluidStack ->
            outputTank.insert(stack, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        }
        // インプットを減らす
        inputSlot.extract(1, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.GENERIC_DEATH, SoundSource.BLOCKS, 0.5f, 1f)
    }

    @JvmRecord
    data class MobLootRecipe(val entity: LivingEntity, val player: ServerPlayer, val drops: List<ImmutableItemStack>) {
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        private fun getExperienceAmount(level: ServerLevel): Int {
            val rawAmount: Int = entity.getExperienceReward(level, player)
            return EventHooks.getExperienceDrop(entity, player, rawAmount)
        }

        fun getExpStack(level: ServerLevel): ImmutableFluidStack? = getExperienceAmount(level)
            .let(HTExperienceHelper::fluidAmountFromExp)
            .let(RagiumFluidContents.EXPERIENCE::toImmutableStack)
    }
}
