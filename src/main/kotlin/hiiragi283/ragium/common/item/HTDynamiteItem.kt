package hiiragi283.ragium.common.item

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.boolText
import hiiragi283.ragium.api.extension.floatText
import hiiragi283.ragium.api.extension.getStackInActiveHand
import hiiragi283.ragium.common.entity.HTDynamiteEntity
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipAppender
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Position
import net.minecraft.world.World
import java.util.function.Consumer

class HTDynamiteItem(val action: HTDynamiteEntity.Action, settings: Settings) : HTThrowableItem(settings) {
    override fun appendTooltip(
        stack: ItemStack,
        context: TooltipContext,
        tooltip: MutableList<Text>,
        type: TooltipType,
    ) {
        stack.get(RagiumComponentTypes.DYNAMITE)?.appendTooltip(context, tooltip::add, type)
    }

    override fun createEntity(world: World, user: LivingEntity): ThrownItemEntity = HTDynamiteEntity(world, user).apply {
        setItem(user.getStackInActiveHand())
        this.action = this@HTDynamiteItem.action
    }

    //    ProjectileItem    //

    override fun createEntity(
        world: World,
        pos: Position,
        stack: ItemStack,
        direction: Direction,
    ): ProjectileEntity = HTDynamiteEntity(world, pos.x, pos.y, pos.z).apply {
        setItem(stack)
        this.action = this@HTDynamiteItem.action
    }

    //    Component    //

    data class Component(val power: Float, val canDestroy: Boolean) : TooltipAppender {
        companion object {
            @JvmField
            val DEFAULT = Component(2f, true)

            @JvmField
            val CODEC: Codec<Component> = RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        Codec.floatRange(0f, 16f).optionalFieldOf("power", 2f).forGetter(Component::power),
                        Codec.BOOL.optionalFieldOf("can_destroy", true).forGetter(Component::canDestroy),
                    ).apply(instance, ::Component)
            }

            @JvmField
            val PACKET_CODEC: PacketCodec<RegistryByteBuf, Component> = PacketCodec.tuple(
                PacketCodecs.FLOAT,
                Component::power,
                PacketCodecs.BOOL,
                Component::canDestroy,
                ::Component,
            )
        }

        init {
            check(power in (0f..16f)) { "Invalid explosion power; $power (allowed range is 0f ~ 16f)" }
        }

        private val sourceType: World.ExplosionSourceType = when (canDestroy) {
            true -> World.ExplosionSourceType.TNT
            false -> World.ExplosionSourceType.NONE
        }

        fun createExplosion(
            world: World,
            entity: Entity,
            x: Double,
            y: Double,
            z: Double,
        ) {
            world.createExplosion(entity, x, y, z, power, false, sourceType)
        }

        //    TooltipAppender    //

        override fun appendTooltip(context: TooltipContext, tooltip: Consumer<Text>, type: TooltipType) {
            tooltip.accept(
                Text
                    .translatable(
                        RagiumTranslationKeys.DYNAMITE_POWER,
                        floatText(power).formatted(Formatting.WHITE),
                    ).formatted(Formatting.GRAY),
            )
            val destroyText: MutableText = boolText(canDestroy).formatted(
                when (canDestroy) {
                    true -> Formatting.RED
                    false -> Formatting.AQUA
                },
            )
            tooltip.accept(
                Text
                    .translatable(
                        RagiumTranslationKeys.DYNAMITE_DESTROY,
                        destroyText,
                    ).formatted(Formatting.GRAY),
            )
        }
    }
}
