package hiiragi283.ragium.common.internal

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTFluidOutputRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTSingleItemRecipeBuilder
import hiiragi283.ragium.api.event.HTMachineProcessEvent
import hiiragi283.ragium.api.event.HTMachineRecipesUpdatedEvent
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineAccess
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.multiblock.*
import hiiragi283.ragium.api.recipe.HTRecipeTypes
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.common.block.addon.HTSlagCollectorBlockEntity
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMultiblockMaps
import hiiragi283.ragium.common.init.RagiumVirtualFluids
import hiiragi283.ragium.common.item.component.HTSpawnerContent
import hiiragi283.ragium.common.network.HTPotionBundlePacket
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.event.RegisterCommandsEvent
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import org.slf4j.Logger

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
internal object RagiumGameEvents {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    fun onRegisterCommands(event: RegisterCommandsEvent) {
        LOGGER.info("Registered Commands!")
    }

    @SubscribeEvent
    fun onEntityDrops(event: LivingDropsEvent) {
        if (event.isCanceled) return
        val source: DamageSource = event.source
        val attacker: Player = source.entity as? Player ?: return
        val level: Level = attacker.level()
        if (!level.isClientSide) {
            val stackInHands: Iterable<ItemStack> = attacker.handSlots
            for (stack: ItemStack in stackInHands) {
                if (stack.isEmpty) continue
                if (stack.getLevel(level.registryAccess(), Enchantments.SILK_TOUCH) > 0) {
                    val attacked: LivingEntity = event.entity
                }
            }
        }
    }

    @SubscribeEvent
    fun useItemOnBlock(event: UseItemOnBlockEvent) {
        if (event.isCanceled) return
        val stack: ItemStack = event.itemStack
        if (stack.isEmpty) return
        if (stack.`is`(Items.BOOK)) {
            togglePreview(event)
        } else if (stack.`is`(Tags.Items.NETHER_STARS)) {
            captureSpawner(event)
        }
    }

    @JvmStatic
    private fun togglePreview(event: UseItemOnBlockEvent) {
        val level: Level = event.level
        val pos: BlockPos = event.pos
        (level.getHTBlockEntity(pos) as? HTMultiblockController)?.let { controller: HTMultiblockController ->
            controller.showPreview = !controller.showPreview
            event.cancelWithResult(ItemInteractionResult.sidedSuccess(level.isClientSide))
        }
    }

    @JvmStatic
    private fun captureSpawner(event: UseItemOnBlockEvent) {
        val level: Level = event.level
        val pos: BlockPos = event.pos
        val player: Player? = event.player
        val definition = HTControllerDefinition(level, pos, Direction.NORTH)
        val controller: HTMultiblockController = object : HTMultiblockController {
            override var showPreview: Boolean = false

            override fun getMultiblockMap(): HTMultiblockMap.Relative = RagiumMultiblockMaps.SPAWNER

            override fun getDefinition(): HTControllerDefinition = definition

            override fun processData(definition: HTControllerDefinition, data: HTMultiblockData) {
                // Get spawner data
                val content: HTSpawnerContent =
                    data.components.get(RagiumComponentTypes.SPAWNER_CONTENT.get()) ?: return
                // Break structure
                getMultiblockMap().convertAbsolute(definition).forEach { posIn: BlockPos, _: HTMultiblockComponent ->
                    level.destroyBlock(posIn, false)
                }
                // Drop Spawner
                dropStackAt(level, pos.above(), createSpawnerStack(content.entityType))
                event.itemStack.consume(1, player)
            }
        }
        val data: HTMultiblockData = controller.collectData { player?.displayClientMessage(it, true) }
        controller.processData(definition, data)
        event.cancelWithResult(ItemInteractionResult.sidedSuccess(level.isClientSide))
    }

    @SubscribeEvent
    fun onLeftClickBlock(event: PlayerInteractEvent.LeftClickEmpty) {
        val stack: ItemStack = event.itemStack
        if (!stack.isEmpty && stack.`is`(RagiumItems.POTION_BUNDLE)) {
            PacketDistributor.sendToServer(HTPotionBundlePacket)
        }
    }

    @SubscribeEvent
    fun onBlastFurnaceSucceeded(event: HTMachineProcessEvent.Success) {
        val machine: HTMachineAccess = event.machine
        val level: Level = machine.levelAccess ?: return
        val pos: BlockPos = machine.pos
        var addon: HTSlagCollectorBlockEntity? = null
        if (machine.machineType == HTMachineType.BLAST_FURNACE) {
            for (direction: Direction in Direction.entries) {
                addon = (level.getBlockEntity(pos.relative(direction)) as? HTSlagCollectorBlockEntity)
                if (addon != null) break
            }
        }
        if (addon == null) return
        addon.onReceiveEvent(event)
    }

    @SubscribeEvent
    fun onMachineRecipesUpdated(event: HTMachineRecipesUpdatedEvent) {
        compressor(event)
        extractor(event)
        grinder(event)
        infuser(event)
    }

    @JvmStatic
    private fun compressor(event: HTMachineRecipesUpdatedEvent) {
        for ((type: HTMaterialType, key: HTMaterialKey) in RagiumAPI
            .getInstance()
            .getMaterialRegistry()
            .typedMaterials) {
            val name: String = key.name
            val mainPrefix: HTTagPrefix? = type.getMainPrefix()
            if (mainPrefix != null) {
                // Gear
                event.register(
                    HTRecipeTypes.COMPRESSOR,
                    RagiumAPI.id("runtime_${name}_gear"),
                ) { lookup: HolderGetter<Item> ->
                    HTSingleItemRecipeBuilder
                        .compressor(lookup)
                        .itemInput(mainPrefix, key, 4)
                        .catalyst(RagiumItemTags.GEAR_MOLDS)
                        .itemOutput(HTTagPrefix.GEAR, key)
                }
                // Plate
                event.register(
                    HTRecipeTypes.COMPRESSOR,
                    RagiumAPI.id("runtime_${name}_plate"),
                ) { lookup: HolderGetter<Item> ->
                    HTSingleItemRecipeBuilder
                        .compressor(lookup)
                        .itemInput(mainPrefix, key)
                        .catalyst(RagiumItemTags.PLATE_MOLDS)
                        .itemOutput(HTTagPrefix.PLATE, key)
                }
                // Rod
                event.register(
                    HTRecipeTypes.COMPRESSOR,
                    RagiumAPI.id("runtime_${name}_rod"),
                ) { lookup: HolderGetter<Item> ->
                    HTSingleItemRecipeBuilder
                        .compressor(lookup)
                        .itemInput(mainPrefix, key)
                        .catalyst(RagiumItemTags.ROD_MOLDS)
                        .itemOutput(HTTagPrefix.ROD, key, 2)
                }
                // Wire
                event.register(
                    HTRecipeTypes.COMPRESSOR,
                    RagiumAPI.id("runtime_${name}_wire"),
                ) { lookup: HolderGetter<Item> ->
                    HTSingleItemRecipeBuilder
                        .compressor(lookup)
                        .itemInput(mainPrefix, key)
                        .catalyst(RagiumItemTags.WIRE_MOLDS)
                        .itemOutput(HTTagPrefix.WIRE, key, 2)
                }
            }
            // Gem
            event.register(
                HTRecipeTypes.COMPRESSOR,
                RagiumAPI.id("runtime_${name}_gem"),
            ) { lookup: HolderGetter<Item> ->
                HTSingleItemRecipeBuilder
                    .compressor(lookup)
                    .itemInput(HTTagPrefix.DUST, key)
                    .itemOutput(HTTagPrefix.GEM, key)
            }
        }
    }

    @JvmStatic
    private fun grinder(event: HTMachineRecipesUpdatedEvent) {
        for ((type: HTMaterialType, key: HTMaterialKey) in RagiumAPI
            .getInstance()
            .getMaterialRegistry()
            .typedMaterials) {
            val name: String = key.name
            val mainPrefix: HTTagPrefix? = type.getMainPrefix()
            val resultPrefix: HTTagPrefix? = type.getOreResultPrefix()
            // Ore
            if (resultPrefix != null) {
                event.register(
                    HTRecipeTypes.GRINDER,
                    RagiumAPI.id("runtime_${name}_dust_from_ore"),
                ) { lookup: HolderGetter<Item> ->
                    val count: Int = RagiumAPI.getInstance().getGrinderOutputCount(key)
                    HTSingleItemRecipeBuilder
                        .grinder(lookup)
                        .itemInput(HTTagPrefix.ORE, key)
                        .itemOutput(resultPrefix, key, count * 2)
                }
            }
            // Gem/Ingot
            if (mainPrefix != null) {
                event.register(
                    HTRecipeTypes.GRINDER,
                    RagiumAPI.id("runtime_${name}_dust_from_main"),
                ) { lookup: HolderGetter<Item> ->
                    HTSingleItemRecipeBuilder
                        .grinder(lookup)
                        .itemInput(mainPrefix, key)
                        .itemOutput(HTTagPrefix.DUST, key)
                }
            }
            // Gear
            event.register(
                HTRecipeTypes.GRINDER,
                RagiumAPI.id("runtime_${name}_dust_from_gear"),
            ) { lookup: HolderGetter<Item> ->
                HTSingleItemRecipeBuilder
                    .grinder(lookup)
                    .itemInput(HTTagPrefix.GEAR, key)
                    .itemOutput(HTTagPrefix.DUST, key, 4)
            }
            // Plate
            event.register(
                HTRecipeTypes.GRINDER,
                RagiumAPI.id("runtime_${name}_dust_from_plate"),
            ) { lookup: HolderGetter<Item> ->
                HTSingleItemRecipeBuilder
                    .grinder(lookup)
                    .itemInput(HTTagPrefix.PLATE, key)
                    .itemOutput(HTTagPrefix.DUST, key)
            }
            // Raw
            event.register(
                HTRecipeTypes.GRINDER,
                RagiumAPI.id("runtime_${name}_dust_from_raw"),
            ) { lookup: HolderGetter<Item> ->
                HTSingleItemRecipeBuilder
                    .grinder(lookup)
                    .itemInput(HTTagPrefix.RAW_MATERIAL, key, 4)
                    .itemOutput(HTTagPrefix.DUST, key, 3)
            }
        }
    }

    @JvmStatic
    private fun extractor(event: HTMachineRecipesUpdatedEvent) {
        // Fluid Bucket -> Bucket + Fluid
        event
            .lookupOrThrow(Registries.FLUID)
            .listElements()
            .forEach { holder: Holder.Reference<Fluid> ->
                val fluid: Fluid = holder.value()
                if (!fluid.isSource) return@forEach
                val bucket: Item = fluid.bucket
                if (bucket == Items.AIR) return@forEach
                event.register(
                    HTRecipeTypes.EXTRACTOR,
                    RagiumAPI.id("runtime_${holder.idOrThrow.path}"),
                ) { lookup: HolderGetter<Item> ->
                    HTFluidOutputRecipeBuilder
                        .extractor(lookup)
                        .itemInput(bucket)
                        .itemOutput(Items.BUCKET)
                        .fluidOutput(fluid)
                }
            }
    }

    @JvmStatic
    private fun infuser(event: HTMachineRecipesUpdatedEvent) {
        for ((type: HTMaterialType, key: HTMaterialKey) in RagiumAPI
            .getInstance()
            .getMaterialRegistry()
            .typedMaterials) {
            val name: String = key.name
            val resultPrefix: HTTagPrefix = type.getOreResultPrefix() ?: continue
            val count: Int = RagiumAPI.getInstance().getGrinderOutputCount(key)
            // 3x
            event.register(
                HTRecipeTypes.INFUSER,
                RagiumAPI.id("runtime_3x_$name"),
            ) { lookup: HolderGetter<Item> ->
                HTFluidOutputRecipeBuilder
                    .infuser(lookup)
                    .itemInput(HTTagPrefix.ORE, key)
                    .fluidInput(RagiumVirtualFluids.SULFURIC_ACID.commonTag, 500)
                    .itemOutput(resultPrefix, key, count * 3)
            }
            // 4x
            event.register(
                HTRecipeTypes.INFUSER,
                RagiumAPI.id("runtime_4x_$name"),
            ) { lookup: HolderGetter<Item> ->
                HTFluidOutputRecipeBuilder
                    .infuser(lookup)
                    .itemInput(HTTagPrefix.ORE, key)
                    .fluidInput(RagiumVirtualFluids.HYDROFLUORIC_ACID.commonTag, 500)
                    .itemOutput(resultPrefix, key, count * 4)
            }
        }
        // Bucket + Fluid -> Fluid Bucket
        event
            .lookupOrThrow(Registries.FLUID)
            .listElements()
            .forEach { holder: Holder.Reference<Fluid> ->
                val fluid: Fluid = holder.value()
                if (!fluid.isSource) return@forEach
                val bucket: Item = fluid.bucket
                if (bucket == Items.AIR) return@forEach
                event.register(
                    HTRecipeTypes.INFUSER,
                    RagiumAPI.id("runtime_${holder.idOrThrow.path}_bucket"),
                ) { lookup: HolderGetter<Item> ->
                    HTFluidOutputRecipeBuilder
                        .infuser(lookup)
                        .itemInput(Items.BUCKET)
                        .fluidInput(fluid)
                        .itemOutput(bucket)
                }
            }
        // Oxidizables
        event
            .lookupOrThrow(Registries.BLOCK)
            .listElements()
            .forEach { holder: Holder.Reference<Block> ->
                val block: Block = holder.value()
                val input = ItemStack(block)
                if (input.isEmpty) return@forEach
                val block1: Block = holder.getData(NeoForgeDataMaps.OXIDIZABLES)?.nextOxidationStage ?: return@forEach
                event.register(
                    HTRecipeTypes.INFUSER,
                    RagiumAPI.id("runtime_${holder.idOrThrow.path}"),
                ) { lookup: HolderGetter<Item> ->
                    HTFluidOutputRecipeBuilder
                        .infuser(lookup)
                        .itemInput(block)
                        .fluidInput(RagiumVirtualFluids.OXYGEN.commonTag, 100)
                        .itemOutput(block1)
                }
            }
    }
}
