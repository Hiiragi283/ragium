package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.event.HTAdvancementRewardCallback
import hiiragi283.ragium.api.inventory.HTBackpackInventory
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.util.openEnderChest
import hiiragi283.ragium.common.util.sendTitle
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.fabricmc.fabric.api.event.player.UseItemCallback
import net.minecraft.advancement.AdvancementEntry
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object RagiumEventHandlers {
    @JvmStatic
    fun init() {
        // send title and floating item packet when unlock advancement
        HTAdvancementRewardCallback.EVENT.register { player: ServerPlayerEntity, entry: AdvancementEntry ->
            if (entry.id == RagiumAPI.id("tier1/root")) {
                player.sendTitle(Text.literal("Welcome to Heat Age!"))
                RagiumNetworks.sendFloatingItem(player, Items.BRICKS)
            }
            if (entry.id == RagiumAPI.id("tier2/root")) {
                player.sendTitle(Text.literal("Welcome to Kinetic Age!"))
                RagiumNetworks.sendFloatingItem(player, Items.POLISHED_BLACKSTONE_BRICKS)
            }
            if (entry.id == RagiumAPI.id("tier3/root")) {
                player.sendTitle(Text.literal("Welcome to Electric Age!"))
                RagiumNetworks.sendFloatingItem(player, Items.END_STONE_BRICKS)
            }
            if (entry.id == RagiumAPI.id("tier4/root")) {
                player.sendTitle(Text.literal("Welcome to Alchemical Age!"))
                RagiumNetworks.sendFloatingItem(player, RagiumContents.Element.RAGIUM.dustItem)
            }
        }

        /*ServerLivingEntityEvents.AFTER_DEATH.register { entity: LivingEntity, damage: DamageSource ->
            if (entity.type.isIn(EntityTypeTags.UNDEAD) && damage.isIn(DamageTypeTags.IS_PLAYER_ATTACK)) {
                dropStackAt(entity, Items.NETHER_STAR.defaultStack)
            }
        }*/

        // open backpack
        UseItemCallback.EVENT.register { player: PlayerEntity, world: World, hand: Hand ->
            val stack: ItemStack = player.getStackInHand(hand)
            (stack.get(RagiumComponentTypes.INVENTORY) as? HTBackpackInventory)
                ?.openInventory(world, player, stack)
                ?: TypedActionResult.pass(stack)
        }

        // open ender chest
        UseItemCallback.EVENT.register { player: PlayerEntity, world: World, hand: Hand ->
            val stack: ItemStack = player.getStackInHand(hand)
            if (stack.isOf(RagiumContents.ENDER_BACKPACK)) {
                openEnderChest(world, player)
                TypedActionResult.success(stack, world.isClient)
            } else {
                TypedActionResult.pass(stack)
            }
        }

        // spawn oblivion cube when oblivion cluster broken
        PlayerBlockBreakEvents.AFTER.register { world: World, player: PlayerEntity, pos: BlockPos, state: BlockState, _: BlockEntity? ->
            if (!player.isCreative) {
                if (state.isOf(RagiumContents.OBLIVION_CLUSTER)) {
                    RagiumEntityTypes.OBLIVION_CUBE.create(world)?.let {
                        it.refreshPositionAndAngles(pos.x + 0.5, pos.y.toDouble(), pos.z + 0.5, 0.0F, 0.0F)
                        world.spawnEntity(it)
                        it.playSpawnEffects()
                    }
                }
            }
        }
    }
}
