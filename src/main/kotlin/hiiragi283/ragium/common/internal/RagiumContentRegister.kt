package hiiragi283.ragium.common.internal

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.accessory.HTAccessoryRegistry
import hiiragi283.ragium.api.accessory.HTAccessorySlotTypes
import hiiragi283.ragium.api.block.HTBlockWithEntity
import hiiragi283.ragium.api.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.content.HTItemContent
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.fluid.HTFluidDrinkingHandler
import hiiragi283.ragium.api.fluid.HTFluidDrinkingHandlerRegistry
import hiiragi283.ragium.api.fluid.HTVirtualFluid
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.storage.HTVoidStorage
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.block.storage.HTCrateBlock
import hiiragi283.ragium.common.block.storage.HTDrumBlock
import hiiragi283.ragium.common.block.transfer.*
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.item.HTRopeBlockItem
import hiiragi283.ragium.common.storage.HTEmptyFluidCubeStorage
import hiiragi283.ragium.common.storage.HTTieredFluidItemStorage
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.fabricmc.fabric.api.transfer.v1.fluid.*
import net.fabricmc.fabric.api.transfer.v1.fluid.base.FullItemFluidStorage
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage
import net.fabricmc.fabric.api.transfer.v1.storage.base.InsertionOnlyStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.DyeColor
import net.minecraft.util.Rarity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import team.reborn.energy.api.EnergyStorage
import team.reborn.energy.api.base.InfiniteEnergyStorage
import kotlin.jvm.optionals.getOrNull

internal object RagiumContentRegister {
    private fun <T : Any> register(registry: Registry<in T>, name: String, value: (T)): T =
        Registry.register(registry, RagiumAPI.id(name), value)

    //    Block    //

    private fun <T : Block> registerBlock(name: String, block: T): T = register(Registries.BLOCK, name, block)

    private fun registerBlock(content: HTBlockContent, block: Block): Block = registerBlock(content.id.path, block)

    private fun registerBlockNew(content: HTBlockContent, block: (AbstractBlock.Settings) -> Block): Block =
        Registry.register(Registries.BLOCK, content.id, block(blockSettings()))

    //    Item    //

    private fun <T : Item> registerItem(name: String, item: T): T = register(Registries.ITEM, name, item)

    private fun registerItem(content: HTItemContent, item: Item): Item = registerItem(content.id.path, item)

    private fun registerItemNew(content: HTItemContent, item: (Item.Settings) -> Item): Item =
        Registry.register(Registries.ITEM, content.id, item(itemSettings()))

    private fun <T : Block> registerBlockItem(
        block: T,
        settings: Item.Settings = itemSettings(),
        factory: (T, Item.Settings) -> Item = ::BlockItem,
    ) {
        registerItem(
            Registries.BLOCK
                .getKeyOrThrow(block)
                .value
                .path,
            factory(block, settings),
        )
    }

    private fun registerBlockItemNew(
        block: HTBlockContent,
        settings: (Item.Settings) -> Item.Settings = { it },
        factory: (Block, Item.Settings) -> Item = ::BlockItem,
    ) {
        Registry.register(Registries.ITEM, block.id, factory(block.get(), settings(itemSettings())))
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
            val block = HTSimplePipeBlock(pipe.tier, pipe.pipeType)
            registerBlock(pipe, block)
            registerBlockItem(block, itemSettings().tier(pipe.tier))
        }
        RagiumContents.CrossPipes.entries.forEach { crossPipe: RagiumContents.CrossPipes ->
            val block = HTPipeBlock()
            registerBlock(crossPipe, block)
            registerBlockItem(block)
        }
        RagiumContents.PipeStations.entries.forEach { station: RagiumContents.PipeStations ->
            val block = HTPipeStationBlock()
            registerBlock(station, block)
            registerBlockItem(block, itemSettings().descriptions(Text.translatable(RagiumTranslationKeys.PIPE_STATION)))
        }
        RagiumContents.FilteringPipe.entries.forEach { filtering: RagiumContents.FilteringPipe ->
            val block = HTFilteringPipeBlock(filtering.pipeType)
            registerBlock(filtering, block)
            registerBlockItem(
                block,
                itemSettings()
                    .descriptions(Text.translatable(RagiumTranslationKeys.PIPE_STATION))
                    .component(RagiumComponentTypes.REWORK_TARGET, Unit),
            )
        }
        RagiumContents.Crates.entries.forEach { crate: RagiumContents.Crates ->
            val block = HTCrateBlock(crate.tier)
            registerBlock(crate, block)
            registerBlockItem(
                block,
                itemSettings().tieredText(RagiumTranslationKeys.CRATE, crate.tier),
            )
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
            addAll(RagiumContents.Gears.entries)
            addAll(RagiumContents.Gems.entries)
            addAll(RagiumContents.Ingots.entries)
            addAll(RagiumContents.Plates.entries)
            addAll(RagiumContents.RawMaterials.entries)
        }.forEach { content ->
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
                Item(itemSettings().tieredText(RagiumTranslationKeys.CIRCUIT, circuit.tier)),
            )
        }

        RagiumContents.PressMolds.entries.forEach { mold: RagiumContents.PressMolds ->
            registerItem(mold, Item(itemSettings()))
        }

        initItems()

        // fluid
        RagiumFluids.entries.forEach { fluid: RagiumFluids ->
            Registry.register(Registries.FLUID, fluid.id, HTVirtualFluid())
        }
    }

    @JvmStatic
    private fun initBlocks() {
        registerBlockNew(RagiumBlocksNew.CREATIVE_CRATE) {
            HTBlockWithEntity.buildHorizontal(RagiumBlockEntityTypes.CREATIVE_CRATE, it)
        }
        registerBlockNew(RagiumBlocksNew.CREATIVE_DRUM) {
            HTBlockWithEntity.build(RagiumBlockEntityTypes.CREATIVE_DRUM, it)
        }
        registerBlockNew(RagiumBlocksNew.CREATIVE_EXPORTER) {
            HTCreativeExporterBlock(it.solid().nonOpaque().strength(2f, 6f))
        }
        registerBlockNew(RagiumBlocksNew.CREATIVE_SOURCE) {
            HTBlockWithEntity.build(RagiumBlockEntityTypes.CREATIVE_SOURCE, it)
        }
        registerBlockItemNew(RagiumBlocksNew.CREATIVE_CRATE, settings = { it.rarity(Rarity.EPIC) })
        registerBlockItemNew(RagiumBlocksNew.CREATIVE_DRUM, settings = { it.rarity(Rarity.EPIC) })
        registerBlockItemNew(RagiumBlocksNew.CREATIVE_EXPORTER, settings = { it.rarity(Rarity.EPIC) })
        registerBlockItemNew(RagiumBlocksNew.CREATIVE_SOURCE, settings = { it.rarity(Rarity.EPIC) })

        registerBlock("mutated_soil", RagiumBlocks.MUTATED_SOIL)
        registerBlock("porous_netherrack", RagiumBlocks.POROUS_NETHERRACK)
        registerBlockItem(
            RagiumBlocks.MUTATED_SOIL,
            itemSettings().descriptions(Text.translatable(RagiumTranslationKeys.MUTATED_SOIL)),
        )
        registerBlockItem(
            RagiumBlocks.POROUS_NETHERRACK,
            itemSettings().descriptions(Text.translatable(RagiumTranslationKeys.POROUS_NETHERRACK)),
        )

        registerBlock("asphalt", RagiumBlocks.ASPHALT)
        registerBlock("asphalt_slab", RagiumBlocks.ASPHALT_SLAB)
        registerBlock("asphalt_stairs", RagiumBlocks.ASPHALT_STAIRS)
        registerBlock("polished_asphalt", RagiumBlocks.POLISHED_ASPHALT)
        registerBlock("polished_asphalt_slab", RagiumBlocks.POLISHED_ASPHALT_SLAB)
        registerBlock("polished_asphalt_stairs", RagiumBlocks.POLISHED_ASPHALT_STAIRS)
        registerBlock("gypsum", RagiumBlocks.GYPSUM)
        registerBlock("gypsum_slab", RagiumBlocks.GYPSUM_SLAB)
        registerBlock("gypsum_stairs", RagiumBlocks.GYPSUM_STAIRS)
        registerBlock("polished_gypsum", RagiumBlocks.POLISHED_GYPSUM)
        registerBlock("polished_gypsum_slab", RagiumBlocks.POLISHED_GYPSUM_SLAB)
        registerBlock("polished_gypsum_stairs", RagiumBlocks.POLISHED_GYPSUM_STAIRS)
        registerBlock("slate", RagiumBlocks.SLATE)
        registerBlock("slate_slab", RagiumBlocks.SLATE_SLAB)
        registerBlock("slate_stairs", RagiumBlocks.SLATE_STAIRS)
        registerBlock("polished_slate", RagiumBlocks.POLISHED_SLATE)
        registerBlock("polished_slate_slab", RagiumBlocks.POLISHED_SLATE_SLAB)
        registerBlock("polished_slate_stairs", RagiumBlocks.POLISHED_SLATE_STAIRS)
        registerBlock("white_line", RagiumBlocks.WHITE_LINE)
        registerBlock("t_white_line", RagiumBlocks.T_WHITE_LINE)
        registerBlock("cross_white_line", RagiumBlocks.CROSS_WHITE_LINE)
        registerBlock("steel_glass", RagiumBlocks.STEEL_GLASS)
        registerBlock("ragium_glass", RagiumBlocks.RAGIUM_GLASS)
        registerBlockItem(RagiumBlocks.ASPHALT)
        registerBlockItem(RagiumBlocks.ASPHALT_SLAB)
        registerBlockItem(RagiumBlocks.ASPHALT_STAIRS)
        registerBlockItem(RagiumBlocks.POLISHED_ASPHALT)
        registerBlockItem(RagiumBlocks.POLISHED_ASPHALT_SLAB)
        registerBlockItem(RagiumBlocks.POLISHED_ASPHALT_STAIRS)
        registerBlockItem(RagiumBlocks.GYPSUM)
        registerBlockItem(RagiumBlocks.GYPSUM_SLAB)
        registerBlockItem(RagiumBlocks.GYPSUM_STAIRS)
        registerBlockItem(RagiumBlocks.POLISHED_GYPSUM)
        registerBlockItem(RagiumBlocks.POLISHED_GYPSUM_SLAB)
        registerBlockItem(RagiumBlocks.POLISHED_GYPSUM_STAIRS)
        registerBlockItem(RagiumBlocks.SLATE)
        registerBlockItem(RagiumBlocks.SLATE_SLAB)
        registerBlockItem(RagiumBlocks.SLATE_STAIRS)
        registerBlockItem(RagiumBlocks.POLISHED_SLATE)
        registerBlockItem(RagiumBlocks.POLISHED_SLATE_SLAB)
        registerBlockItem(RagiumBlocks.POLISHED_SLATE_STAIRS)
        registerBlockItem(RagiumBlocks.WHITE_LINE)
        registerBlockItem(RagiumBlocks.T_WHITE_LINE)
        registerBlockItem(RagiumBlocks.CROSS_WHITE_LINE)
        registerBlockItem(RagiumBlocks.STEEL_GLASS)
        registerBlockItem(RagiumBlocks.RAGIUM_GLASS)

        registerBlock("sponge_cake", RagiumBlocks.SPONGE_CAKE)
        registerBlock("sweet_berries_cake", RagiumBlocks.SWEET_BERRIES_CAKE)
        registerBlockItem(
            RagiumBlocks.SPONGE_CAKE,
            itemSettings().descriptions(Text.translatable(RagiumTranslationKeys.SPONGE_CAKE)),
        )
        registerBlockItem(RagiumBlocks.SWEET_BERRIES_CAKE)

        registerBlock("auto_illuminator", RagiumBlocks.AUTO_ILLUMINATOR)
        registerBlock("extended_processor", RagiumBlocks.EXTENDED_PROCESSOR)
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
        registerBlockItem(
            RagiumBlocks.EXTENDED_PROCESSOR,
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
        // registerBlock("buffer", RagiumBlocks.BUFFER)
        registerBlock("enchantment_bookshelf", RagiumBlocks.ENCHANTMENT_BOOKSHELF)
        registerBlock("item_display", RagiumBlocks.ITEM_DISPLAY)
        registerBlock("shaft", RagiumBlocks.SHAFT)
        registerBlock("rope", RagiumBlocks.ROPE)
        registerBlock("infesting", RagiumBlocks.INFESTING)
        registerBlockItem(
            RagiumBlocks.BACKPACK_INTERFACE,
            itemSettings().component(RagiumComponentTypes.REWORK_TARGET, Unit),
        )
        /*registerBlockItem(
            RagiumBlocks.BUFFER,
            itemSettings().component(RagiumComponentTypes.REWORK_TARGET, Unit),
        )*/
        registerBlockItem(
            RagiumBlocks.ENCHANTMENT_BOOKSHELF,
            itemSettings().component(RagiumComponentTypes.REWORK_TARGET, Unit),
        )
        registerBlockItem(RagiumBlocks.ITEM_DISPLAY)
        registerBlockItem(
            RagiumBlocks.ROPE,
            itemSettings()
                .descriptions(Text.translatable(RagiumTranslationKeys.ROPE))
                .component(RagiumComponentTypes.REWORK_TARGET, Unit),
            ::HTRopeBlockItem,
        )
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

        registerItem("forge_hammer", RagiumItems.FORGE_HAMMER)
        registerItem("steel_axe", RagiumItems.STEEL_AXE)
        registerItem("steel_hoe", RagiumItems.STEEL_HOE)
        registerItem("steel_pickaxe", RagiumItems.STEEL_PICKAXE)
        registerItem("steel_shovel", RagiumItems.STEEL_SHOVEL)
        registerItem("steel_sword", RagiumItems.STEEL_SWORD)
        registerItem("stella_saber", RagiumItems.STELLA_SABER)
        registerItem("ragium_saber", RagiumItems.RAGIUM_SABER)
        registerItem("gigant_hammer", RagiumItems.GIGANT_HAMMER)

        registerItem("dynamite", RagiumItems.DYNAMITE)
        registerItem("anvil_dynamite", RagiumItems.ANVIL_DYNAMITE)
        registerItem("bedrock_dynamite", RagiumItems.BEDROCK_DYNAMITE)
        registerItem("flattening_dynamite", RagiumItems.FLATTENING_DYNAMITE)

        registerItem("backpack", RagiumItems.BACKPACK)
        registerItem("empty_fluid_cube", RagiumItems.EMPTY_FLUID_CUBE)
        registerItem("filled_fluid_cube", RagiumItems.FILLED_FLUID_CUBE)
        registerItem("fluid_filter", RagiumItems.FLUID_FILTER)
        registerItem("guide_book", RagiumItems.GUIDE_BOOK)
        registerItem("item_filter", RagiumItems.ITEM_FILTER)
        registerItem("ragi_wrench", RagiumItems.RAGI_WRENCH)
        registerItem("trader_catalog", RagiumItems.TRADER_CATALOG)

        registerItem("sweet_berries_cake_piece", RagiumItems.SWEET_BERRIES_CAKE_PIECE)
        registerItem("melon_pie", RagiumItems.MELON_PIE)

        registerItem("butter", RagiumItems.BUTTER)
        registerItem("caramel", RagiumItems.CARAMEL)
        registerItem("dough", RagiumItems.DOUGH)
        registerItem("flour", RagiumItems.FLOUR)

        registerItem("chocolate", RagiumItems.CHOCOLATE)
        registerItem("chocolate_apple", RagiumItems.CHOCOLATE_APPLE)
        registerItem("chocolate_bread", RagiumItems.CHOCOLATE_BREAD)
        registerItem("chocolate_cookie", RagiumItems.CHOCOLATE_COOKIE)

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
        registerItem("obsidian_tear", RagiumItems.OBSIDIAN_TEAR)

        registerItem("basalt_mesh", RagiumItems.BASALT_MESH)
        registerItem("blazing_carbon_electrode", RagiumItems.BLAZING_CARBON_ELECTRODE)
        registerItem("carbon_electrode", RagiumItems.CARBON_ELECTRODE)
        registerItem("charged_carbon_electrode", RagiumItems.CHARGED_CARBON_ELECTRODE)
        registerItem("engine", RagiumItems.ENGINE)
        registerItem("laser_emitter", RagiumItems.LASER_EMITTER)
        registerItem("led", RagiumItems.LED)
        registerItem("processor_socket", RagiumItems.PROCESSOR_SOCKET)
        registerItem("ragi_crystal_processor", RagiumItems.RAGI_CRYSTAL_PROCESSOR)
        registerItem("solar_panel", RagiumItems.SOLAR_PANEL)

        registerItem("uranium_fuel", RagiumItems.URANIUM_FUEL)
        registerItem("plutonium_fuel", RagiumItems.PLUTONIUM_FUEL)
        registerItem("yellow_cake", RagiumItems.YELLOW_CAKE)
        registerItem("yellow_cake_piece", RagiumItems.YELLOW_CAKE_PIECE)
        registerItem("nuclear_waste", RagiumItems.NUCLEAR_WASTE)

        registerItem("ragi_ticket", RagiumItems.RAGI_TICKET)
    }

    @JvmStatic
    fun initRegistry() {
        // ApiLookup
        registerItemStorages()
        registerFluidStorages()
        registerEnergyStorages()
        registerTierProviders()
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
        // Dispenser
        DispenserBlock.registerProjectileBehavior(RagiumItems.BEDROCK_DYNAMITE)
        DispenserBlock.registerProjectileBehavior(RagiumItems.DYNAMITE)
        DispenserBlock.registerProjectileBehavior(RagiumItems.FLATTENING_DYNAMITE)
        // Fluid Attributes
        RagiumFluids.entries.forEach { fluid: RagiumFluids ->
            FluidVariantAttributes.register(
                fluid.get(),
                object : FluidVariantAttributeHandler {
                    override fun getName(fluidVariant: FluidVariant): Text = Text.translatable(fluid.translationKey)
                },
            )
        }

        registerDrinkHandlers(HTFluidDrinkingHandlerRegistry::register)
    }

    private fun registerItemStorages() {
        registerItemStorage({ world: World, _: BlockPos, state: BlockState, _: BlockEntity?, direction: Direction? ->
            val color: DyeColor = state.getOrNull(RagiumBlockProperties.COLOR) ?: return@registerItemStorage null
            world.backpackManager
                .map { it[color] }
                .map { InventoryStorage.of(it, direction) }
                .result()
                .getOrNull()
        }, RagiumBlocks.BACKPACK_INTERFACE)

        registerItemStorage({ world: World, pos: BlockPos, _: BlockState, _: BlockEntity?, _: Direction? ->
            InsertionOnlyStorage { resource: ItemVariant, maxAmount: Long, _: TransactionContext ->
                if (dropStackAt(world, pos.down(), resource.toStack(maxAmount.toInt()))) maxAmount else 0
            }
        }, RagiumBlocks.OPEN_CRATE)
        // trash box
        registerItemStorage(
            { _: World, _: BlockPos, _: BlockState, _: BlockEntity?, _: Direction? -> HTVoidStorage.ITEM },
            RagiumBlocks.TRASH_BOX,
        )
        // cross pipe
        registerItemStorage({ world: World, pos: BlockPos, _: BlockState, _: BlockEntity?, direction: Direction? ->
            direction?.let { front: Direction ->
                ItemStorage.SIDED.find(world, pos.offset(front.opposite), front)
            }
        }, RagiumContents.CrossPipes.STEEL.get())
        // pipe station
        registerItemStorage({ world: World, pos: BlockPos, state: BlockState, _: BlockEntity?, direction: Direction? ->
            val front: Direction = state.getOrDefault(Properties.FACING, Direction.NORTH)
            val others: List<Direction> =
                Direction.entries.filterNot { directionIn: Direction -> directionIn.axis == front.axis }
            CombinedStorage(
                buildList {
                    addAll(
                        others.mapNotNull { direction: Direction ->
                            ItemStorage.SIDED.find(
                                world,
                                pos.offset(direction),
                                direction.opposite,
                            )
                        },
                    )
                    add(ItemStorage.SIDED.find(world, pos.offset(front), front.opposite))
                },
            )
        }, RagiumContents.PipeStations.ITEM.get())
        // filtering pipe
        registerItemStorage({ world: World, pos: BlockPos, _: BlockState, _: BlockEntity?, direction: Direction? ->
            null
        }, RagiumContents.FilteringPipe.ITEM.get())
    }

    private fun registerFluidStorages() {
        FluidStorage
            .combinedItemApiProvider(RagiumItems.EMPTY_FLUID_CUBE)
            .register(::HTEmptyFluidCubeStorage)
        RagiumContents.Drums.entries
            .map(RagiumContents.Drums::asItem)
            .map(FluidStorage::combinedItemApiProvider)
            .forEach { event: Event<FluidStorage.CombinedItemApiProvider> -> event.register(HTTieredFluidItemStorage::find) }
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
        } // trash box
        registerFluidStorage(
            { _: World, _: BlockPos, _: BlockState, _: BlockEntity?, _: Direction? -> HTVoidStorage.FLUID },
            RagiumBlocks.TRASH_BOX,
        )
        // cross pipe
        registerFluidStorage({ world: World, pos: BlockPos, _: BlockState, _: BlockEntity?, direction: Direction? ->
            direction?.let { front: Direction ->
                FluidStorage.SIDED.find(world, pos.offset(front.opposite), front)
            }
        }, RagiumContents.CrossPipes.GOLD.get())
        // pipe station
        registerFluidStorage({ world: World, pos: BlockPos, state: BlockState, _: BlockEntity?, direction: Direction? ->
            val front: Direction = state.getOrDefault(Properties.FACING, Direction.NORTH)
            val others: List<Direction> =
                Direction.entries.filterNot { directionIn: Direction -> directionIn.axis == front.axis }
            CombinedStorage(
                buildList {
                    addAll(
                        others.mapNotNull { direction: Direction ->
                            FluidStorage.SIDED.find(
                                world,
                                pos.offset(direction),
                                direction.opposite,
                            )
                        },
                    )
                    add(FluidStorage.SIDED.find(world, pos.offset(front), front.opposite))
                },
            )
        }, RagiumContents.PipeStations.FLUID.get())
        // filtering pipe
        registerFluidStorage({ world: World, pos: BlockPos, _: BlockState, _: BlockEntity?, direction: Direction? ->
            null
        }, RagiumContents.FilteringPipe.FLUID.get())
    }

    private fun registerItemStorage(provider: BlockApiLookup.BlockApiProvider<Storage<ItemVariant>, Direction?>, block: Block) {
        ItemStorage.SIDED.registerForBlocks(provider, block)
    }

    private fun registerEnergyStorages() {
        EnergyStorage.SIDED.registerForBlocks(
            { _: World, _: BlockPos, _: BlockState, _: BlockEntity?, _: Direction? -> InfiniteEnergyStorage.INSTANCE },
            RagiumBlocksNew.CREATIVE_SOURCE.get(),
        )
        EnergyStorage.SIDED.registerForBlocks({ world: World, _: BlockPos, _: BlockState, _: BlockEntity?, _: Direction? ->
            world.energyNetwork.result().getOrNull()
        }, RagiumBlocks.NETWORK_INTERFACE)
    }

    private fun registerTierProviders() {
        HTMachineTier.SIDED_LOOKUP.registerFallback { _: World, _: BlockPos, _: BlockState, blockEntity: BlockEntity?, _: Direction? ->
            (blockEntity as? HTMachineBlockEntityBase)?.tier
        }
    }

    private fun registerFluidStorage(provider: BlockApiLookup.BlockApiProvider<Storage<FluidVariant>, Direction?>, block: Block) {
        FluidStorage.SIDED.registerForBlocks(provider, block)
    }

    private fun registerDrinkHandlers(consumer: (Fluid, HTFluidDrinkingHandler) -> Unit) {
        consumer(Fluids.LAVA) { _: ItemStack, world: World, user: LivingEntity ->
            user.setOnFireFromLava()
            dropStackAt(user, Items.OBSIDIAN)
        }
        consumer(RagiumFluids.MILK.get()) { _: ItemStack, world: World, user: LivingEntity ->
            user.clearStatusEffects()
        }
        consumer(RagiumFluids.HONEY.get()) { _: ItemStack, world: World, user: LivingEntity ->
            user.removeStatusEffect(StatusEffects.POISON)
        }
        consumer(RagiumFluids.CHOCOLATE.get()) { _: ItemStack, world: World, user: LivingEntity ->
            user.addStatusEffect(StatusEffectInstance(StatusEffects.STRENGTH, 20 * 5, 1))
        }
    }
}
