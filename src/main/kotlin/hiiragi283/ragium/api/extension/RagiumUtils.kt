package hiiragi283.ragium.api.extension

import com.google.common.collect.HashBasedTable
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.util.HTTable
import hiiragi283.ragium.api.util.HTWrappedTable
import hiiragi283.ragium.common.block.entity.HTBlockEntityBase
import net.fabricmc.api.EnvType
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.metadata.ModMetadata
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluid
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
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.text.Texts
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.TeleportTarget
import net.minecraft.world.World
import java.util.*
import kotlin.jvm.optionals.getOrNull

//    BlockPos    //

fun BlockPos.getAroundPos(filter: (BlockPos) -> Boolean): List<BlockPos> = Direction.entries.map(this::offset).filter(filter)

//    ChunkPos    //

fun ChunkPos.iterator(yRange: IntRange): Iterator<BlockPos> = iterator {
    (startX..endX).forEach { x: Int ->
        (startZ..endZ).forEach { z: Int ->
            yRange.forEach { y: Int ->
                yield(BlockPos(x, y, z))
            }
        }
    }
}

fun ChunkPos.forEach(yRange: IntRange, action: (BlockPos) -> Unit) {
    (startX..endX).forEach { x: Int ->
        (startZ..endZ).forEach { z: Int ->
            yRange.forEach { y: Int ->
                action(BlockPos(x, y, z))
            }
        }
    }
}

//    Entity    //

fun teleport(
    entity: Entity,
    world: World,
    posTo: Vec3d,
    playSound: Boolean = true,
): Boolean {
    if (world is ServerWorld && canTeleport(entity, world)) {
        if (entity.hasVehicle()) {
            entity.detach()
        }
        if (entity is ServerPlayerEntity) {
            if (entity.networkHandler.isConnectionOpen) {
                entity.teleportTo(
                    TeleportTarget(
                        world,
                        posTo,
                        entity.velocity,
                        entity.yaw,
                        entity.pitch,
                        TeleportTarget.NO_OP,
                    ),
                )
                entity.onLanding()
                entity.clearCurrentExplosion()
            }
        } else {
            entity.teleportTo(
                TeleportTarget(
                    world,
                    entity.pos.add(0.0, 1.0, 0.0),
                    entity.velocity,
                    entity.yaw,
                    entity.pitch,
                    TeleportTarget.NO_OP,
                ),
            )
            entity.onLanding()
            if (entity is PlayerEntity) {
                entity.clearCurrentExplosion()
            }
        }
        if (playSound) {
            world.playSound(null, posTo.x, posTo.y, posTo.z, SoundEvents.ENTITY_PLAYER_TELEPORT, SoundCategory.PLAYERS)
        }
        return true
    }
    return false
}

fun canTeleport(entity: Entity, world: World): Boolean = if (entity.world.registryKey == world.registryKey) {
    if (entity !is LivingEntity) {
        entity.isAlive
    } else {
        entity.isAlive && !entity.isSleeping
    }
} else {
    entity.canUsePortals(true)
}

fun LivingEntity.getStackInActiveHand(): ItemStack = getStackInHand(activeHand)

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

inline fun <reified T : Any> collectEntrypoints(key: String): List<T> = FabricLoader.getInstance().getEntrypoints(key, T::class.java)

//    Fluid    //

val Fluid.name: MutableText
    get() = FluidVariant.of(this).name

//    Identifier    //

fun Identifier.splitWith(splitter: Char): String = "${namespace}${splitter}$path"

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

val <T : Any> RegistryEntryList<T>.isEmpty: Boolean
    get() = size() == 0

fun createWrapperLookup(): RegistryWrapper.WrapperLookup = BuiltinRegistries.createWrapperLookup()

fun <T : Any> idComparator(registry: Registry<T>): Comparator<T> = compareBy(registry::getId)

fun <T : Any> entryComparator(registry: Registry<T>): Comparator<RegistryEntry<T>> = compareBy { it.key.orElseThrow().value }

fun <T : Any> RegistryEntry<T>.isOf(value: T): Boolean = value() == value

operator fun <T : Any> RegistryEntryList<T>.contains(value: T): Boolean = any { it.isOf(value) }

fun <T : Any> RegistryEntryList<T>.asText(mapper: (T) -> Text): MutableText = storage
    .map(
        { it.name },
        { Texts.join(this.map(RegistryEntry<T>::value), mapper) },
    ).copy()

//    ScreenHandler    //

fun <T : Any> ScreenHandlerContext.getOrNull(getter: (World, BlockPos) -> T?): T? = get(getter, null)

fun ScreenHandlerContext.getBlockEntity(): BlockEntity? = getOrNull(World::getBlockEntity)

fun ScreenHandlerContext.getInventory(size: Int): Inventory =
    (getBlockEntity() as? HTBlockEntityBase)?.asInventory() ?: SimpleInventory(size)

fun ScreenHandlerContext.getMachineEntity(): HTMachineBlockEntityBase? = getBlockEntity() as? HTMachineBlockEntityBase

//    Table    //

fun <R : Any, C : Any, V : Any> tableOf(): HTTable<R, C, V> = HTWrappedTable(HashBasedTable.create())

fun <R : Any, C : Any, V : Any> mutableTableOf(): HTWrappedTable.Mutable<R, C, V> = HTWrappedTable.Mutable(HashBasedTable.create())

fun <R : Any, C : Any, V : Any> buildTable(builderAction: HTTable.Mutable<R, C, V>.() -> Unit): HTTable<R, C, V> =
    HTWrappedTable.Mutable(HashBasedTable.create<R, C, V>()).apply(builderAction)

fun <R : Any, C : Any, V : Any> HTTable<R, C, V>.forEach(action: (Triple<R, C, V>) -> Unit) {
    entries.forEach(action)
}
