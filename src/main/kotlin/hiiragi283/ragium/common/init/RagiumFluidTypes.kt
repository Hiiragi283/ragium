package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.BlockAndTintGetter
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.pathfinder.PathType
import net.neoforged.neoforge.common.SoundActions
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries

object RagiumFluidTypes {
    @JvmField
    val REGISTER: DeferredRegister<FluidType> =
        DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, RagiumAPI.MOD_ID)

    @JvmStatic
    fun register(name: String, properties: FluidType.Properties): DeferredHolder<FluidType, FluidType> =
        REGISTER.register(name) { _: ResourceLocation ->
            FluidType(
                properties
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY),
            )
        }

    @JvmField
    val HONEY: DeferredHolder<FluidType, FluidType> = REGISTER.register("honey") { _: ResourceLocation ->
        object : FluidType(Properties.create()) {
            override fun getBlockForFluidState(getter: BlockAndTintGetter, pos: BlockPos, state: FluidState): BlockState =
                Blocks.HONEY_BLOCK.defaultBlockState()
        }
    }

    @JvmField
    val SNOW: DeferredHolder<FluidType, FluidType> = REGISTER.register("snow") { _: ResourceLocation ->
        object : FluidType(Properties.create()) {
            override fun getBucket(stack: FluidStack): ItemStack = ItemStack(Items.POWDER_SNOW_BUCKET)

            override fun getBlockForFluidState(getter: BlockAndTintGetter, pos: BlockPos, state: FluidState): BlockState =
                Blocks.POWDER_SNOW.defaultBlockState()
        }
    }

    @JvmField
    val CRUDE_OIL: DeferredHolder<FluidType, FluidType> = register(
        "crude_oil",
        FluidType.Properties
            .create()
            .descriptionId("block.ragium.crude_oil")
            .canSwim(false)
            .pathType(PathType.LAVA)
            .density(3000)
            .viscosity(6000),
    )

    @JvmField
    val GASEOUS: DeferredHolder<FluidType, FluidType> = register(
        "gaseous",
        FluidType.Properties
            .create()
            .canSwim(false)
            .canDrown(false)
            .pathType(null)
            .density(-1000),
    )
}
