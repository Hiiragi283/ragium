package hiiragi283.ragium.internal

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.advancements.HTBlockInteractionTrigger
import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.recipe.HTBlockInteractingRecipe
import hiiragi283.ragium.api.recipe.HTCauldronDroppingRecipe
import hiiragi283.ragium.api.recipe.HTInteractRecipeInput
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import hiiragi283.ragium.setup.RagiumComponentTypes
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.stats.Stats
import net.minecraft.tags.BlockTags
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.animal.Bee
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.npc.WanderingTrader
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ChestMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RenderTooltipEvent
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.event.LootTableLoadEvent
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent
import net.neoforged.neoforge.event.tick.EntityTickEvent
import net.neoforged.neoforge.fluids.SimpleFluidContent
import org.slf4j.Logger
import kotlin.jvm.optionals.getOrNull

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumRuntimeEvents {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    //    Item Interaction    //

    @SubscribeEvent
    fun onClickedEntity(event: PlayerInteractEvent.EntityInteract) {
        // イベントがキャンセルされている場合はパス
        if (event.isCanceled) return
        val stack: ItemStack = event.itemStack
        // アイテムがガラス瓶の場合はハチを捕まえる
        if (stack.`is`(Items.GLASS_BOTTLE)) {
            val target: Bee = event.target as? Bee ?: return
            if (target.isAlive) {
                val player: Player = event.entity
                target.level().playSound(player, target, SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS, 1f, 1f)
                // ハチを瓶に詰める
                if (!player.level().isClientSide) {
                    target.discard()
                    stack.shrink(1)
                    dropStackAt(player, RagiumItems.BOTTLED_BEE)
                }
                event.cancellationResult = InteractionResult.sidedSuccess(player.level().isClientSide)
                return
            }
        }
    }

    @SubscribeEvent
    fun onClickedBlock(event: PlayerInteractEvent.RightClickBlock) {
        if (event.isCanceled) return
        val hand: InteractionHand = event.hand
        val stack: ItemStack = event.itemStack

        val hitResult: BlockHitResult = event.hitVec
        val level: Level = event.level
        val state: BlockState = level.getBlockState(hitResult.blockPos)

        val input = HTInteractRecipeInput(hitResult.blockPos, stack)
        val firstRecipe: HTBlockInteractingRecipe = level.recipeManager
            .getRecipeFor(RagiumRecipeTypes.BLOCK_INTERACTING.get(), input, level)
            .getOrNull()
            ?.value ?: return

        val player: Player = event.entity
        if (player is ServerPlayer) HTBlockInteractionTrigger.trigger(player, state)
        firstRecipe.applyActions(UseOnContext(level, player, hand, stack, hitResult))
        event.isCanceled = true
        event.cancellationResult = InteractionResult.sidedSuccess(level.isClientSide)
    }

    @SubscribeEvent
    fun onUseItem(event: PlayerInteractEvent.RightClickItem) {
        if (event.isCanceled) return
        val stack: ItemStack = event.itemStack
        if (stack.isEmpty) return
        val player: Player = event.entity
        val level: Level = player.level()
        // エンダーバンドルの場合はGUIを開く
        if (stack.`is`(RagiumItems.ENDER_BUNDLE)) {
            // SEを再生する
            level.playSound(null, player.blockPosition(), SoundEvents.ENDER_CHEST_OPEN, SoundSource.BLOCKS)
            // GUiを開く
            player.openMenu(
                SimpleMenuProvider(
                    { containerId: Int, inventory: Inventory, playerIn: Player ->
                        ChestMenu.threeRows(containerId, inventory, playerIn.enderChestInventory)
                    },
                    Component.translatable("container.enderchest"),
                ),
            )
            player.awardStat(Stats.OPEN_ENDERCHEST)
            event.cancellationResult = InteractionResult.sidedSuccess(level.isClientSide)
            return
        }
        // 行商人のカタログの場合もGUIを開く
        if (stack.`is`(RagiumItems.TRADER_CATALOG)) {
            event.cancellationResult =
                WanderingTrader(EntityType.WANDERING_TRADER, level).interact(player, InteractionHand.MAIN_HAND)
            return
        }
        // 経験値ベリーの場合は経験値を与える
        if (stack.`is`(RagiumItems.EXP_BERRIES)) {
            player.giveExperiencePoints(8)
            stack.consume(1, player)
            level.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS)
            event.cancellationResult = InteractionResult.sidedSuccess(level.isClientSide)
            return
        }
        // アイテムがハチ入りの瓶の場合はハチを開放する
        if (stack.`is`(RagiumItems.BOTTLED_BEE)) {
            val result: InteractionResult = Items.BEE_SPAWN_EGG.use(event.level, player, event.hand).result
            if (result.indicateItemUse()) {
                dropStackAt(player, Items.GLASS_BOTTLE)
            }
            event.cancellationResult = result
            return
        }
    }

    @SubscribeEvent
    fun onFinishUsingItem(event: LivingEntityUseItemEvent.Finish) {
        val stack: ItemStack = event.item
        if (stack.isEmpty) return
        val result: ItemStack = event.resultStack
        val user: LivingEntity = event.entity
        val level: Level = user.level()
        // アンブロシアの場合は個数を減らさない
        if (stack.`is`(RagiumItems.AMBROSIA)) {
            if (result.isEmpty) {
                event.resultStack = stack.copy()
            } else {
                result.grow(1)
            }
            return
        }
        // アイスクリームの場合は火を消す
        if (stack.`is`(RagiumItems.ICE_CREAM)) {
            if (!level.isClientSide) {
                user.extinguishFire()
            }
            return
        }
    }

    //    Entity    //

    @SubscribeEvent
    fun onEntityStruck(event: EntityStruckByLightningEvent) {
        if (event.isCanceled) return
        // プレイヤーによって召喚された落雷は無視される
        if (event.lightning.cause != null) return

        val target: Entity = event.entity
        LOGGER.info("Entity: ${target.type} is struck!")
        // すでに落雷を受けたエンティティは除外される
        if (target.persistentData.getBoolean("AlreadyStruck")) {
            LOGGER.info("Already struck entity found!")
            event.isCanceled = true
            return
        }
        // アイテムの場合だけ変換を行う
        val itemEntity: ItemEntity = target as? ItemEntity ?: return
        val stackIn: ItemStack = itemEntity.item
        if (stackIn.`is`(Tags.Items.INGOTS)) {
            itemEntity.item = RagiumItems.RAGI_ALLOY_INGOT.toStack(stackIn.count)
            itemEntity.persistentData.putBoolean("AlreadyStruck", true)
            event.isCanceled = true
        }
    }

    @SubscribeEvent
    fun onEntityTick(event: EntityTickEvent.Post) {
        val itemEntity: ItemEntity = event.entity as? ItemEntity ?: return
        // 大釜の中にいない場合はパス
        val stateIn: BlockState = itemEntity.inBlockState
        if (stateIn.`is`(Blocks.CAULDRON) || !stateIn.`is`(BlockTags.CAULDRONS)) return
        // 除外フラグを持っていたらパス
        if (itemEntity.persistentData.getBoolean(RagiumConstantValues.IGNORE_CAULDRON_DROP)) return
        // 最初に一致するレシピを取得する
        val level: Level = itemEntity.level()
        val pos: BlockPos = itemEntity.blockPosition()
        val stack: ItemStack = itemEntity.item
        val remainCount: Int = stack.count - 1

        val input = HTInteractRecipeInput(pos, stack)
        val firstRecipe: HTCauldronDroppingRecipe? = level.recipeManager
            .getRecipeFor(RagiumRecipeTypes.CAULDRON_DROPPING.get(), input, level)
            .getOrNull()
            ?.value
        // レシピが存在する場合，処理を実行する
        if (firstRecipe != null) {
            itemEntity.item = firstRecipe.assemble(input, level.registryAccess()).copy()
            level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState())
            if (remainCount > 0) {
                dropStackAt(itemEntity, stack.copyWithCount(remainCount))
            }
        } else {
            // レシピが存在しない場合，拾いなおすまで再実行させない
            itemEntity.persistentData.putBoolean(RagiumConstantValues.IGNORE_CAULDRON_DROP, true)
        }
    }

    //    Loot Table    //

    @SubscribeEvent
    fun onLoadLootTable(event: LootTableLoadEvent) {
        val loot: LootTable = event.table

        fun modify(entityType: EntityType<*>, function: LootTable.() -> Unit) {
            if (loot.lootTableId == entityType.defaultLootTable.location()) {
                function(loot)
            }
        }

        fun modify(block: Block, function: LootTable.() -> Unit) {
            if (loot.lootTableId == block.lootTable.location()) {
                function(loot)
            }
        }

        // エルダーガーディアンがエルダーの心臓を落とすように
        modify(EntityType.ELDER_GUARDIAN) {
            addPool(
                LootPool
                    .lootPool()
                    .setRolls(ConstantValue.exactly(1f))
                    .add(LootItem.lootTableItem(RagiumItems.ELDER_HEART))
                    .build(),
            )
            LOGGER.info("Modified loot table for Elder Guardian!")
        }
        // 行商人がカタログを落とすように
        modify(EntityType.WANDERING_TRADER) {
            if (RagiumConfig.COMMON.dropTraderCatalog.get()) {
                addPool(
                    LootPool
                        .lootPool()
                        .setRolls(ConstantValue.exactly(1f))
                        .add(LootItem.lootTableItem(RagiumItems.TRADER_CATALOG))
                        .build(),
                )
            }
            LOGGER.info("Modified loot table for Wandering Trader!")
        }
    }

    //    Tooltips    //

    @SubscribeEvent
    fun itemTooltips(event: ItemTooltipEvent) {
        val stack: ItemStack = event.itemStack
        if (stack.`is`(RagiumModTags.Items.WIP)) {
            event.toolTip.add(Component.translatable(RagiumTranslationKeys.TEXT_WIP).withStyle(ChatFormatting.DARK_RED))
        }
    }

    @SubscribeEvent
    fun gatherComponents(event: RenderTooltipEvent.GatherComponents) {
        val stack: ItemStack = event.itemStack
        val content: SimpleFluidContent = stack.get(RagiumComponentTypes.FLUID_CONTENT) ?: return
        if (content.isEmpty) return
        // event.tooltipElements.add(1, Either.right(HTFluidTooltipComponent(content)))
    }
}
