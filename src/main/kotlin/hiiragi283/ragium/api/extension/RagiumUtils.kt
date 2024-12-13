package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.recipe.HTItemIngredient
import net.fabricmc.api.EnvType
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.metadata.ModMetadata
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemStack
import net.minecraft.recipe.input.RecipeInput
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.MutableText
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import net.minecraft.world.TeleportTarget
import net.minecraft.world.World
import team.reborn.energy.api.EnergyStorage
import java.util.*
import kotlin.jvm.optionals.getOrNull

//    ApiLookup    //

/**
 * Try to find and API instance from [player] holding [ItemStack] in [hand]
 */
fun <A : Any> ItemApiLookup<A, ContainerItemContext>.findFromHand(player: PlayerEntity, hand: Hand = Hand.MAIN_HAND): A? =
    ContainerItemContext.forPlayerInteraction(player, hand).find(this)

/**
 * Try to find and API instance from [stack]
 */
fun <A : Any> ItemApiLookup<A, ContainerItemContext>.findFromStack(stack: ItemStack): A? =
    ContainerItemContext.withConstant(stack).find(this)

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

/**
 * Throw [ProjectileEntity] by [player] in [world]
 * @return true if [ProjectileEntity] has successfully spawned on server side, or false if not
 */
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
        entity.setVelocity(player, player.pitch, player.yaw, 0.0f, 1.5f, 1.0f)
        return world.spawnEntity(entity)
    }
    return false
}

/**
 * Get [ItemStack] which [this] living entity holding in [LivingEntity.getActiveHand]
 */
fun LivingEntity.getStackInActiveHand(): ItemStack = getStackInHand(activeHand)

//    Color    //

fun toFloatColor(color: Int): Triple<Float, Float, Float> {
    val red: Float = (color shr 16 and 255) / 255.0f
    val green: Float = (color shr 8 and 255) / 255.0f
    val blue: Float = (color and 255) / 255.0f
    return Triple(red, green, blue)
}

//    EnergyStorage    //

/**
 * Calculate [Float] percentage from [EnergyStorage.getAmount] and [EnergyStorage.getCapacity] of [this] energy storage
 */
val EnergyStorage.energyPercent: Float
    get() = (amount.toFloat() / capacity.toFloat()) * 100

//    FabricLoader    //

fun isModLoaded(modId: String): Boolean = FabricLoader.getInstance().isModLoaded(modId)

fun getEnvType(): EnvType = FabricLoader.getInstance().environmentType

fun isClientEnv(): Boolean = FabricLoader.getInstance().environmentType == EnvType.CLIENT

fun isServerEnv(): Boolean = FabricLoader.getInstance().environmentType == EnvType.SERVER

/**
 * Check the current launch is data generation or not
 */
fun isDataGen(): Boolean = System.getProperty("fabric-api.datagen") != null

/**
 * Get [ModMetadata] from [modId], or null if there is no mod with [modId]
 */
fun getModMetadata(modId: String): ModMetadata? = FabricLoader
    .getInstance()
    .getModContainer(modId)
    .getOrNull()
    ?.metadata

/**
 * Get mod name from [modId], or null if there is no mod with [modId]
 */
fun getModName(modId: String): String? = getModMetadata(modId)?.name

inline fun <reified T : Any> collectEntrypoints(key: String): List<T> = FabricLoader.getInstance().getEntrypoints(key, T::class.java)

//    Fluid    //

/**
 * Get fluid name from [FluidVariantAttributes.getName]
 */
val Fluid.name: MutableText
    get() = FluidVariant.of(this).name

/**
 * Get fluid name from [FluidVariantAttributes.getName]
 */
val FluidVariant.name: MutableText
    get() = FluidVariantAttributes.getName(this).copy()

/**
 * Check [this] fluid is the same as [Fluids.EMPTY]
 */
val Fluid.isEmpty: Boolean
    get() = this == Fluids.EMPTY

//    Identifier    //

fun Identifier.splitWith(splitter: Char): String = "${namespace}${splitter}$path"

//    ServiceLoader    //

inline fun <reified T : Any> collectInstances(): Iterable<T> = ServiceLoader.load(T::class.java)

//    Recipe    //

/**
 * Create a new list from all stacks in [this] recipe input by [RecipeInput.getStackInSlot]
 */
fun RecipeInput.toList(): List<ItemStack> = buildList<ItemStack> {
    for (index: Int in (0 until this.size)) {
        add(this[index])
    }
}

fun getMatchingIndices(ingredients: Collection<HTItemIngredient>, stacks: Collection<ItemStack>): Boolean {
    if (ingredients.size > stacks.size) return false
    val ingredients1: MutableList<HTItemIngredient> = ingredients.toMutableList()
    val stacks1: MutableList<ItemStack> = stacks.toMutableList()
    val ingIterator: MutableIterator<HTItemIngredient> = ingredients1.iterator()
    val stackIterator: MutableIterator<ItemStack> = stacks1.iterator()
    while (ingIterator.hasNext()) {
        ingredient@{
            val ingredient: HTItemIngredient = ingIterator.next()
            while (stackIterator.hasNext()) {
                stack@{
                    val stack: ItemStack = stackIterator.next()
                    if (ingredient.test(stack)) {
                        ingIterator.remove()
                        stackIterator.remove()
                    }
                }
            }
        }
    }
    return ingredients1.isEmpty() && stacks1.isEmpty()
}
