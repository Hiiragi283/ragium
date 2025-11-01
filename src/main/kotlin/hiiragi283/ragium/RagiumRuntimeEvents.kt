package hiiragi283.ragium

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.util.HTItemDropHelper
import hiiragi283.ragium.common.util.HTItemHelper
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import io.wispforest.accessories.api.AccessoriesCapability
import io.wispforest.accessories.api.slot.SlotEntryReference
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.animal.Bee
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Equipable
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.EffectCure
import net.neoforged.neoforge.common.EffectCures
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.event.ItemAttributeModifierEvent
import net.neoforged.neoforge.event.enchanting.GetEnchantmentLevelEvent
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent
import net.neoforged.neoforge.event.entity.living.MobEffectEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumRuntimeEvents {
    //    Attribute    //

    @JvmStatic
    private val CREATIVE_FLIGHT = AttributeModifier(
        RagiumAPI.id("anti_gravity"),
        1.0,
        AttributeModifier.Operation.ADD_VALUE,
    )

    @SubscribeEvent
    fun modifyItemAttribute(event: ItemAttributeModifierEvent) {
        val stack: ItemStack = event.itemStack
        val slot: EquipmentSlot = stack.equipmentSlot ?: Equipable.get(stack)?.equipmentSlot ?: return
        if (stack.getOrDefault(RagiumDataComponents.ANTI_GRAVITY, false)) {
            event.addModifier(NeoForgeMod.CREATIVE_FLIGHT, CREATIVE_FLIGHT, EquipmentSlotGroup.bySlot(slot))
        }
    }

    //    Block    //

    @SubscribeEvent
    fun onUseItem(event: PlayerInteractEvent.RightClickItem) {
        val stack: ItemStack = event.itemStack
        if (stack.isEmpty) return
        val player: Player = event.entity
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
                HTItemDropHelper.giveStackTo(player, ItemStack(Items.GLASS_BOTTLE))
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
        event.stack
            .get(RagiumDataComponents.INTRINSIC_ENCHANTMENT)
            ?.useInstance(event.lookup, event.enchantments::set)
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
                    HTItemDropHelper.giveStackTo(player, RagiumItems.BOTTLED_BEE.toStack())
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
    fun beforeEntityDamaged(event: LivingDamageEvent.Pre) {
        val accessoryCap: AccessoriesCapability = RagiumPlatform.INSTANCE.getAccessoryCap(event.entity) ?: return
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
        val entity: LivingEntity = event.entity
        val level: Level = entity.level()
        val source: DamageSource = event.source
        // サーバー側のみで実行する
        if (level.isClientSide) return
        generateResonantDebris(entity, level)
        lootMobHead(entity, level, source)
    }

    @JvmStatic
    private fun generateResonantDebris(entity: LivingEntity, level: Level) {
        // 対象が共振の残骸を生成しない場合はスキップ
        if (!entity.type.`is`(RagiumModTags.EntityTypes.GENERATE_RESONANT_DEBRIS)) return
        // 半径4 m以内のブロックに対して変換を試みる
        val entityPos: BlockPos = entity.blockPosition()
        BlockPos.betweenClosed(entityPos.offset(-4, -4, -4), entityPos.offset(4, 4, 4)).forEach { pos: BlockPos ->
            val state: BlockState = level.getBlockState(pos)
            if (state.`is`(RagiumModTags.Blocks.RESONANT_DEBRIS_REPLACEABLES)) {
                if (entity.random.nextInt(15) == 0) {
                    level.destroyBlock(pos, false)
                    level.setBlockAndUpdate(pos, RagiumBlocks.RESONANT_DEBRIS.get().defaultBlockState())
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    @JvmStatic
    private fun lootMobHead(entity: LivingEntity, level: Level, source: DamageSource) {
        // 武器にStrike効果が付いているか判定
        val weapon: ItemStack = source.weaponItem ?: return
        if (HTItemHelper.hasStrike(weapon)) {
            // 対象のモブに対応する頭をドロップする
            RagiumDataMaps.INSTANCE
                .getMobHead(level.registryAccess(), entity.type.builtInRegistryHolder())
                .let(entity::spawnAtLocation)
        }
    }

    @SubscribeEvent
    fun onEffectRemove(event: MobEffectEvent.Remove) {
        val cure: EffectCure = event.cure ?: return
        if (cure == EffectCures.MILK && RagiumConfig.COMMON.disableMilkCure.asBoolean) {
            event.isCanceled = true
        }
    }

    //    Recipe    //

    @SubscribeEvent
    fun onItemCrafted(event: PlayerEvent.ItemCraftedEvent) {
        val result: ItemStack = event.crafting
        if (result.isEmpty) return
        val stackIn: ImmutableItemStack = result.remove(RagiumDataComponents.ITEM_CONTENT)?.getOrNull(0) ?: return
        HTItemDropHelper.giveStackTo(event.entity, stackIn.unwrap())
    }
}
