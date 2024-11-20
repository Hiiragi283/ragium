package hiiragi283.ragium.common.internal

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.accessory.HTAccessoryRegistry
import hiiragi283.ragium.api.accessory.HTAccessorySlotTypes
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.fluid.HTFluidDrinkingHandlerRegistry
import hiiragi283.ragium.api.fluid.HTVirtualFluid
import hiiragi283.ragium.api.material.HTTagPrefixedBlock
import hiiragi283.ragium.api.material.HTTagPrefixedBlockItem
import hiiragi283.ragium.api.material.HTTagPrefixedItem
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.block.HTDrumBlock
import hiiragi283.ragium.common.block.HTExporterBlock
import hiiragi283.ragium.common.block.HTPipeBlock
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.storage.HTEmptyFluidCubeStorage
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.fabricmc.fabric.api.transfer.v1.fluid.*
import net.fabricmc.fabric.api.transfer.v1.fluid.base.FullItemFluidStorage
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleItemStorage
import net.fabricmc.fabric.api.transfer.v1.storage.base.InsertionOnlyStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.minecraft.block.*
import net.minecraft.block.cauldron.CauldronBehavior
import net.minecraft.block.entity.BlockEntity
import net.minecraft.component.type.FoodComponent
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.fluid.Fluids
import net.minecraft.item.*
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.text.Text
import net.minecraft.util.DyeColor
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import team.reborn.energy.api.EnergyStorage
import team.reborn.energy.api.base.InfiniteEnergyStorage
import kotlin.jvm.optionals.getOrNull

internal object RagiumContentRegister {
    fun <T : Any> register(registry: Registry<in T>, name: String, value: (T)): T = Registry.register(registry, RagiumAPI.id(name), value)

    //    Block    //

    fun <T : Block> registerBlock(name: String, block: T): T = register(Registries.BLOCK, name, block)

    fun registerBlock(content: HTContent<Block>, block: Block): Block = registerBlock(content.id.path, block)

    //    Item    //

    fun <T : Item> registerItem(name: String, item: T): T = register(Registries.ITEM, name, item)

    fun registerItem(content: HTContent<Item>, item: Item): Item = registerItem(content.id.path, item)

    fun <T : Block> registerBlockItem(
        block: T,
        settings: Item.Settings = itemSettings(),
        factory: (T, Item.Settings) -> Item = ::BlockItem,
    ) {
        Registries.BLOCK
            .getKey(block)
            .getOrNull()
            ?.value
            ?.let { registerItem(it.path, factory(block, settings)) }
    }

    //    Init    //

    @JvmStatic
    fun registerContents() {
        // block
        RagiumContents.Ores.entries.forEach { ore: RagiumContents.Ores ->
            val block = HTTagPrefixedBlock(ore.tagPrefix, ore.material, blockSettings(ore.baseStone))
            registerBlock(ore, block)
            registerBlockItem(block, itemSettings(), ::HTTagPrefixedBlockItem)
        }
        RagiumContents.StorageBlocks.entries.forEach { storage: RagiumContents.StorageBlocks ->
            val block = HTTagPrefixedBlock(storage.tagPrefix, storage.material, blockSettings(Blocks.IRON_BLOCK))
            registerBlock(storage, block)
            registerBlockItem(block, itemSettings(), ::HTTagPrefixedBlockItem)
        }

        RagiumContents.Grates.entries.forEach { grate: RagiumContents.Grates ->
            val block = TransparentBlock(blockSettings(Blocks.COPPER_GRATE))
            registerBlock(grate, block)
            registerBlockItem(block, itemSettings().tier(grate.tier))
        }
        RagiumContents.Casings.entries.forEach { casings: RagiumContents.Casings ->
            val block = Block(blockSettings(Blocks.SMOOTH_STONE))
            registerBlock(casings, block)
            registerBlockItem(block, itemSettings().tier(casings.tier))
        }
        RagiumContents.Hulls.entries.forEach { hull: RagiumContents.Hulls ->
            val block = TransparentBlock(blockSettings(Blocks.SMOOTH_STONE))
            registerBlock(hull, block)
            registerBlockItem(block, itemSettings().tier(hull.tier))
        }
        RagiumContents.Coils.entries.forEach { coil: RagiumContents.Coils ->
            val block = PillarBlock(blockSettings(Blocks.COPPER_BLOCK))
            registerBlock(coil, block)
            registerBlockItem(block, itemSettings().tier(coil.tier))
        }
        RagiumContents.Exporters.entries.forEach { exporter: RagiumContents.Exporters ->
            val block = HTExporterBlock(exporter.tier)
            registerBlock(exporter, block)
            registerBlockItem(block, itemSettings().tier(exporter.tier))
        }
        RagiumContents.Pipes.entries.forEach { pipe: RagiumContents.Pipes ->
            val block = HTPipeBlock(pipe.tier, pipe.pipeType)
            registerBlock(pipe, block)
            registerBlockItem(block, itemSettings().tier(pipe.tier))
        }
        RagiumContents.Drums.entries.forEach { drum: RagiumContents.Drums ->
            val block = HTDrumBlock(drum.tier)
            registerBlock(drum, block)
            registerBlockItem(block, itemSettings().tier(drum.tier))
        }
        initBlocks()

        // item
        buildList {
            addAll(RagiumContents.Dusts.entries)
            addAll(RagiumContents.Gems.entries)
            addAll(RagiumContents.Ingots.entries)
            addAll(RagiumContents.Plates.entries)
            addAll(RagiumContents.RawMaterials.entries)
            addAll(RagiumContents.CircuitBoards.entries)
            addAll(RagiumContents.Circuits.entries)
        }.forEach { content: HTContent<Item> ->
            if (content is HTContent.Material<Item>) {
                registerItem(content, HTTagPrefixedItem(content.tagPrefix, content.material, itemSettings()))
            } else {
                registerItem(content, Item(itemSettings()))
            }
        }
        initItems()

        // fluid
        RagiumFluids.entries.forEach { fluid: RagiumFluids ->
            Registry.register(Registries.FLUID, fluid.id, HTVirtualFluid())
        }
    }

    @JvmStatic
    private fun initBlocks() {
        registerBlock("porous_netherrack", RagiumBlocks.POROUS_NETHERRACK)
        registerBlockItem(
            RagiumBlocks.POROUS_NETHERRACK,
            itemSettings().descriptions(Text.translatable(RagiumTranslationKeys.POROUS_NETHERRACK)),
        )

        registerBlock("asphalt", RagiumBlocks.ASPHALT)
        registerBlockItem(RagiumBlocks.ASPHALT)

        registerBlock("sponge_cake", RagiumBlocks.SPONGE_CAKE)
        registerBlock("sweet_berries_cake", RagiumBlocks.SWEET_BERRIES_CAKE)
        registerBlockItem(
            RagiumBlocks.SPONGE_CAKE,
            itemSettings().descriptions(Text.translatable(RagiumTranslationKeys.SPONGE_CAKE)),
        )
        registerBlockItem(
            RagiumBlocks.SWEET_BERRIES_CAKE,
            itemSettings()
                .food(
                    FoodComponent
                        .Builder()
                        .nutrition(2)
                        .saturationModifier(0.1f)
                        .build(),
                ).maxDamage(7)
                .component(RagiumComponentTypes.DAMAGE_INSTEAD_OF_DECREASE, Unit),
        )

        registerBlock("auto_illuminator", RagiumBlocks.AUTO_ILLUMINATOR)
        registerBlock("creative_source", RagiumBlocks.CREATIVE_SOURCE)
        registerBlock("large_processor", RagiumBlocks.LARGE_PROCESSOR)
        registerBlock("manual_forge", RagiumBlocks.MANUAL_FORGE)
        registerBlock("manual_grinder", RagiumBlocks.MANUAL_GRINDER)
        registerBlock("manual_mixer", RagiumBlocks.MANUAL_MIXER)
        registerBlock("network_interface", RagiumBlocks.NETWORK_INTERFACE)
        registerBlock("open_crate", RagiumBlocks.OPEN_CRATE)
        registerBlock("teleport_anchor", RagiumBlocks.TELEPORT_ANCHOR)
        registerBlock("trash_box", RagiumBlocks.TRASH_BOX)
        registerBlockItem(
            RagiumBlocks.AUTO_ILLUMINATOR,
            itemSettings().descriptions(
                Text.translatable(
                    RagiumTranslationKeys.AUTO_ILLUMINATOR,
                    RagiumAPI.getInstance().config.autoIlluminatorRadius,
                ),
            ),
        )
        registerBlockItem(RagiumBlocks.CREATIVE_SOURCE)
        registerBlockItem(
            RagiumBlocks.LARGE_PROCESSOR,
            itemSettings().descriptions(Text.translatable(RagiumTranslationKeys.LARGE_PROCESSOR)),
        )
        registerBlockItem(RagiumBlocks.MANUAL_FORGE)
        registerBlockItem(
            RagiumBlocks.MANUAL_GRINDER,
            itemSettings().descriptions(Text.translatable(RagiumTranslationKeys.MANUAL_GRINDER)),
        )
        registerBlockItem(RagiumBlocks.MANUAL_MIXER)
        registerBlockItem(
            RagiumBlocks.NETWORK_INTERFACE,
            itemSettings().descriptions(Text.translatable(RagiumTranslationKeys.NETWORK_INTERFACE)),
        )
        registerBlockItem(
            RagiumBlocks.OPEN_CRATE,
            itemSettings().descriptions(Text.translatable(RagiumTranslationKeys.OPEN_CRATE)),
        )
        registerBlockItem(RagiumBlocks.TELEPORT_ANCHOR)
        registerBlockItem(
            RagiumBlocks.TRASH_BOX,
            itemSettings().descriptions(Text.translatable(RagiumTranslationKeys.TRASH_BOX)),
        )

        registerBlock("backpack_interface", RagiumBlocks.BACKPACK_INTERFACE)
        registerBlock("buffer", RagiumBlocks.BUFFER)
        registerBlock("enchantment_bookshelf", RagiumBlocks.ENCHANTMENT_BOOKSHELF)
        registerBlock("item_display", RagiumBlocks.ITEM_DISPLAY)
        registerBlock("shaft", RagiumBlocks.SHAFT)
        registerBlock("infesting", RagiumBlocks.INFESTING)
        registerBlockItem(RagiumBlocks.BACKPACK_INTERFACE)
        registerBlockItem(RagiumBlocks.BUFFER)
        registerBlockItem(RagiumBlocks.ENCHANTMENT_BOOKSHELF)
        registerBlockItem(RagiumBlocks.ITEM_DISPLAY)
        registerBlockItem(RagiumBlocks.SHAFT)
    }

    @JvmStatic
    private fun initItems() {
        registerItem("steel_helmet", RagiumItems.STEEL_HELMET)
        registerItem("steel_chestplate", RagiumItems.STEEL_CHESTPLATE)
        registerItem("steel_leggings", RagiumItems.STEEL_LEGGINGS)
        registerItem("steel_boots", RagiumItems.STEEL_BOOTS)
        registerItem("stella_goggle", RagiumItems.STELLA_GOGGLE)
        registerItem("stella_jacket", RagiumItems.STELLA_JACKET)
        registerItem("stella_leggings", RagiumItems.STELLA_LEGGINGS)
        registerItem("stella_boots", RagiumItems.STELLA_BOOTS)

        registerItem("anvil_dynamite", RagiumItems.ANVIL_DYNAMITE)
        registerItem("backpack", RagiumItems.BACKPACK)
        registerItem("bedrock_dynamite", RagiumItems.BEDROCK_DYNAMITE)
        registerItem("bujin", RagiumItems.BUJIN)
        registerItem("dynamite", RagiumItems.DYNAMITE)
        registerItem("empty_fluid_cube", RagiumItems.EMPTY_FLUID_CUBE)
        registerItem("filled_fluid_cube", RagiumItems.FILLED_FLUID_CUBE)
        registerItem("flattening_dynamite", RagiumItems.FLATTENING_DYNAMITE)
        registerItem("fluid_filter", RagiumItems.FLUID_FILTER)
        registerItem("forge_hammer", RagiumItems.FORGE_HAMMER)
        registerItem("gigant_hammer", RagiumItems.GIGANT_HAMMER)
        registerItem("item_filter", RagiumItems.ITEM_FILTER)
        registerItem("steel_axe", RagiumItems.STEEL_AXE)
        registerItem("steel_hoe", RagiumItems.STEEL_HOE)
        registerItem("steel_pickaxe", RagiumItems.STEEL_PICKAXE)
        registerItem("steel_shovel", RagiumItems.STEEL_SHOVEL)
        registerItem("steel_sword", RagiumItems.STEEL_SWORD)
        registerItem("trader_catalog", RagiumItems.TRADER_CATALOG)

        registerItem("bee_wax", RagiumItems.BEE_WAX)
        registerItem("butter", RagiumItems.BUTTER)
        registerItem("caramel", RagiumItems.CARAMEL)
        registerItem("chocolate", RagiumItems.CHOCOLATE)
        registerItem("chocolate_apple", RagiumItems.CHOCOLATE_APPLE)
        registerItem("chocolate_bread", RagiumItems.CHOCOLATE_BREAD)
        registerItem("flour", RagiumItems.FLOUR)
        registerItem("dough", RagiumItems.DOUGH)
        registerItem("minced_meat", RagiumItems.MINCED_MEAT)
        registerItem("pulp", RagiumItems.PULP)

        registerItem("basalt_mesh", RagiumItems.BASALT_MESH)
        registerItem("crimson_crystal", RagiumItems.CRIMSON_CRYSTAL)
        registerItem("crude_silicon", RagiumItems.CRUDE_SILICON)
        registerItem("deepant", RagiumItems.DEEPANT)
        registerItem("engine", RagiumItems.ENGINE)
        registerItem("engineering_plastic_plate", RagiumItems.ENGINEERING_PLASTIC_PLATE)
        registerItem("heart_of_the_nether", RagiumItems.HEART_OF_THE_NETHER)
        registerItem("laser_emitter", RagiumItems.LASER_EMITTER)
        registerItem("luminescence_dust", RagiumItems.LUMINESCENCE_DUST)
        registerItem("plastic_plate", RagiumItems.PLASTIC_PLATE)
        registerItem("polymer_resin", RagiumItems.POLYMER_RESIN)
        registerItem("processor_socket", RagiumItems.PROCESSOR_SOCKET)
        registerItem("ragi_alloy_compound", RagiumItems.RAGI_ALLOY_COMPOUND)
        registerItem("ragi_crystal_processor", RagiumItems.RAGI_CRYSTAL_PROCESSOR)
        registerItem("ragi_ticket", RagiumItems.RAGI_TICKET)
        registerItem("ragi_wrench", RagiumItems.RAGI_WRENCH)
        registerItem("refined_silicon", RagiumItems.REFINED_SILICON)
        registerItem("residual_coke", RagiumItems.RESIDUAL_COKE)
        registerItem("silicon", RagiumItems.SILICON)
        registerItem("slag", RagiumItems.SLAG)
        registerItem("soap_ingot", RagiumItems.SOAP_INGOT)
        registerItem("solar_panel", RagiumItems.SOLAR_PANEL)
        registerItem("stella_plate", RagiumItems.STELLA_PLATE)
        registerItem("warped_crystal", RagiumItems.WARPED_CRYSTAL)
    }

    @JvmStatic
    fun initRegistry() {
        // ApiLookup
        ItemStorage.SIDED.registerForBlocks({ world: World, _: BlockPos, state: BlockState, _: BlockEntity?, direction: Direction? ->
            val color: DyeColor = state.getOrNull(RagiumBlockProperties.COLOR) ?: return@registerForBlocks null
            world.backpackManager
                ?.get(color)
                ?.let { InventoryStorage.of(it, direction) }
        }, RagiumBlocks.BACKPACK_INTERFACE)
        ItemStorage.SIDED.registerForBlocks({ _: World, _: BlockPos, _: BlockState, _: BlockEntity?, _: Direction? ->
            object : SingleItemStorage() {
                override fun getCapacity(variant: ItemVariant): Long = Long.MAX_VALUE
            }
        }, RagiumBlocks.TRASH_BOX)
        ItemStorage.SIDED.registerForBlocks({ world: World, pos: BlockPos, _: BlockState, _: BlockEntity?, _: Direction? ->
            InsertionOnlyStorage { resource: ItemVariant, maxAmount: Long, _: TransactionContext ->
                if (dropStackAt(world, pos.down(), resource.toStack(maxAmount.toInt()))) maxAmount else 0
            }
        }, RagiumBlocks.OPEN_CRATE)

        FluidStorage
            .combinedItemApiProvider(RagiumItems.EMPTY_FLUID_CUBE)
            .register(::HTEmptyFluidCubeStorage)
        FluidStorage.GENERAL_COMBINED_PROVIDER.register { context: ContainerItemContext ->
            if (context.itemVariant.isOf(RagiumItems.FILLED_FLUID_CUBE)) {
                context
                    .itemVariant
                    .componentMap
                    .get(RagiumComponentTypes.FLUID)
                    ?.let {
                        FullItemFluidStorage(
                            context,
                            RagiumItems.EMPTY_FLUID_CUBE,
                            FluidVariant.of(it),
                            FluidConstants.BUCKET,
                        )
                    }
                    ?: return@register null
            } else {
                null
            }
        }
        FluidStorage.SIDED.registerForBlocks({ _: World, _: BlockPos, _: BlockState, _: BlockEntity?, _: Direction? ->
            fluidStorageOf(Long.MAX_VALUE)
        }, RagiumBlocks.TRASH_BOX)

        EnergyStorage.SIDED.registerForBlocks(
            { _: World, _: BlockPos, _: BlockState, _: BlockEntity?, _: Direction? -> InfiniteEnergyStorage.INSTANCE },
            RagiumBlocks.CREATIVE_SOURCE,
        )
        EnergyStorage.SIDED.registerForBlocks({ world: World, _: BlockPos, _: BlockState, _: BlockEntity?, _: Direction? ->
            world.energyNetwork
        }, RagiumBlocks.NETWORK_INTERFACE)

        // Accessory
        HTAccessoryRegistry.register(RagiumItems.STELLA_GOGGLE) {
            equippedAction = HTAccessoryRegistry.EquippedAction {
                it.addStatusEffect(StatusEffectInstance(StatusEffects.NIGHT_VISION, -1, 0))
            }
            unequippedAction = HTAccessoryRegistry.UnequippedAction {
                it.removeStatusEffect(StatusEffects.NIGHT_VISION)
            }
            slotType = HTAccessorySlotTypes.FACE
        }
        // Cauldron
        /*registerCauldron(
            CauldronBehavior.WATER_CAULDRON_BEHAVIOR,
            RagiumContents.Dusts.CRUDE_RAGINITE,
        ) { state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, _: Hand, stack: ItemStack ->
            if (stack.isOf(RagiumContents.Dusts.CRUDE_RAGINITE)) {
                if (!world.isClient) {
                    val count: Int = stack.count
                    stack.count = -1
                    dropStackAt(player, ItemStack(RagiumContents.Dusts.RAGINITE, count))
                    LeveledCauldronBlock.decrementFluidLevel(state, world, pos)
                }
                ItemActionResult.success(world.isClient)
            } else {
                ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
            }
        }*/
        // Dispenser
        DispenserBlock.registerProjectileBehavior(RagiumItems.BEDROCK_DYNAMITE)
        DispenserBlock.registerProjectileBehavior(RagiumItems.DYNAMITE)
        DispenserBlock.registerProjectileBehavior(RagiumItems.FLATTENING_DYNAMITE)
        // Fluid Attributes
        RagiumFluids.entries.forEach { fluid: RagiumFluids ->
            FluidVariantAttributes.register(
                fluid.value,
                object : FluidVariantAttributeHandler {
                    override fun getName(fluidVariant: FluidVariant): Text = Text.translatable(fluid.translationKey)
                },
            )
        }

        // HTFluidDrinkingHandlerRegistry
        HTFluidDrinkingHandlerRegistry.register(Fluids.LAVA) { _: ItemStack, world: World, user: LivingEntity ->
            if (!world.isClient) {
                user.setOnFireFromLava()
                dropStackAt(user, Items.OBSIDIAN.defaultStack)
            }
        }
        HTFluidDrinkingHandlerRegistry.register(RagiumFluids.MILK) { _: ItemStack, world: World, user: LivingEntity ->
            if (!world.isClient) {
                user.clearStatusEffects()
            }
        }
        HTFluidDrinkingHandlerRegistry.register(RagiumFluids.HONEY) { _: ItemStack, world: World, user: LivingEntity ->
            if (!world.isClient) {
                user.removeStatusEffect(StatusEffects.POISON)
            }
        }
        HTFluidDrinkingHandlerRegistry.register(RagiumFluids.CHOCOLATE) { _: ItemStack, world: World, user: LivingEntity ->
            if (!world.isClient) {
                user.addStatusEffect(
                    StatusEffectInstance(StatusEffects.STRENGTH, 20 * 5, 1),
                )
            }
        }
    }

    @JvmStatic
    private fun registerCauldron(map: CauldronBehavior.CauldronBehaviorMap, item: ItemConvertible, behavior: CauldronBehavior) {
        map.map[item.asItem()] = behavior
    }
}
