package hiiragi283.ragium.common.internal

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.accessory.HTAccessoryRegistry
import hiiragi283.ragium.api.accessory.HTAccessorySlotTypes
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.fluid.HTFluidDrinkingHandlerRegistry
import hiiragi283.ragium.api.fluid.HTVirtualFluid
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
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.fluid.Fluids
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
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
            val block = Block(blockSettings(ore.baseStone))
            registerBlock(ore, block)
            registerBlockItem(
                block,
                itemSettings().material(ore.material, ore.tagPrefix),
            )
        }
        RagiumContents.StorageBlocks.entries.forEach { storage: RagiumContents.StorageBlocks ->
            val block = Block(blockSettings(Blocks.IRON_BLOCK))
            registerBlock(storage, block)
            registerBlockItem(
                block,
                itemSettings().material(storage.material, storage.tagPrefix),
            )
        }

        RagiumContents.Grates.entries.forEach { grate: RagiumContents.Grates ->
            val block = TransparentBlock(blockSettings(Blocks.COPPER_GRATE))
            registerBlock(grate, block)
            registerBlockItem(
                block,
                itemSettings().tieredText(RagiumTranslationKeys.GRATE, grate.tier),
            )
        }
        RagiumContents.Casings.entries.forEach { casings: RagiumContents.Casings ->
            val block = Block(blockSettings(Blocks.SMOOTH_STONE))
            registerBlock(casings, block)
            registerBlockItem(
                block,
                itemSettings().tieredText(RagiumTranslationKeys.CASING, casings.tier),
            )
        }
        RagiumContents.Hulls.entries.forEach { hull: RagiumContents.Hulls ->
            val block = TransparentBlock(blockSettings(Blocks.SMOOTH_STONE))
            registerBlock(hull, block)
            registerBlockItem(
                block,
                itemSettings().tieredText(RagiumTranslationKeys.HULL, hull.tier),
            )
        }
        RagiumContents.Coils.entries.forEach { coil: RagiumContents.Coils ->
            val block = PillarBlock(blockSettings(Blocks.COPPER_BLOCK))
            registerBlock(coil, block)
            registerBlockItem(
                block,
                itemSettings().tieredText(RagiumTranslationKeys.COIL, coil.tier),
            )
        }
        RagiumContents.Exporters.entries.forEach { exporter: RagiumContents.Exporters ->
            val block = HTExporterBlock(exporter.tier)
            registerBlock(exporter, block)
            registerBlockItem(
                block,
                itemSettings().tieredText(RagiumTranslationKeys.EXPORTER, exporter.tier),
            )
        }
        RagiumContents.Pipes.entries.forEach { pipe: RagiumContents.Pipes ->
            val block = HTPipeBlock(pipe.tier, pipe.pipeType)
            registerBlock(pipe, block)
            registerBlockItem(block, itemSettings().tier(pipe.tier))
        }
        RagiumContents.Drums.entries.forEach { drum: RagiumContents.Drums ->
            val block = HTDrumBlock(drum.tier)
            registerBlock(drum, block)
            registerBlockItem(
                block,
                itemSettings().tieredText(RagiumTranslationKeys.DRUM, drum.tier),
            )
        }
        initBlocks()

        // item
        buildList {
            addAll(RagiumContents.Dusts.entries)
            addAll(RagiumContents.Gems.entries)
            addAll(RagiumContents.Ingots.entries)
            addAll(RagiumContents.Plates.entries)
            addAll(RagiumContents.RawMaterials.entries)
        }.forEach { content: HTContent.Material<Item> ->
            registerItem(
                content,
                Item(
                    itemSettings().material(content.material, content.tagPrefix),
                ),
            )
        }
        RagiumContents.CircuitBoards.entries.forEach { board: RagiumContents.CircuitBoards ->
            registerItem(
                board,
                Item(itemSettings().tieredText(RagiumTranslationKeys.CIRCUIT_BOARD, board.tier)),
            )
        }
        RagiumContents.Circuits.entries.forEach { circuit: RagiumContents.Circuits ->
            registerItem(
                circuit,
                Item(itemSettings().tieredText(RagiumTranslationKeys.CIRCUIT_BOARD, circuit.tier)),
            )
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
        registerBlockItem(RagiumBlocks.SWEET_BERRIES_CAKE)

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
        registerItem("guide_book", RagiumItems.GUIDE_BOOK)
        registerItem("item_filter", RagiumItems.ITEM_FILTER)
        registerItem("ragi_wrench", RagiumItems.RAGI_WRENCH)
        registerItem("steel_axe", RagiumItems.STEEL_AXE)
        registerItem("steel_hoe", RagiumItems.STEEL_HOE)
        registerItem("steel_pickaxe", RagiumItems.STEEL_PICKAXE)
        registerItem("steel_shovel", RagiumItems.STEEL_SHOVEL)
        registerItem("steel_sword", RagiumItems.STEEL_SWORD)
        registerItem("trader_catalog", RagiumItems.TRADER_CATALOG)

        registerItem("sweet_berries_cake_piece", RagiumItems.SWEET_BERRIES_CAKE_PIECE)

        registerItem("butter", RagiumItems.BUTTER)
        registerItem("caramel", RagiumItems.CARAMEL)
        registerItem("dough", RagiumItems.DOUGH)
        registerItem("flour", RagiumItems.FLOUR)

        registerItem("chocolate", RagiumItems.CHOCOLATE)
        registerItem("chocolate_apple", RagiumItems.CHOCOLATE_APPLE)
        registerItem("chocolate_bread", RagiumItems.CHOCOLATE_BREAD)

        registerItem("minced_meat", RagiumItems.MINCED_MEAT)
        registerItem("meat_ingot", RagiumItems.MEAT_INGOT)
        registerItem("cooked_meat_ingot", RagiumItems.COOKED_MEAT_INGOT)

        registerItem("bee_wax", RagiumItems.BEE_WAX)
        registerItem("pulp", RagiumItems.PULP)
        registerItem("residual_coke", RagiumItems.RESIDUAL_COKE)

        registerItem("deepant", RagiumItems.DEEPANT)
        registerItem("luminescence_dust", RagiumItems.LUMINESCENCE_DUST)
        registerItem("ragi_alloy_compound", RagiumItems.RAGI_ALLOY_COMPOUND)
        registerItem("slag", RagiumItems.SLAG)
        registerItem("soap_ingot", RagiumItems.SOAP_INGOT)

        registerItem("polymer_resin", RagiumItems.POLYMER_RESIN)
        registerItem("plastic_plate", RagiumItems.PLASTIC_PLATE)
        registerItem("engineering_plastic_plate", RagiumItems.ENGINEERING_PLASTIC_PLATE)
        registerItem("stella_plate", RagiumItems.STELLA_PLATE)

        registerItem("crude_silicon", RagiumItems.CRUDE_SILICON)
        registerItem("silicon", RagiumItems.SILICON)
        registerItem("refined_silicon", RagiumItems.REFINED_SILICON)

        registerItem("crimson_crystal", RagiumItems.CRIMSON_CRYSTAL)
        registerItem("warped_crystal", RagiumItems.WARPED_CRYSTAL)

        registerItem("basalt_mesh", RagiumItems.BASALT_MESH)
        registerItem("blazing_carbon_electrode", RagiumItems.BLAZING_CARBON_ELECTRODE)
        registerItem("carbon_electrode", RagiumItems.CARBON_ELECTRODE)
        registerItem("charged_carbon_electrode", RagiumItems.CHARGED_CARBON_ELECTRODE)
        registerItem("engine", RagiumItems.ENGINE)
        registerItem("laser_emitter", RagiumItems.LASER_EMITTER)
        registerItem("processor_socket", RagiumItems.PROCESSOR_SOCKET)
        registerItem("ragi_crystal_processor", RagiumItems.RAGI_CRYSTAL_PROCESSOR)
        registerItem("solar_panel", RagiumItems.SOLAR_PANEL)

        registerItem("uranium_fuel", RagiumItems.URANIUM_FUEL)
        registerItem("yellow_cake", RagiumItems.YELLOW_CAKE)
        registerItem("yellow_cake_piece", RagiumItems.YELLOW_CAKE_PIECE)

        registerItem("ragi_ticket", RagiumItems.RAGI_TICKET)
    }

    @JvmStatic
    fun initRegistry() {
        // ApiLookup
        ItemStorage.SIDED.registerForBlocks({ world: World, _: BlockPos, state: BlockState, _: BlockEntity?, direction: Direction? ->
            val color: DyeColor = state.getOrNull(RagiumBlockProperties.COLOR) ?: return@registerForBlocks null
            world.backpackManager
                .map { it[color] }
                .map { InventoryStorage.of(it, direction) }
                .result()
                .getOrNull()
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
            world.energyNetwork.result().getOrNull()
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
}
