package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.accessory.HTAccessoryRegistry
import hiiragi283.ragium.api.event.HTAdvancementRewardCallback
import hiiragi283.ragium.api.event.HTBrushingDropRegistry
import hiiragi283.ragium.api.extension.energyPercent
import hiiragi283.ragium.api.extension.sendTitle
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTFluidSyncable
import hiiragi283.ragium.api.recipe.HTItemIngredient
import hiiragi283.ragium.api.screen.HTMachineScreenHandlerBase
import hiiragi283.ragium.common.RagiumContents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.minecraft.advancement.AdvancementEntry
import net.minecraft.block.BlockState
import net.minecraft.component.ComponentMap
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.boss.BossBar
import net.minecraft.entity.boss.ServerBossBar
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.*
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.BlockRotation
import net.minecraft.util.Hand
import net.minecraft.util.Rarity
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import team.reborn.energy.api.EnergyStorage

object RagiumEventHandlers {
    @JvmField
    val ENERGY_BAR = ServerBossBar(Text.empty(), BossBar.Color.YELLOW, BossBar.Style.PROGRESS)

    @JvmStatic
    fun init() {
        // send title and floating item packet when unlock advancement
        HTAdvancementRewardCallback.EVENT.register { player: ServerPlayerEntity, entry: AdvancementEntry ->
            if (entry.id == RagiumAPI.id("tier1/root")) {
                player.sendTitle(Text.literal("Welcome to Tier1!").formatted(Rarity.COMMON.formatting))
                RagiumNetworks.sendFloatingItem(player, RagiumContents.Ingots.RAGI_ALLOY)
            }
            if (entry.id == RagiumAPI.id("tier2/root")) {
                player.sendTitle(Text.literal("Welcome to Tier2!").formatted(Rarity.UNCOMMON.formatting))
                RagiumNetworks.sendFloatingItem(player, RagiumContents.Ingots.RAGI_STEEL)
            }
            if (entry.id == RagiumAPI.id("tier3/root")) {
                player.sendTitle(Text.literal("Welcome to Tier3!").formatted(Rarity.RARE.formatting))
                RagiumNetworks.sendFloatingItem(player, RagiumContents.Ingots.REFINED_RAGI_STEEL)
            }
            if (entry.id == RagiumAPI.id("tier4/root")) {
                player.sendTitle(Text.literal("Welcome to Tier4!").formatted(Rarity.EPIC.formatting))
                RagiumNetworks.sendFloatingItem(player, RagiumContents.Gems.RAGIUM)
            }
        }

        ServerEntityEvents.EQUIPMENT_CHANGE.register { entity: LivingEntity, slot: EquipmentSlot, old: ItemStack, new: ItemStack ->
            if (slot.type == EquipmentSlot.Type.HUMANOID_ARMOR) {
                if (old.isEmpty && !new.isEmpty) {
                    HTAccessoryRegistry.onEquipped(entity, new)
                    return@register
                }
                if (!old.isEmpty && new.isEmpty) {
                    HTAccessoryRegistry.onUnequipped(entity, old)
                    return@register
                }
            }
        }

        ServerTickEvents.END_SERVER_TICK.register { server: MinecraftServer ->
            server.playerManager.playerList.forEach { player: ServerPlayerEntity ->
                // send fluid sync packet
                (player.currentScreenHandler as? HTMachineScreenHandlerBase)?.let { screen: HTMachineScreenHandlerBase ->
                    (player.world.getBlockEntity(screen.pos) as? HTFluidSyncable)
                        ?.sendPacket(player, RagiumNetworks::sendFluidSync)
                }
                // consume energy when worm stella goggles
                if (player.armorItems.any { it.isOf(RagiumItems.STELLA_GOGGLE) }) {
                    if (!HTMachineTier.BASIC.consumerEnergy(player.world)) {
                        player.removeStatusEffect(StatusEffects.NIGHT_VISION)
                    }
                }
                // show energy bar (boss bar) when holding energy item
                val itemContext: ContainerItemContext = ContainerItemContext.forPlayerInteraction(player, Hand.MAIN_HAND)
                itemContext
                    .find(EnergyStorage.ITEM)
                    ?.let { storage: EnergyStorage ->
                        ENERGY_BAR.apply {
                            name = itemContext.itemVariant.toStack().name
                            percent = storage.energyPercent
                            addPlayer(player)
                        }
                    } ?: run { ENERGY_BAR.removePlayer(player) }
            }
        }

        // rotate block by ragi-wrench
        UseBlockCallback.EVENT.register { player: PlayerEntity, world: World, hand: Hand, result: BlockHitResult ->
            val stack: ItemStack = player.getStackInHand(hand)
            if (stack.isOf(RagiumItems.RAGI_WRENCH)) {
                val pos: BlockPos = result.blockPos
                val state: BlockState = world.getBlockState(pos)
                val rotated: BlockState = when (player.isSneaking) {
                    true -> {
                        if (Properties.FACING in state) {
                            state.with(Properties.FACING, result.side)
                        } else {
                            state
                        }
                    }

                    false -> state.rotate(BlockRotation.COUNTERCLOCKWISE_90)
                }
                if (rotated != state) {
                    if (!world.isClient) {
                        world.setBlockState(pos, rotated)
                    }
                    return@register ActionResult.success(world.isClient)
                }
            }
            ActionResult.PASS
        }
        // hard mode repair
        DefaultItemComponentEvents.MODIFY.register { context: DefaultItemComponentEvents.ModifyContext ->
            context.modify({
                (it as? ToolItem)?.material == ToolMaterials.IRON || (it as? ArmorItem)?.material == ArmorMaterials.IRON
            }) { builder: ComponentMap.Builder, item: Item ->
                builder.add(
                    RagiumComponentTypes.REPAIRMENT,
                    HTItemIngredient.of(RagiumHardModeContents.IRON.getContent(RagiumAPI.getInstance().config.isHardMode)),
                )
            }
            context.modify({
                (it as? ToolItem)?.material == ToolMaterials.GOLD || (it as? ArmorItem)?.material == ArmorMaterials.GOLD
            }) { builder: ComponentMap.Builder, item: Item ->
                builder.add(
                    RagiumComponentTypes.REPAIRMENT,
                    HTItemIngredient.of(RagiumHardModeContents.GOLD.getContent(RagiumAPI.getInstance().config.isHardMode)),
                )
            }
            context.modify({
                (it as? ToolItem)?.material == ToolMaterials.NETHERITE || (it as? ArmorItem)?.material == ArmorMaterials.NETHERITE
            }) { builder: ComponentMap.Builder, item: Item ->
                builder.add(
                    RagiumComponentTypes.REPAIRMENT,
                    HTItemIngredient.of(RagiumHardModeContents.NETHERITE.getContent(RagiumAPI.getInstance().config.isHardMode)),
                )
            }
        }
        // brushing interaction
        HTBrushingDropRegistry.init()
    }
}
