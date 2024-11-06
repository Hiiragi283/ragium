package hiiragi283.ragium.api.extension

import com.google.common.collect.HashBasedTable
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.util.HTTable
import hiiragi283.ragium.api.util.HTWrappedTable
import net.fabricmc.api.EnvType
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.metadata.ModMetadata
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeEntry
import net.minecraft.recipe.RecipeManager
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.input.RecipeInput
import net.minecraft.registry.BuiltinRegistries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.SimpleNamedScreenHandlerFactory
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableTextContent
import net.minecraft.util.Identifier
import net.minecraft.util.Language
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import java.text.NumberFormat
import java.util.*
import kotlin.jvm.optionals.getOrNull

//    BlockPos    //

fun BlockPos.getAroundPos(filter: (BlockPos) -> Boolean): List<BlockPos> = Direction.entries.map(this::offset).filter(filter)

//    ChunkPos    //

fun ChunkPos.forEach(yRange: IntRange, action: (BlockPos) -> Unit) {
    (startX..endX).forEach { x: Int ->
        (startZ..endZ).forEach { z: Int ->
            yRange.forEach { y: Int ->
                action(BlockPos(x, y, z))
            }
        }
    }
}

//    Color    //

fun toFloatColor(color: Int): Triple<Float, Float, Float> {
    val red: Float = (color shr 16 and 255) / 255.0f
    val green: Float = (color shr 8 and 255) / 255.0f
    val blue: Float = (color and 255) / 255.0f
    return Triple(red, green, blue)
}

//    FabricLoader    //

fun isModLoaded(modId: String): Boolean = FabricLoader.getInstance().isModLoaded(modId)

fun getEnvType(): EnvType = FabricLoader.getInstance().environmentType

fun isClientEnv(): Boolean = FabricLoader.getInstance().environmentType == EnvType.CLIENT

fun isServerEnv(): Boolean = FabricLoader.getInstance().environmentType == EnvType.SERVER

fun isDataGen(): Boolean = System.getProperty("fabric-api.datagen") != null

fun getModMetadata(modId: String): ModMetadata? = FabricLoader
    .getInstance()
    .getModContainer(modId)
    .getOrNull()
    ?.metadata

fun getModName(modId: String): String? = getModMetadata(modId)?.name

//    Identifier    //

fun Identifier.splitWith(splitter: Char): String = "${namespace}${splitter}$path"

//    Inventory    //

fun openEnderChest(world: World, player: PlayerEntity) {
    if (!world.isClient) {
        player.openHandledScreen(
            SimpleNamedScreenHandlerFactory({ syncId: Int, playerInv: PlayerInventory, _: PlayerEntity ->
                GenericContainerScreenHandler.createGeneric9x3(
                    syncId,
                    playerInv,
                    playerInv.player.enderChestInventory,
                )
            }, Text.translatable("container.enderchest")),
        )
    }
    world.playSound(
        null,
        player.x,
        player.y,
        player.z,
        SoundEvents.BLOCK_ENDER_CHEST_OPEN,
        SoundCategory.BLOCKS,
        0.5f,
        1.0f,
    )
}

fun Inventory.modifyStack(slot: Int, mapping: (ItemStack) -> ItemStack) {
    val stackIn: ItemStack = getStack(slot)
    setStack(slot, mapping(stackIn))
}

fun Inventory.toStorage(side: Direction?): InventoryStorage = InventoryStorage.of(this, side)

fun Inventory.getStackOrNull(slot: Int): ItemStack? = if (slot in 0..size()) getStack(slot) else null

//    ServiceLoader    //

inline fun <reified T : Any> collectInstances(): Iterable<T> = ServiceLoader.load(T::class.java)

//    Recipe    //

fun <T : RecipeInput, U : Recipe<T>> RecipeManager.getFirstMatch(
    type: RecipeType<U>,
    predicate: (RecipeEntry<U>) -> Boolean,
): RecipeEntry<U>? = listAllOfType(type).firstOrNull(predicate)

fun <T : RecipeInput, U : Recipe<T>> RecipeManager.getAllMatches(
    type: RecipeType<U>,
    predicate: (RecipeEntry<U>) -> Boolean,
): List<RecipeEntry<U>> = listAllOfType(type).filter(predicate)

operator fun <T : Recipe<*>> RecipeEntry<T>.component1(): Identifier = this.id

operator fun <T : Recipe<*>> RecipeEntry<T>.component2(): T = this.value

//    Registry    //

fun createWrapperLookup(): RegistryWrapper.WrapperLookup = BuiltinRegistries.createWrapperLookup()

fun <T : Any> idComparator(registry: Registry<T>): Comparator<T> = compareBy(registry::getId)

fun <T : Any> entryComparator(registry: Registry<T>): Comparator<RegistryEntry<T>> = compareBy { it.key.orElseThrow().value }

fun <T : Any> RegistryEntry<T>.isOf(value: T): Boolean = value() == value

fun <T : Any> RegistryEntryList<T>.isIn(value: T): Boolean = any { it.isOf(value) }

//    ScreenHandler    //

fun <T : Any> ScreenHandlerContext.getOrNull(getter: (World, BlockPos) -> T?): T? = get(getter, null)

fun ScreenHandlerContext.getBlockEntity(): BlockEntity? = getOrNull(World::getBlockEntity)

fun ScreenHandlerContext.getInventory(size: Int): Inventory = getBlockEntity() as? Inventory ?: SimpleInventory(size)

fun ScreenHandlerContext.getMachine(): HTMachineBlockEntityBase? = getBlockEntity() as? HTMachineBlockEntityBase

//    Table    //

fun <R : Any, C : Any, V : Any> tableOf(): HTTable<R, C, V> = HTWrappedTable(HashBasedTable.create())

fun <R : Any, C : Any, V : Any> mutableTableOf(): HTWrappedTable.Mutable<R, C, V> = HTWrappedTable.Mutable(HashBasedTable.create())

fun <R : Any, C : Any, V : Any> buildTable(builderAction: HTTable.Mutable<R, C, V>.() -> Unit): HTTable<R, C, V> =
    HTWrappedTable.Mutable(HashBasedTable.create<R, C, V>()).apply(builderAction)

fun <R : Any, C : Any, V : Any> HTTable<R, C, V>.forEach(action: (Triple<R, C, V>) -> Unit) {
    entries.forEach(action)
}

//    Text    //

fun intText(value: Int): MutableText = longText(value.toLong())

fun longText(value: Long): MutableText = Text.literal(NumberFormat.getNumberInstance().format(value))

fun Text.hasValidTranslation(): Boolean = (this.content as? TranslatableTextContent)
    ?.let(TranslatableTextContent::getKey)
    ?.let(Language.getInstance()::hasTranslation)
    ?: false

/*fun breakRangedBlock(
    world: World,
    pos: BlockPos,
    range: Int,
    breaker: Entity,
    tool: ItemStack
) {
    breakRangedBlock(world, pos, breaker.facing, range, breaker, tool::isSuitableFor)
}

fun breakRangedBlock(
    world: World,
    pos: BlockPos,
    direction: Direction,
    range: Int,
    breaker: Entity?,
    predicate: (BlockState) -> Boolean
) {
    when (direction.axis) {
        Direction.Axis.X -> BlockPos.iterate(pos.add(0, range, -range), pos.add(0, -range, range))
        Direction.Axis.Y -> BlockPos.iterate(pos.add(-range, 0, -range), pos.add(range, 0, range))
        Direction.Axis.Z -> BlockPos.iterate(pos.add(-range, range, 0), pos.add(range, -range, 0))
        else -> listOf()
    }
        .filter { predicate(world.getBlockState(it)) }
        .forEach {
            world.setBlockState(it, Blocks.BEDROCK.defaultState)
            // world.breakBlock(it, true, breaker)
        }
}*/
