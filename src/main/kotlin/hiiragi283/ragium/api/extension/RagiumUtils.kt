package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.common.block.entity.HTBlockEntityBase
import net.fabricmc.api.EnvType
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.metadata.ModMetadata
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeEntry
import net.minecraft.recipe.RecipeManager
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.input.RecipeInput
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.MutableText
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.TeleportTarget
import net.minecraft.world.World
import team.reborn.energy.api.EnergyStorage
import java.util.*
import kotlin.jvm.optionals.getOrNull

//    ApiLookup    //

fun <A : Any> ItemApiLookup<A, ContainerItemContext>.findFromHand(player: PlayerEntity, hand: Hand = Hand.MAIN_HAND): A? =
    ContainerItemContext.forPlayerInteraction(player, hand).find(this)

fun <A : Any> ItemApiLookup<A, ContainerItemContext>.findFromStack(stack: ItemStack): A? =
    ContainerItemContext.withConstant(stack).find(this)

//    BlockEntity    //

fun <T : Any> BlockEntity.ifPresentWorld(action: (World) -> T): T? = world?.let(action)

fun BlockEntity.createContext(): ScreenHandlerContext = world?.let { ScreenHandlerContext.create(it, pos) } ?: ScreenHandlerContext.EMPTY

//    BlockPos    //

val BlockPos.aroundPos: List<BlockPos>
    get() = Direction.entries.map(this::offset)

fun BlockPos.getAroundPos(filter: (BlockPos) -> Boolean): List<BlockPos> = Direction.entries.map(this::offset).filter(filter)

//    ChunkPos    //

fun ChunkPos.iterator(yRange: IntRange): Iterator<BlockPos> = buildList {
    (startX..endX).forEach { x: Int ->
        (startZ..endZ).forEach { z: Int ->
            yRange.forEach { y: Int ->
                add(BlockPos(x, y, z))
            }
        }
    }
}.iterator()

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

fun throwEntity(world: World, player: PlayerEntity, entityBuilder: (World, PlayerEntity) -> ProjectileEntity?): Boolean {
    world.playSound(
        null,
        player.x,
        player.y,
        player.z,
        SoundEvents.ENTITY_SNOWBALL_THROW,
        SoundCategory.PLAYERS,
        0.5f,
        0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f),
    )
    if (!world.isClient) {
        val entity: ProjectileEntity = entityBuilder(world, player) ?: return false
        entity.apply {
            setVelocity(player, player.pitch, player.yaw, 0.0f, 1.5f, 1.0f)
            world.spawnEntity(this)
        }
        return true
    }
    return false
}

fun LivingEntity.getStackInActiveHand(): ItemStack = getStackInHand(activeHand)

//    Color    //

fun toFloatColor(color: Int): Triple<Float, Float, Float> {
    val red: Float = (color shr 16 and 255) / 255.0f
    val green: Float = (color shr 8 and 255) / 255.0f
    val blue: Float = (color and 255) / 255.0f
    return Triple(red, green, blue)
}

//    EnergyStorage    //

val EnergyStorage.energyPercent: Float
    get() = (amount.toFloat() / capacity.toFloat()) * 100

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

val Fluid.isEmpty: Boolean
    get() = this == Fluids.EMPTY

val Fluid.nonEmptyOrNull: Fluid?
    get() = takeUnless { it.isEmpty }

//    Identifier    //

fun Identifier.splitWith(splitter: Char): String = "${namespace}${splitter}$path"

//    PlayerEntity    //

fun Entity.asServerPlayer(): ServerPlayerEntity? = this as? ServerPlayerEntity

@Suppress("UNCHECKED_CAST")
fun <T : ScreenHandler> ServerPlayerEntity.getAsScreenHandler(): T? = currentScreenHandler as? T

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

fun RecipeInput.iterable(): Iterable<ItemStack> = buildList<ItemStack> {
    for (index: Int in (0 until this.size)) {
        add(this[index])
    }
}

//    ScreenHandler    //

fun <T : Any> ScreenHandlerContext.getOrNull(getter: (World, BlockPos) -> T?): T? = get(getter, null)

fun ScreenHandlerContext.getBlockEntity(): BlockEntity? = getOrNull(World::getBlockEntity)

fun ScreenHandlerContext.getInventory(size: Int): Inventory =
    (getBlockEntity() as? HTBlockEntityBase)?.asInventory() ?: SimpleInventory(size)

fun ScreenHandlerContext.getMachineEntity(): HTMachineBlockEntityBase? = getBlockEntity() as? HTMachineBlockEntityBase
