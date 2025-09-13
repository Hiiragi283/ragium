package hiiragi283.ragium

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.item.component.HTIntrinsicEnchantment
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.util.HTKeyOrTagEntry
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import io.wispforest.accessories.api.AccessoriesCapability
import io.wispforest.accessories.api.slot.SlotEntryReference
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.animal.Bee
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentInstance
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.EffectCure
import net.neoforged.neoforge.common.EffectCures
import net.neoforged.neoforge.event.enchanting.GetEnchantmentLevelEvent
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent
import net.neoforged.neoforge.event.entity.living.MobEffectEvent
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumRuntimeEvents {
    //    Block    //

    @SubscribeEvent
    fun onUseItem(event: PlayerInteractEvent.RightClickItem) {
        val stack: ItemStack = event.itemStack
        if (stack.isEmpty) return
        val player: Player = event.entity
        val level: Level = player.level()
        // エンダーバンドルの場合はGUIを開く
        /*if (stack.`is`(RagiumItems.ENDER_BUNDLE)) {
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
        }*/
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

    //    Enchantment    //

    @SubscribeEvent
    fun getEnchantmentLevel(event: GetEnchantmentLevelEvent) {
        val stack: ItemStack = event.stack
        val enchantments: ItemEnchantments.Mutable = event.enchantments
        val lookup: HolderLookup.RegistryLookup<Enchantment> = event.lookup

        val enchantment: HTIntrinsicEnchantment = stack.get(RagiumDataComponents.INTRINSIC_ENCHANTMENT) ?: return
        enchantment.getInstance(lookup).ifPresent { instance: EnchantmentInstance ->
            enchantments.set(instance.enchantment, instance.level)
        }
    }

    //    Entity    //

    @SubscribeEvent
    fun onClickedEntity(event: PlayerInteractEvent.EntityInteract) {
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

    /*fun onEntityStruck(event: EntityStruckByLightningEvent) {
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
    }*/

    @SubscribeEvent
    fun inEntityDamaged(event: LivingDamageEvent.Pre) {
        val accessoryCap: AccessoriesCapability = RagiumAPI.getInstance().getAccessoryCap(event.entity) ?: return
        val reference: SlotEntryReference = accessoryCap.getFirstEquipped { stack: ItemStack ->
            stack.has(RagiumDataComponents.IMMUNE_DAMAGE_TYPES)
        } ?: return
        val entry: HTKeyOrTagEntry<DamageType> = reference.stack.get(RagiumDataComponents.IMMUNE_DAMAGE_TYPES) ?: return
        val source: DamageSource = event.source
        if (entry.map(source::`is`, source::`is`)) {
            event.newDamage = 0f
        }
    }

    @SubscribeEvent
    fun onEntityDeath(event: LivingDeathEvent) {
        // 対象が共振の残骸を生成しない場合はスキップ
        val entity: LivingEntity = event.entity
        if (!entity.type.`is`(RagiumModTags.EntityTypes.GENERATE_RESONANT_DEBRIS)) return
        // サーバー側のみで実行する
        val level: Level = entity.level()
        if (level.isClientSide) return
        // 半径4 m以内のブロックに対して変換を試みる
        val entityPos: BlockPos = entity.blockPosition()
        val random: RandomSource = entity.random
        BlockPos.betweenClosed(entityPos.offset(-4, -4, -4), entityPos.offset(4, 4, 4)).forEach { pos: BlockPos ->
            val state: BlockState = level.getBlockState(pos)
            if (state.`is`(RagiumModTags.Blocks.RESONANT_DEBRIS_REPLACEABLES)) {
                if (random.nextInt(15) == 0) {
                    level.destroyBlock(pos, false)
                    level.setBlockAndUpdate(pos, RagiumBlocks.RESONANT_DEBRIS.get().defaultBlockState())
                }
            }
        }
    }

    @SubscribeEvent
    fun onEffectRemove(event: MobEffectEvent.Remove) {
        val cure: EffectCure = event.cure ?: return
        if (cure == EffectCures.MILK && RagiumConfig.COMMON.disableMilkCure.asBoolean) {
            event.isCanceled = true
        }
    }
}
