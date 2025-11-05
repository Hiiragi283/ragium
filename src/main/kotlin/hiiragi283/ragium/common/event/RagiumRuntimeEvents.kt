package hiiragi283.ragium.common.event

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.common.util.HTItemDropHelper
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
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
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.EffectCure
import net.neoforged.neoforge.common.EffectCures
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.event.ItemAttributeModifierEvent
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent
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

    @SubscribeEvent
    fun onEquipped(event: LivingEquipmentChangeEvent) {
        val entity: LivingEntity = event.entity
        val from: ItemStack = event.from
        val to: ItemStack = event.to
        // 装着時
        if (from.isEmpty) {
            if (to.`is`(RagiumItems.NIGHT_VISION_GOGGLES)) {
                entity.addEffect(MobEffectInstance(MobEffects.NIGHT_VISION, -1, 0, true, true))
            }
        }
        // 脱着時
        if (to.isEmpty) {
            if (from.`is`(RagiumItems.NIGHT_VISION_GOGGLES)) {
                entity.removeEffect(MobEffects.NIGHT_VISION)
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
