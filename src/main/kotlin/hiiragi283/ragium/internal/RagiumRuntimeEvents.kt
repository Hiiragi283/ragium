package hiiragi283.ragium.internal

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.advancements.HTBlockInteractionTrigger
import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.recipe.HTBlockInteractingRecipe
import hiiragi283.ragium.api.recipe.HTInteractRecipeInput
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumEnchantments
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.stats.Stats
import net.minecraft.util.RandomSource
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
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.client.event.RenderTooltipEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.event.enchanting.GetEnchantmentLevelEvent
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent
import net.neoforged.neoforge.fluids.SimpleFluidContent
import org.slf4j.Logger
import kotlin.jvm.optionals.getOrNull

internal object RagiumRuntimeEvents {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    @JvmStatic
    fun registerEvents() {
        NeoForge.EVENT_BUS.addListener(::onClickedEntity)
        NeoForge.EVENT_BUS.addListener(::onClickedBlock)
        NeoForge.EVENT_BUS.addListener(::onUseItem)
        NeoForge.EVENT_BUS.addListener(::onFinishUsingItem)

        NeoForge.EVENT_BUS.addListener(::onEntityStruck)
        NeoForge.EVENT_BUS.addListener(::onEntityDeath)

        NeoForge.EVENT_BUS.addListener(::getEnchantmentLevel)
        NeoForge.EVENT_BUS.addListener(::itemTooltips)
        NeoForge.EVENT_BUS.addListener(::gatherComponents)
    }

    //    Item Interaction    //

    private fun onClickedEntity(event: PlayerInteractEvent.EntityInteract) {
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

    private fun onClickedBlock(event: PlayerInteractEvent.RightClickBlock) {
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

    private fun onUseItem(event: PlayerInteractEvent.RightClickItem) {
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

    private fun onFinishUsingItem(event: LivingEntityUseItemEvent.Finish) {
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

    private fun onEntityStruck(event: EntityStruckByLightningEvent) {
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

    private fun onEntityDeath(event: LivingDeathEvent) {
        val entity: LivingEntity = event.entity
        if (!entity.type.`is`(RagiumModTags.EntityTypes.GENERATE_RESONANT_DEBRIS)) return

        val level: Level = entity.level()
        if (level.isClientSide) return

        val entityPos: BlockPos = entity.blockPosition()
        val random: RandomSource = entity.random
        val range: IntRange = -4..4
        for (x: Int in range) {
            for (y: Int in range) {
                for (z: Int in range) {
                    val pos: BlockPos = entityPos.offset(x, y, z)
                    val state: BlockState = level.getBlockState(pos)
                    if (state.`is`(RagiumModTags.Blocks.RESONANT_DEBRIS_REPLACEABLES)) {
                        if (random.nextInt(15) == 0) {
                            level.destroyBlock(pos, false)
                            level.setBlockAndUpdate(pos, RagiumBlocks.RESONANT_DEBRIS.get().defaultBlockState())
                        }
                    }
                }
            }
        }
    }

    //    Enchantments    //

    private fun getEnchantmentLevel(event: GetEnchantmentLevelEvent) {
        val stack: ItemStack = event.stack
        val enchantments: ItemEnchantments.Mutable = event.enchantments
        val lookup: HolderLookup.RegistryLookup<Enchantment> = event.lookup
        // Add Noise Canceling V to Deep Steel Sword
        if (stack.`is`(RagiumItems.DEEP_STEEL_TOOLS.swordItem)) {
            lookup.get(RagiumEnchantments.NOISE_CANCELING).ifPresent { holder: Holder.Reference<Enchantment> ->
                enchantments.upgrade(holder, 5)
            }
            return
        }
        // Set Fortune V to Deep Steel Pickaxe
        if (stack.`is`(RagiumItems.DEEP_STEEL_TOOLS.pickaxeItem)) {
            lookup.get(Enchantments.FORTUNE).ifPresent { holder: Holder.Reference<Enchantment> ->
                enchantments.set(holder, 5)
            }
            return
        }
    }

    //    Tooltips    //

    private fun itemTooltips(event: ItemTooltipEvent) {
        val stack: ItemStack = event.itemStack
        if (stack.`is`(RagiumModTags.Items.WIP)) {
            event.toolTip.add(Component.translatable(RagiumTranslationKeys.TEXT_WIP).withStyle(ChatFormatting.DARK_RED))
        }
    }

    private fun gatherComponents(event: RenderTooltipEvent.GatherComponents) {
        val stack: ItemStack = event.itemStack
        val content: SimpleFluidContent = stack.get(RagiumDataComponents.FLUID_CONTENT) ?: return
        if (content.isEmpty) return
        // event.tooltipElements.add(1, Either.right(HTFluidTooltipComponent(content)))
    }
}
