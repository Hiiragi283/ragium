package hiiragi283.ragium.api.extension

import net.fabricmc.api.EnvType
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.metadata.ModMetadata
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemStack
import net.minecraft.registry.entry.RegistryEntry
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
 * [player]が所持している[ItemStack]からAPIのインスタンスを取得します。
 * @param A APIのクラス
 * @param hand 取得したい[ItemStack]を持っている手
 * @return [ContainerItemContext.forPlayerInteraction]からAPIインスタンスを返す
 */
fun <A : Any> ItemApiLookup<A, ContainerItemContext>.findFromHand(player: PlayerEntity, hand: Hand = Hand.MAIN_HAND): A? =
    ContainerItemContext.forPlayerInteraction(player, hand).find(this)

/**
 * [stack]からAPIインスタンスを取得します。
 * @param A APIのクラス
 * @return [ContainerItemContext.withConstant]からAPIインスタンスを返す
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
 * プレイヤーからエンティティを投げます。
 * @param entityBuilder [World]と[PlayerEntity]から[ProjectileEntity]を返すブロック
 * @return サーバー側でエンティティがスポーンした場合はtrue，それ以外の場合はfalse
 */
fun PlayerEntity.throwEntity(world: World, entityBuilder: (World, PlayerEntity) -> ProjectileEntity?): Boolean {
    world.playSound(
        null,
        x,
        y,
        z,
        SoundEvents.ENTITY_SNOWBALL_THROW,
        SoundCategory.PLAYERS,
        0.5f,
        0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f),
    )
    if (!world.isClient) {
        val entity: ProjectileEntity = entityBuilder(world, this) ?: return false
        entity.setVelocity(this, pitch, yaw, 0.0f, 1.5f, 1.0f)
        return world.spawnEntity(entity)
    }
    return false
}

/**
 * [Hand.MAIN_HAND]から[ItemStack]を返します。
 */
fun LivingEntity.getStackInMainHand(): ItemStack = getStackInHand(Hand.MAIN_HAND)

/**
 * 指定した値からエフェクトを追加します。
 * @param effect 追加する効果
 * @param duration 効果の時間 [/tick]
 * @param amplifier 0をレベル1とする効果の強度
 * @param ambient ビーコンやコンジットのような表示にするかどうか
 * @param showParticles パーティクルを表示するかどうか
 * @param showIcon エフェクトのアイコンを表示するかどうか
 */
fun LivingEntity.addStatusEffect(
    effect: RegistryEntry<StatusEffect>,
    duration: Int,
    amplifier: Int = 0,
    ambient: Boolean = false,
    showParticles: Boolean = true,
    showIcon: Boolean = false,
) {
    addStatusEffect(
        StatusEffectInstance(
            effect,
            duration,
            amplifier,
            ambient,
            showParticles,
            showIcon,
            null,
        ),
    )
}

/**
 * 指定した値から無限の効果時間をもつエフェクトを追加します。
 * @param effect 追加する効果
 * @param amplifier 0をレベル1とする効果の強度
 * @param ambient ビーコンやコンジットのような表示にするかどうか
 * @param showParticles パーティクルを表示するかどうか
 * @param showIcon エフェクトのアイコンを表示するかどうか
 */
fun LivingEntity.addInfinityStatusEffect(
    effect: RegistryEntry<StatusEffect>,
    amplifier: Int = 0,
    ambient: Boolean = false,
    showParticles: Boolean = true,
    showIcon: Boolean = false,
) {
    addStatusEffect(
        effect,
        -1,
        amplifier,
        ambient,
        showParticles,
        showIcon,
    )
}

fun ThrownItemEntity.setItemFromOwner(owner: LivingEntity): ThrownItemEntity = apply {
    setItem(owner.getStackInMainHand())
}

//    Color    //

/**
 * 指定した[color]を[Float]値に変換します。
 */
fun toFloatColor(color: Int): Triple<Float, Float, Float> {
    val red: Float = (color shr 16 and 255) / 255.0f
    val green: Float = (color shr 8 and 255) / 255.0f
    val blue: Float = (color and 255) / 255.0f
    return Triple(red, green, blue)
}

//    EnergyStorage    //

/**
 * 蓄電率を返します。
 */
val EnergyStorage.energyPercent: Float
    get() = (amount.toFloat() / capacity.toFloat()) * 100

//    FabricLoader    //

/**
 * 指定した[modId]が読み込まれているか判定します。
 */
fun isModLoaded(modId: String): Boolean = FabricLoader.getInstance().isModLoaded(modId)

/**
 * 現在の物理サイドを取得します。
 */
fun getEnvType(): EnvType = FabricLoader.getInstance().environmentType

/**
 * 現在の物理サイドがクライアント側かどうか判定します。
 */
fun isClientEnv(): Boolean = FabricLoader.getInstance().environmentType == EnvType.CLIENT

/**
 * 現在の物理サイドがサーバー側かどうか判定します。
 */
fun isServerEnv(): Boolean = FabricLoader.getInstance().environmentType == EnvType.SERVER

/**
 * 現在の起動構成がデータ生成であるか判定します。
 */
fun isDataGen(): Boolean = System.getProperty("fabric-api.datagen") != null

/**
 * 指定した[modId]から[ModMetadata]を返します。
 * @return [FabricLoader.getModContainer]がnullの場合はnull
 */
fun getModMetadata(modId: String): ModMetadata? = FabricLoader
    .getInstance()
    .getModContainer(modId)
    .getOrNull()
    ?.metadata

/**
 * 指定した[modId]からModの名前を返します。
 * @return [getModMetadata]がnullの場合はnull
 */
fun getModName(modId: String): String? = getModMetadata(modId)?.name

/**
 * 指定した[key]からエントリポイントの一覧を返します。
 * @param T エントリポイントのクラス
 */
inline fun <reified T : Any> collectEntrypoints(key: String): List<T> = FabricLoader.getInstance().getEntrypoints(key, T::class.java)

//    Fluid    //

/**
 * 液体の名前を返します。
 */
val Fluid.name: MutableText
    get() = FluidVariant.of(this).name

/**
 * 液体の名前を返します。
 */
val FluidVariant.name: MutableText
    get() = FluidVariantAttributes.getName(this).copy()

/**
 * 液体が[Fluids.EMPTY]に一致するか判定します。。
 */
val Fluid.isEmpty: Boolean
    get() = this == Fluids.EMPTY

//    Identifier    //

/**
 * [Identifier]を[splitter]で分割された文字列として返します。
 */
fun Identifier.splitWith(splitter: Char): String = "${namespace}${splitter}$path"

//    ServiceLoader    //

inline fun <reified T : Any> collectInstances(): Iterable<T> = ServiceLoader.load(T::class.java)
