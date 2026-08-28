package hiiragi283.ragium.mixin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Registry.class)
public interface RegistryMixin<E> {
    @Inject(method = "holderByNameCodec", at = @At("RETURN"), cancellable = true)
    private void ragium$holderByNameCodec(CallbackInfoReturnable<Codec<Holder<E>>> cir) {
        if (!DatagenModLoader.isRunningDataGen()) return;
        Codec<Holder<E>> original = cir.getReturnValue();
        cir.setReturnValue(Codec.of(
                new Encoder<>() {
                    @Override
                    public <T> DataResult<T> encode(Holder<E> input, DynamicOps<T> ops, T prefix) {
                        // 入力がDeferredHolderかつ値が紐づかない場合，IDから強制的にエンコードさせる
                        if (input instanceof DeferredHolder<E, ?> holder && !holder.isBound()) {
                            return Identifier.CODEC.encode(holder.getId(), ops, prefix);
                        } else {
                            return original.encode(input, ops, prefix);
                        }
                    }
                },
                original,
                original.toString()));
    }
}
