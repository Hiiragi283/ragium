package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.accessory.HTAccessoryRegistry
import hiiragi283.ragium.api.content.HTHardModeContents
import hiiragi283.ragium.api.event.HTAdvancementRewardCallback
import hiiragi283.ragium.api.event.HTModifyBlockDropsCallback
import hiiragi283.ragium.api.extension.hasEnchantment
import hiiragi283.ragium.api.extension.sendTitle
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTFluidSyncable
import hiiragi283.ragium.api.recipe.HTItemIngredient
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.screen.HTMachineScreenHandlerBase
import hiiragi283.ragium.common.RagiumContents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents
import net.minecraft.advancement.AdvancementEntry
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.component.ComponentMap
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterials
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolItem
import net.minecraft.item.ToolMaterials
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeEntry
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.input.RecipeInput
import net.minecraft.recipe.input.SingleStackRecipeInput
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.BlockRotation
import net.minecraft.util.Hand
import net.minecraft.util.Rarity
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object RagiumEventHandlers {
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

        // modify drops
        HTModifyBlockDropsCallback.EVENT.register {
                _: BlockState,
                world: ServerWorld,
                _: BlockPos,
                _: BlockEntity?,
                breaker: Entity?,
                tool: ItemStack,
                drops: List<ItemStack>,
            ->
            when {
                hasEnchantment(RagiumEnchantments.SMELTING, world, tool) -> drops.map { drop: ItemStack ->
                    applyRecipe(drop, world, breaker, tool, RecipeType.SMELTING, ::SingleStackRecipeInput)
                }

                hasEnchantment(RagiumEnchantments.SLEDGE_HAMMER, world, tool) -> drops.map { drop: ItemStack ->
                    applyMachineRecipe(drop, world, breaker, tool, RagiumMachineKeys.GRINDER)
                }

                hasEnchantment(RagiumEnchantments.BUZZ_SAW, world, tool) -> drops.map { drop: ItemStack ->
                    applyMachineRecipe(drop, world, breaker, tool, RagiumMachineKeys.SAW_MILL)
                }

                else -> drops
            }
        }

        // DefaultItemComponentEvents
        /*HTAllowSpawnCallback.EVENT.register { entityType: EntityType<*>, _: ServerWorldAccess, _: BlockPos, reason: SpawnReason ->
            if (entityType.spawnGroup == SpawnGroup.MONSTER && reason == SpawnReason.NATURAL) TriState.FALSE else TriState.DEFAULT
        }

        ServerLivingEntityEvents.AFTER_DEATH.register { entity: LivingEntity, damage: DamageSource ->
            if (entity.type.isIn(EntityTypeTags.UNDEAD) && damage.isIn(DamageTypeTags.IS_PLAYER_ATTACK)) {
                dropStackAt(entity, Items.NETHER_STAR.defaultStack)
            }
        }*/

        // range mining
        /*PlayerBlockBreakEvents.AFTER.register { world: World, player: PlayerEntity, pos: BlockPos, _: BlockState, _: BlockEntity? ->
            val enchant: RegistryEntry<Enchantment> =
                world.getEntry(RegistryKeys.ENCHANTMENT, Enchantments.UNBREAKING) ?: return@register
            val stack: ItemStack = player.getStackInHand(Hand.MAIN_HAND)
            val enchantLevel: Int = EnchantmentHelper.getLevel(enchant, stack)
            if (enchantLevel > 0) {
                breakRangedBlock(
                    world,
                    pos,
                    enchantLevel,
                    player,
                    stack
                )
            }
        }*/

        // spawn oblivion cube when oblivion cluster broken
        /*PlayerBlockBreakEvents.AFTER.register { world: World, player: PlayerEntity, pos: BlockPos, state: BlockState, _: BlockEntity? ->
            if (!player.isCreative) {
                if (state.isOf(RagiumBlocks.OBLIVION_CLUSTER)) {
                    RagiumEntityTypes.OBLIVION_CUBE.create(world)?.let {
                        it.refreshPositionAndAngles(pos.x + 0.5, pos.y.toDouble(), pos.z + 0.5, 0.0F, 0.0F)
                        world.spawnEntity(it)
                        it.playSpawnEffects()
                    }
                }
            }
        }*/

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

        DefaultItemComponentEvents.MODIFY.register { context: DefaultItemComponentEvents.ModifyContext ->
            context.modify({
                (it as? ToolItem)?.material == ToolMaterials.IRON || (it as? ArmorItem)?.material == ArmorMaterials.IRON
            }) { builder: ComponentMap.Builder, item: Item ->
                builder.add(
                    RagiumComponentTypes.REPAIRMENT,
                    HTItemIngredient.of(HTHardModeContents.IRON.getContent(RagiumAPI.getInstance().config.isHardMode)),
                )
            }
            context.modify({
                (it as? ToolItem)?.material == ToolMaterials.GOLD || (it as? ArmorItem)?.material == ArmorMaterials.GOLD
            }) { builder: ComponentMap.Builder, item: Item ->
                builder.add(
                    RagiumComponentTypes.REPAIRMENT,
                    HTItemIngredient.of(HTHardModeContents.GOLD.getContent(RagiumAPI.getInstance().config.isHardMode)),
                )
            }
        }
    }

    @JvmStatic
    private fun <T : RecipeInput, U : Recipe<T>> applyRecipe(
        drop: ItemStack,
        world: World,
        breaker: Entity?,
        tool: ItemStack,
        recipeType: RecipeType<U>,
        factory: (ItemStack) -> T,
    ): ItemStack {
        val input: T = factory(drop)
        return world.recipeManager
            .getFirstMatch(recipeType, input, world)
            .map(RecipeEntry<U>::value)
            .map { it.craft(input, world.registryManager) }
            .map { drop1: ItemStack ->
                breaker
                    ?.let { it as? LivingEntity }
                    ?.let { tool.damage(1, it, EquipmentSlot.MAINHAND) }
                drop1
            }.orElse(drop)
    }

    @JvmStatic
    private fun applyMachineRecipe(
        drop: ItemStack,
        world: World,
        breaker: Entity?,
        tool: ItemStack,
        key: HTMachineKey,
    ): ItemStack = applyRecipe(drop, world, breaker, tool, RagiumRecipeTypes.MACHINE) {
        HTMachineInput.create(key, HTMachineTier.PRIMITIVE) { add(it) }
    }
}
