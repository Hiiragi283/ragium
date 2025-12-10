package hiiragi283.ragium.common.event

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.api.entity.isOf
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.util.HTItemDropHelper
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Equipable
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.EffectCure
import net.neoforged.neoforge.common.EffectCures
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.event.ItemAttributeModifierEvent
import net.neoforged.neoforge.event.entity.EntityTeleportEvent
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent
import net.neoforged.neoforge.event.entity.living.MobEffectEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumRuntimeHandler {
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

    /*fun onInteractBlock(event: PlayerInteractEvent.RightClickBlock) {
        val level: Level = event.level

        val pos: BlockPos = event.pos
        val state: BlockState = level.getBlockState(pos)
        val stack: ItemStack = event.itemStack

        if (state.`is`(Blocks.BUDDING_AMETHYST) && stack.`is`(RagiumModTags.Items.BUDDING_AZURE_ACTIVATOR)) {
            if (!level.isClientSide) {
                val player: Player = event.entity
                if (player is ServerPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(player, pos, stack)
                }
                level.setBlockAndUpdate(pos, RagiumBlocks.BUDDING_QUARTZ.get().defaultBlockState())
                stack.consume(1, player)
            }
            event.cancellationResult = InteractionResult.sidedSuccess(level.isClientSide)
            event.isCanceled = true
        }
    }*/

    //    Entity    //

    @SubscribeEvent
    fun beforeTeleport(event: EntityTeleportEvent.EnderEntity) {
        // 異質な流動体に触れているエンダーマンはワープできなくなる
        if (event.entityLiving.isInFluidType(RagiumFluidContents.ELDRITCH_FLUX.getType())) {
            event.isCanceled = true
        }
    }

    @SubscribeEvent
    fun onClickedEntity(event: PlayerInteractEvent.EntityInteract) {
        val stack: ItemStack = event.itemStack
        // アイテムがガラス瓶の場合はハチを捕まえる
        if (stack.`is`(Items.GLASS_BOTTLE)) {
            val target: Entity = event.target ?: return
            if (target.isOf(EntityType.BEE) && target.isAlive) {
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
        if (!EquipmentSlotGroup.ARMOR.test(event.slot)) return
        val entity: Player = event.entity as? Player ?: return
        val from: ItemStack = event.from
        val to: ItemStack = event.to
        // 装着時
        if (from.isEmpty) {
            RagiumDataMapTypes.getEquipAction(to)?.onEquip(entity, to)
        }
        // 脱着時
        if (to.isEmpty) {
            RagiumDataMapTypes.getEquipAction(from)?.onUnequip(entity, from)
        }
    }

    /*fun onEntityStruck(event: EntityStruckByLightningEvent) {
        // プレイヤーによって召喚された落雷は無視される
        if (event.lightning.cause != null) return

        val target: Entity = event.entity
        RagiumAPI.LOGGER.debug("Entity: {} is struck!", target.type)
        // すでに落雷を受けたエンティティは除外される
        if (target.persistentData.getBoolean("AlreadyStruck")) {
            RagiumAPI.LOGGER.debug("Already struck entity found!")
            event.isCanceled = true
            return
        }
        // アイテムの場合だけ変換を行う
        val itemEntity: ItemEntity = target as? ItemEntity ?: return
        val stackIn: ItemStack = itemEntity.item
        if (RagiumBlocks.IMITATION_SPAWNER.isOf(stackIn)) {
            itemEntity.item = ItemStack(Items.SPAWNER)
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

    @Suppress("DEPRECATION")
    @SubscribeEvent
    fun onItemCrafted(event: PlayerEvent.ItemCraftedEvent) {
        val result: ItemStack = event.crafting
        if (result.isEmpty) return
        // 中身のアイテムをプレイヤーに渡す
        result.remove(RagiumDataComponents.ITEM_CONTENT)?.forEach { stack: ImmutableItemStack? ->
            HTItemDropHelper.giveStackTo(event.entity, stack)
        }
    }
}
