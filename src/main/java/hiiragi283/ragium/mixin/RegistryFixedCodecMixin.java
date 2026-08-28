package hiiragi283.ragium.mixin;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryFixedCodec;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RegistryFixedCodec.class)
public abstract class RegistryFixedCodecMixin<E> {
    @Inject(
            method =
                    "encode(Lnet/minecraft/core/Holder;Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;",
            at = @At("HEAD"),
            cancellable = true)
    private <T> void ragium$encode(
            Holder<E> input, DynamicOps<T> ops, T prefix, CallbackInfoReturnable<DataResult<T>> cir) {
        if (!DatagenModLoader.isRunningDataGen()) return;
        // 入力がDeferredHolderかつ値が紐づかない場合，IDから強制的にエンコードさせる
        if (input instanceof DeferredHolder<E, ?> holder && !holder.isBound()) {
            cir.setReturnValue(Identifier.CODEC.encode(holder.getId(), ops, prefix));
        }
    }
}
