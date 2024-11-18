package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.accessory.HTAccessoryRegistry
import hiiragi283.ragium.api.event.HTAdvancementRewardCallback
import hiiragi283.ragium.api.event.HTModifyBlockDropsCallback
import hiiragi283.ragium.api.extension.hasEnchantment
import hiiragi283.ragium.api.extension.sendTitle
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTFluidSyncable
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.screen.HTMachineScreenHandlerBase
import hiiragi283.ragium.common.RagiumContents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents
import net.minecraft.advancement.AdvancementEntry
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeEntry
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.input.RecipeInput
import net.minecraft.recipe.input.SingleStackRecipeInput
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Rarity
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

        DefaultItemComponentEvents.MODIFY.register { context: DefaultItemComponentEvents.ModifyContext ->
            // blocks
            addDescription(
                context,
                RagiumBlocks.POROUS_NETHERRACK,
                Text.literal("Absorb lava like sponge but not reusable"),
            )
            addDescription(
                context,
                RagiumBlocks.SPONGE_CAKE,
                Text.literal("Decrease falling damage when landed"),
            )

            addDescription(
                context,
                RagiumBlocks.AUTO_ILLUMINATOR,
                Text.literal("Place light blocks below area"),
            )
            addDescription(
                context,
                RagiumBlocks.LARGE_PROCESSOR,
                Text.literal("Extend processor machine inside the multiblock"),
            )
            addDescription(
                context,
                RagiumBlocks.MANUAL_FORGE,
                Text.literal("Right-click to place ingredient, or process by Forge Hammer"),
            )
            addDescription(
                context,
                RagiumBlocks.MANUAL_GRINDER,
                Text.literal("Input ingredients by Hopper\nRight-click to process"),
            )
            addDescription(
                context,
                RagiumBlocks.MANUAL_MIXER,
                Text.literal("Process mixer recipe with holding items"),
            )
            addDescription(
                context,
                RagiumBlocks.OPEN_CRATE,
                Text.literal("Drop items below when inserted"),
            )
            addDescription(
                context,
                RagiumBlocks.TRASH_BOX,
                Text.literal("Remove ALL inserted items or fluids"),
            )
            // items
            addDescription(
                context,
                RagiumItems.BACKPACK,
                Text.literal("Share inventory between same colored backpacks"),
            )
            addDescription(
                context,
                RagiumItems.FLUID_FILTER,
                Text.literal("Right-click Exporter to apply\nId format - modid:path\nTag format - #modid:path"),
            )
            addDescription(
                context,
                RagiumItems.ITEM_FILTER,
                Text.literal("Right-click Exporter to apply\nId format - modid:path\nTag format - #modid:path"),
            )
            addDescription(
                context,
                RagiumItems.WARPED_CRYSTAL,
                Text.literal("Click on Teleport Anchor to link\nTeleport on the Anchor by right-clicking"),
            )
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

        /*UseBlockCallback.EVENT.register { player: PlayerEntity, world: World, hand: Hand, result: BlockHitResult ->
            val stack: ItemStack = player.getStackInHand(hand)
            if (stack.hasEnchantments() && world.getBlockState(result.blockPos).isOf(Blocks.CRYING_OBSIDIAN)) {
                stack.enchantments
                    .toLevelMap()
                    .map(EnchantedBookItem::forEnchantment)
                    .onEach { dropStackAt(player, it) }
                stack.remove(DataComponentTypes.ENCHANTMENTS)
                ActionResult.success(world.isClient)
            } else {
                ActionResult.PASS
            }
        }*/
    }

    @JvmStatic
    private fun addDescription(context: DefaultItemComponentEvents.ModifyContext, item: ItemConvertible, text: MutableText) {
        context.modify(item.asItem()) {
            it.add(RagiumComponentTypes.DESCRIPTION, text.formatted(Formatting.AQUA))
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
